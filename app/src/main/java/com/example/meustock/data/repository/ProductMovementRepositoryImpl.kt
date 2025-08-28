package com.example.meustock.data.repository

import android.util.Log
import com.example.meustock.data.mappers.toDomain
import com.example.meustock.data.models.ProductDto
import com.example.meustock.data.models.MovementDto
import com.example.meustock.domain.model.Product
import com.example.meustock.domain.model.ProductMovement
import com.example.meustock.domain.repository.ProductMovementRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

/**
 * Implementação do repositório para gerenciar movimentos de produtos no Firestore.
 */
class ProductMovementRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : ProductMovementRepository {

    // Referência à coleção de produtos no Firestore
    private val productsCollection = firestore.collection("products")

    /**
     * Obtém uma lista de movimentos de um produto específico em tempo real.
     * Usa `callbackFlow` para escutar mudanças no Firestore.
     **/

    override suspend fun getProductMovements(productId: String): Flow<List<ProductMovement>> = callbackFlow {
        val movementsCollection = productsCollection.document(productId).collection("movements")
        val listener = movementsCollection
            .orderBy("date", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error) // Emite um erro e fecha o flow
                    Log.e("ProductMovementRepo", "Erro ao obter movimentos para o produto $productId", error)
                    return@addSnapshotListener
                }

                // Mapeia os documentos para objetos de domínio
                val movements = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(MovementDto::class.java)?.toDomain()
                } ?: emptyList()

                trySend(movements) // Emite a lista de movimentos
                Log.d("ProductMovementRepo", "Movimentos recebidos: ${movements.size}")
            }

        // Aguarda o fechamento do flow para remover o listener
        awaitClose { listener.remove() }
    }

    override suspend fun searchProducts(query: String): List<Product> {
        if (query.isBlank()) return emptyList()

        return try {
            val byId = productsCollection
                .orderBy("idProduct")
                .startAt(query.uppercase()) 
                .endAt(query.uppercase() + "\uf8ff")
                .limit(10)
                .get()
                .await()


            if (!byId.isEmpty) {
                return byId.documents.mapNotNull { it.toObject(ProductDto::class.java)?.toDomain() }
            }

            // Busca por nome (prefix search)
            val byName = productsCollection
                .orderBy("name")
                .startAt(query)
                .endAt(query + "\uf8ff")
                .limit(10)
                .get()
                .await()

            byName.documents.mapNotNull { it.toObject(ProductDto::class.java)?.toDomain() }
        } catch (e: Exception) {
            Log.e("ProductRepo", "Erro na busca: $query", e)
            emptyList()
        }
    }

    /**
     * Registra um novo movimento de estoque para um produto.
     */
    override suspend fun registerProductMovement(
        productId: String,
        quantity: Int,
        type: String,
        responsible: String?,
        notes: String?
    ) {
        val productRef = productsCollection.document(productId)
        val movementId = UUID.randomUUID().toString()
        val movementRef = productRef.collection("movements").document(movementId)

        firestore.runTransaction { transaction ->
            val snapshot = transaction.get(productRef)
            val currentProduct = snapshot.toObject(ProductDto::class.java)
                ?: throw Exception("Produto não encontrado")

            val newStock = currentProduct.currentStock + if (type == "Entrada") quantity else -quantity
            if (newStock < 0) throw Exception("Estoque insuficiente")

            val updatedProduct = currentProduct.copy(
                currentStock = newStock,
                lastUpdateDate = System.currentTimeMillis()
            )
            transaction.set(productRef, updatedProduct)

            val movement = MovementDto(
                id = movementId,
                productId = productId,
                quantity = quantity,
                type = type,
                date = System.currentTimeMillis(),
                responsible = responsible,
                notes = notes
            )
            transaction.set(movementRef, movement)
        }.await()
    }

    /**
     * Adiciona uma quantidade ao estoque de um produto de forma segura (usando transação).
     */
    override suspend fun addProductStock(productId: String, quantity: Int) {
        updateProductStock(productId, quantity)
    }

    /**
     * Remove uma quantidade do estoque de um produto de forma segura (usando transação).
     * Lança uma exceção se a quantidade for maior que o estoque atual.
     */
    override suspend fun removeProductStock(productId: String, quantity: Int) {
        updateProductStock(productId, -quantity)
    }

    /**
     * Função privada para abstrair a lógica de transação de estoque,
     * usada tanto para adição quanto para remoção.
     */
    private suspend fun updateProductStock(productId: String, quantityDelta: Int) {
        val docRef = productsCollection.document(productId)
        firestore.runTransaction { transaction ->
            val snapshot = transaction.get(docRef)
            val currentProduct = snapshot.toObject(ProductDto::class.java)
                ?: throw Exception("Produto com ID $productId não encontrado.")

            val newStock = currentProduct.currentStock + quantityDelta
            if (newStock < 0) {
                throw Exception("Estoque insuficiente. Ação cancelada.")
            }

            val updatedProduct = currentProduct.copy(
                currentStock = newStock,
                lastUpdateDate = System.currentTimeMillis()
            )
            transaction.set(docRef, updatedProduct)
        }.await()
    }


    /**
     * Obtém todos os produtos da coleção.
     */
    override suspend fun getAllProducts(): List<Product> {
        return try {
            val snapshot = productsCollection.get().await()
            snapshot.documents.mapNotNull { it.toObject(ProductDto::class.java)?.toDomain() }
        } catch (e: Exception) {
            Log.e("ProductMovementRepo", "Erro ao buscar todos os produtos", e)
            emptyList()
        }
    }

    /**
     * Escuta mudanças em um produto específico em tempo real.
     */
    override fun listenProductById(productId: String): Flow<Product?> = callbackFlow {
        val listener = productsCollection.document(productId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    Log.e("ProductMovementRepo", "Erro ao escutar produto $productId", error)
                    return@addSnapshotListener
                }

                val product = snapshot?.toObject(ProductDto::class.java)?.toDomain()
                trySend(product)
            }
        awaitClose { listener.remove() }
    }

    /**
     * Obtém uma lista dos movimentos mais recentes de todos os produtos.
     * Usa `collectionGroup` para buscar movimentos de subcoleções.
     */
    override suspend fun listenRecentMovements(limit: Long): Flow<List<ProductMovement>> = callbackFlow {
        val listener = firestore.collectionGroup("movements")
            .orderBy("date", Query.Direction.DESCENDING)
            .limit(limit)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    Log.e("ProductMovementRepo", "Erro ao escutar movimentos recentes", error)
                    return@addSnapshotListener
                }

                val movements = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(MovementDto::class.java)?.toDomain()
                } ?: emptyList()

                trySend(movements)
            }
        awaitClose { listener.remove() }
    }
}
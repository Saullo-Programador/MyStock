package com.example.meustock.data.repository

import android.util.Log
import com.example.meustock.data.mappers.toDomain
import com.example.meustock.data.mappers.toDto
import com.example.meustock.data.models.ProductDto
import com.example.meustock.domain.model.Product
import com.example.meustock.domain.repository.ProductRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * Implementação do repositório para gerenciar produtos no Firestore.
 */
class ProductRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : ProductRepository {

    // Referência à coleção de produtos no Firestore
    private val collection = firestore.collection("products")

    /**
     * Obtém uma lista de todos os produtos em tempo real.
     * Ideal para UI que precisa de atualizações constantes.
     */
    override suspend fun getProducts(): Flow<List<Product>> = callbackFlow {

        val uid = FirebaseAuth.getInstance().currentUser?.uid
            ?: run {
                trySend(emptyList())
                close()
                return@callbackFlow
            }

        val registration = collection
            .whereEqualTo("createdBy", uid)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    close(e) // Emite o erro e fecha o flow
                    Log.e("ProductRepo", "Erro ao buscar produtos", e)
                    return@addSnapshotListener
                }

                // Mapeia os documentos para objetos de domínio
                val products = snapshot?.documents?.mapNotNull {
                    it.toObject(ProductDto::class.java)?.toDomain()
                } ?: emptyList()

                trySend(products) // Emite a nova lista de produtos
            }
        awaitClose { registration.remove() } // Remove o listener ao fechar o flow
    }

    /**
     * Gera o próximo código de produto.
     * Busca o último código e incrementa.
     */
    override suspend fun getNextProductCode(): String {
        val querySnapshot = collection
            .orderBy("idProduct", Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .await()

        val lastCode = querySnapshot.documents.firstOrNull()
            ?.getString("idProduct")
            ?.removePrefix("PROD-")
            ?.toIntOrNull() ?: 0

        val nextCode = lastCode + 1
        return "PROD-${nextCode.toString().padStart(4, '0')}"
    }

    /**
     * Adiciona um novo produto ao Firestore.
     */
    override suspend fun addProduct(product: Product) {

        val uid = FirebaseAuth.getInstance().currentUser?.uid
            ?: throw Exception("Usuário não autenticado")

        val productWithUser = product.copy(createdBy = uid)

        collection.document(product.idProduct)
            .set(productWithUser.toDto())
            .await()

    }

    /**
     * Obtém um produto específico em tempo real.
     */
    override suspend fun detailProduct(productId: String): Flow<Product?> = callbackFlow {
        val listener = collection.document(productId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    Log.e("ProductRepo", "Erro ao escutar produto $productId", error)
                    return@addSnapshotListener
                }

                val product = snapshot?.toObject(ProductDto::class.java)?.toDomain()
                trySend(product)
            }
        awaitClose { listener.remove() }
    }

    /**
     * Obtém um produto específico uma única vez por ID.
     */
    override suspend fun getProductById(productId: String): Product? {
        return try {
            val snapshot = collection.document(productId).get().await()
            if (snapshot.exists()) {
                snapshot.toObject(ProductDto::class.java)?.toDomain()
            } else null
        } catch (e: Exception) {
            Log.e("ProductRepo", "Erro ao buscar produto por ID $productId", e)
            null
        }
    }

    /**
     * Atualiza um produto existente.
     * Usa o mesmo metodo `set` com o ID do documento.
     */
    override suspend fun updateProduct(product: Product) {
        collection.document(product.idProduct)
            .set(product.toDto())
            .await()
    }

    /**
     * Deleta um produto pelo seu ID.
     */
    override suspend fun deleteProduct(product: Product) {
        collection.document(product.idProduct)
            .delete()
            .await()
    }
}
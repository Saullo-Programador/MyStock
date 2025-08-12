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

class ProductMovementRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
): ProductMovementRepository {

    private val collection = firestore.collection("products")

    override suspend fun getProductMovements(productId: String): Flow<List<ProductMovement>> = callbackFlow {
        val listener = collection.document(productId)
            .collection("movements")
            .orderBy("date", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val movements = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(MovementDto::class.java)?.toDomain()
                    }
                    trySend(movements)
                }
            }

        awaitClose { listener.remove() }
    }


    override suspend fun registerProductMovement(
        productId: String,
        quantity: Int,
        type: String,
        responsible: String?,
        notes: String?
    ) {
        val movement = MovementDto(
            id = UUID.randomUUID().toString(),
            productId = productId,
            quantity = quantity,
            type = type,
            date = System.currentTimeMillis(),
            responsible = responsible,
            notes = notes
        )

        collection.document(productId)
            .collection("movements")
            .document(movement.id)
            .set(movement)
            .await()
    }

    // Adicionar estoque a um produto
    override suspend fun addProductStock(productId: String, quantity: Int) {
        val docRef = collection.document(productId)
        firestore.runTransaction { transaction ->
            val snapshot = transaction.get(docRef)
            val current = snapshot.toObject(ProductDto::class.java)
                ?: throw Exception("Produto não encontrado")

            val updatedStock = current.copy(
                currentStock = current.currentStock + quantity,
                lastUpdateDate = System.currentTimeMillis()
            )
            transaction.set(docRef, updatedStock)
        }.await()
    }

    // Remover estoque de um produto
    override suspend fun removeProductStock(productId: String, quantity: Int) {
        val docRef = collection.document(productId)
        firestore.runTransaction { transaction ->
            val snapshot = transaction.get(docRef)
            val current = snapshot.toObject(ProductDto::class.java)
                ?: throw Exception("Produto não encontrado")

            val newStock = current.currentStock - quantity
            if (newStock < 0) {
                throw Exception("Estoque insuficiente para retirada")
            }

            val updatedStock = current.copy(
                currentStock = newStock,
                lastUpdateDate = System.currentTimeMillis()
            )
            transaction.set(docRef, updatedStock)
        }.await()
    }

    // Buscar produto por código ou nome
    override suspend fun getProductsByCodeOrName(query: String): Product? {
        val snapshot = collection
            .whereEqualTo("idProduct", query)
            .get()
            .await()

        val first = snapshot.documents.firstOrNull()
            ?: collection.whereEqualTo("name", query).get().await().documents.firstOrNull()

        return first?.toObject(ProductDto::class.java)?.toDomain()
    }


    override suspend fun getAllProducts(): List<Product> {
        return try {
            val snapshot = collection.get().await()
            snapshot.documents.mapNotNull { doc ->
                doc.toObject(ProductDto::class.java)?.toDomain()
            }
        } catch (e: Exception) {
            Log.e("ProductRepository", "Erro ao buscar todos os produtos", e)
            emptyList()
        }
    }


}
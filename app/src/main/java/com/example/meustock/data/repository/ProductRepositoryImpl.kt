package com.example.meustock.data.repository

import com.example.meustock.data.mappers.toDomain
import com.example.meustock.data.mappers.toDto
import com.example.meustock.data.models.ProductDto
import com.example.meustock.domain.model.Product
import com.example.meustock.domain.repository.ProductRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
): ProductRepository {
    private val collection = firestore.collection("products")

    override suspend fun getProducts(): Flow<List<Product>> = callbackFlow {
        val registration = collection.addSnapshotListener { snapshot, e ->
            if (e != null) {
                close(e)
                return@addSnapshotListener
            }
            if (snapshot != null && !snapshot.isEmpty) {
                val products = snapshot.documents.mapNotNull {
                    it.toObject(ProductDto::class.java)?.toDomain()
                }
                trySend(products) // Emita a nova lista de produtos
            } else {
                trySend(emptyList()) // Nenhum produto ou snapshot vazio
            }
        }
        awaitClose {
            registration.remove()
        }
    }

    override suspend fun addProduct(product: Product){
        collection.add(product.toDto()).await()
    }

    override suspend fun updateProduct(product: Product){
        collection.document(product.id)
            .set(product.toDto())
            .await()
    }

    override suspend fun deleteProduct(product: Product){
        collection.document(product.id)
            .delete()
            .await()
    }

}
package com.example.meustock.data.repository

import android.util.Log
import com.example.meustock.data.mappers.toDomain
import com.example.meustock.data.mappers.toDto
import com.example.meustock.data.models.ProductDto
import com.example.meustock.domain.model.Product
import com.example.meustock.domain.repository.ProductRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
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

    override suspend fun getNextProductCode(): String{
        val querySnapshot = collection
            .orderBy("idProduct", Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .await()

        val lastCode = querySnapshot.documents.firstOrNull()
            ?.getString("idProduct")
            ?.filter { it.isDigit() }
            ?.toIntOrNull() ?: 0

        val nextCode = lastCode + 1
        return "PROD-"+nextCode.toString().padStart(4, '0')
    }

    override suspend fun addProduct(product: Product){
        collection.document(product.id)
            .set(product.toDto())
            .await()
    }

    override suspend fun detailProduct(productId: String): Flow<Product> = callbackFlow {
        val listener = collection.document(productId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("ProductRepo", "Erro ao escutar documento", error)
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    val dto = snapshot.toObject(ProductDto::class.java)
                    if (dto != null) {
                        trySend(dto.toDomain())
                    } else {
                        Log.w("ProductRepo", "DTO convertido é null")
                    }
                } else {
                    Log.w("ProductRepo", "Documento não existe para ID: $productId")
                }
            }

        awaitClose { listener.remove() }
    }


    override suspend fun getProductById(id: String): Product? {
        return try {
            val snapshot = firestore.collection("products")
                .document(id)
                .get()
                .await()

            if (snapshot.exists()) {
                snapshot.toObject(ProductDto::class.java)?.toDomain()
            } else null
        } catch (e: Exception) {
            Log.e("ProductRepository", "Erro ao buscar produto por ID", e)
            null
        }
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
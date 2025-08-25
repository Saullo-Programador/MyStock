package com.example.meustock.domain.repository

import com.example.meustock.domain.model.Product
import com.example.meustock.domain.model.ProductMovement
import kotlinx.coroutines.flow.Flow

interface ProductMovementRepository{

    suspend fun listenRecentMovements(limit: Long = 5): Flow<List<ProductMovement>>
    suspend fun addProductStock(productId: String, quantity: Int)
    suspend fun removeProductStock(productId: String, quantity: Int)
    suspend fun getProductMovements(productId: String): Flow<List<ProductMovement>>
    suspend fun getProductsByCodeOrName(query: String): Product?
    suspend fun registerProductMovement(
        productId: String,
        quantity: Int,
        type: String,
        responsible: String? = null,
        notes: String? = null
    )
    suspend fun getAllProducts(): List<Product>
    fun listenProductById(productId: String): Flow<Product?>

}
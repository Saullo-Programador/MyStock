package com.example.meustock.domain.repository

import com.example.meustock.domain.model.Product
import com.example.meustock.domain.model.ProductMovement
import kotlinx.coroutines.flow.Flow

interface ProductRepository{
    suspend fun getProducts(): Flow<List<Product>>
    suspend fun getNextProductCode(): String
    suspend fun addProduct(product: Product)
    suspend fun deleteProduct(product: Product)
    suspend fun updateProduct(product: Product)
    suspend fun detailProduct(productId: String): Flow<Product>
    suspend fun getProductById(id: String): Product?



}
package com.example.meustock.domain.repository

import com.example.meustock.data.repository.ProductRepositoryImpl
import com.example.meustock.domain.model.Product
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface ProductRepository{
    suspend fun getProducts(): Flow<List<Product>>
    suspend fun addProduct(product: Product)
    suspend fun deleteProduct(product: Product)
    suspend fun updateProduct(product: Product)
}
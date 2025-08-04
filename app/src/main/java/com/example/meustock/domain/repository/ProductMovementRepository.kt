package com.example.meustock.domain.repository

import com.example.meustock.domain.model.Product
import com.example.meustock.domain.model.ProductMovement
import kotlinx.coroutines.flow.Flow

interface ProductMovementRepository{
    suspend fun addProductStock(productId: String, quantity: Int)
    suspend fun removeProductStock(productId: String, quantity: Int)
    suspend fun getProductMovements(productId: String): Flow<List<ProductMovement>>

    suspend fun getProductsByCodeOrName(query: String): Product?
}
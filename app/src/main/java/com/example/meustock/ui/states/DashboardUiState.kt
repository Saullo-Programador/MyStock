package com.example.meustock.ui.states

import com.example.meustock.domain.model.Product

data class DashboardUiState(
    val totalProducts: Int = 0,
    val totalItemsInStock: Int = 0,
    val totalStockValue: Double = 0.0,
    val lowStockItems: Int = 0,
    val restockProducts: List<Product> = emptyList()
    //val lastMovements: List<ProductMovement> = emptyList()
)

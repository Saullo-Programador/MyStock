package com.example.meustock.ui.states

data class DashboardUiState(
    val totalProducts: Int = 0,
    val totalItemsInStock: Int = 0,
    val totalStockValue: Double = 0.0,
    //val lastMovements: List<ProductMovement> = emptyList()
)

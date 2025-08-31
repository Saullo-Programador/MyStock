package com.example.meustock.ui.states

import com.example.meustock.domain.model.Product

data class ProductStockUiState(
    val query: String = "",
    val selectedProduct: Product? = null,
    val quantity: String = "",
)

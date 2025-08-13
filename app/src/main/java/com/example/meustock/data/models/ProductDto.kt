package com.example.meustock.data.models

data class ProductDto(
    val idProduct: String = "",
    val imageUrl: String? = null,
    val name: String = "",
    val description: String? = null,
    val barcodeSku: String? = null,
    val costPrice: Double = 0.0,
    val sellingPrice: Double = 0.0,
    val currentStock: Int = 0,
    val minimumStock: Int = 0,
    val category: String = "",
    val brand: String? = null,
    val unitOfMeasurement: String = "",
    val supplier: String? = null,
    val stockLocation: String? = null,
    val status: String = "Ativo",
    val notes: String? = null,
    val registrationDate: Long = 0L,
    val lastUpdateDate: Long = 0L
)

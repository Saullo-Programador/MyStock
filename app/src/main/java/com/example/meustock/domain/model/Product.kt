package com.example.meustock.domain.model

import java.util.UUID

data class Product(
    val idProduct: String,
    val imageUrl: String? = null,
    val imagePublicId: String? = null,
    val name: String,
    val description: String? = null,
    val barcodeSku: String? = null,
    val costPrice: Double,
    val sellingPrice: Double,
    val currentStock: Int,
    val minimumStock: Int,
    val category: String,
    val brand: String? = null,
    val unitOfMeasurement: String,
    val supplier: String? = null,
    val stockLocation: String? = null,
    val status: String = "Ativo", // Ex: "Ativo", "Inativo", "Promoção"
    val notes: String? = null,
    val registrationDate: Long = System.currentTimeMillis(),
    val lastUpdateDate: Long = System.currentTimeMillis()
)
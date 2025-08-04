package com.example.meustock.domain.model

data class ProductMovement(
    val id: String,
    val productId: String,
    val quantity: Int,
    val type: String, // "entrada" ou "saida"
    val date: Long,
    val responsible: String?,
    val notes: String?
)

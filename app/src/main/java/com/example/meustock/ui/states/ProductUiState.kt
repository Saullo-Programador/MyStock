package com.example.meustock.ui.states

import java.util.UUID

data class ProductUiState(
    // Campos de identificação e metadados, geralmente gerados internamente
    val id: String = UUID.randomUUID().toString(),
    val registrationDate: Long = System.currentTimeMillis(),
    val lastUpdateDate: Long = System.currentTimeMillis(),

    // Campos do formulário (valores atuais)
    val nameProduct: String = "", // Nome do Produto
    val description: String? = "", // Descrição do Produto
    val barcodeSku: String? = "", // Código de Barras do Produto
    val costPrice: Double = 0.0, // Preço de Custo do Produto
    val sellingPrice: Double = 0.0, // Preço de Venda do Produto
    val currentStock: Int = 0, // Estoque Atual do Produto
    val minimumStock: Int = 0, // Estoque Mínimo do Produto
    val category: String = "", // Categoria do Produto
    val brand: String? = "", // Marca do Produto
    val unitOfMeasurement: String = "", // Unidade de Medida do Produto: Como o produto é vendido (ex: unidade, caixa, metro, quilo).
    val supplier: String? = "", // Fornecedor do Produto
    val stockLocation: String? = "", // Localização no Estoque
    val status: String = "Ativo", // Status do Produto (ex: ativo, inativo, promoção, etc)
    val notes: String? = "", // Observações

)

package com.example.meustock.ui.states

import java.util.UUID

data class ProductUiState(
    // Campos de identificação e metadados, geralmente gerados internamente
    val idProduct: String = "", // Código do Produto

    val registrationDate: Long = System.currentTimeMillis(),
    val lastUpdateDate: Long = System.currentTimeMillis(),

    // Campos do formulário (valores atuais)
    val imageUrl: String? = null, // URL da Imagem do Produto
    val nameProduct: String = "", // Nome do Produto
    val description: String? = "", // Descrição do Produto
    val barcodeSku: String? = "", // Código de Barras do Produto
    val costPrice: String = "", // Preço de Custo do Produto
    val sellingPrice: String = "", // Preço de Venda do Produto
    val currentStock: String = "", // Estoque Atual do Produto
    val minimumStock: String = "", // Estoque Mínimo do Produto
    val category: String = "", // Categoria do Produto
    val brand: String? = "", // Marca do Produto
    val unitOfMeasurement: String = "", // Unidade de Medida do Produto: Como o produto é vendido (ex: unidade, caixa, metro, quilo).
    val supplier: String? = "", // Fornecedor do Produto
    val stockLocation: String? = "", // Localização no Estoque
    val status: String = "Ativo", // Status do Produto (ex: ativo, inativo, promoção, etc)
    val notes: String? = "", // Observações

)

sealed class ProductFormEvent {
    object Loading : ProductFormEvent()
    object Success : ProductFormEvent()
    data class Error(val message: String) : ProductFormEvent()
    object Idle : ProductFormEvent() // Estado inicial ou reset
}
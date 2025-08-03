package com.example.meustock.ui.states

import com.example.meustock.domain.model.Product
import java.util.UUID

data class ProductEditUiState(
    val id: String = UUID.randomUUID().toString(),
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

    val onRegistrationDateChange: (Long) -> Unit = {},
    val onLastUpdateDateChange: (Long) -> Unit = {},
    val onImageUrlChange: (String?) -> Unit = {},
    val onNameProductChange: (String) -> Unit = {},
    val onDescriptionChange: (String?) -> Unit = {},
    val onBarcodeSkuChange: (String?) -> Unit = {},
    val onCostPriceChange: (String) -> Unit = {},
    val onSellingPriceChange: (String) -> Unit = {},
    val onCurrentStockChange: (String) -> Unit = {},
    val onMinimumStockChange: (String) -> Unit = {},
    val onCategoryChange: (String) -> Unit = {},
    val onBrandChange: (String?) -> Unit = {},
    val onUnitOfMeasurementChange: (String) -> Unit = {},
    val onSupplierChange: (String?) -> Unit = {},
    val onStockLocationChange: (String?) -> Unit = {},
    val onStatusChange: (String) -> Unit = {},
    val onNotesChange: (String?) -> Unit = {},
    val isLoading: Boolean = false
)

fun Product.toEditUiState(onUpdate: (ProductEditUiState) -> Unit): ProductEditUiState {
    return ProductEditUiState(
        id = id,
        idProduct = idProduct,
        registrationDate = registrationDate,
        lastUpdateDate = lastUpdateDate,
        imageUrl = imageUrl,
        nameProduct = name,
        description = description,
        barcodeSku = barcodeSku,
        costPrice = costPrice.toString(),
        sellingPrice = sellingPrice.toString(),
        currentStock = currentStock.toString(),
        minimumStock = minimumStock.toString(),
        category = category,
        brand = brand,
        unitOfMeasurement = unitOfMeasurement,
        supplier = supplier,
        stockLocation = stockLocation,
        status = status,
        notes = notes,
        // Os lambdas você pode injetar fora ou dentro da função
    )
}

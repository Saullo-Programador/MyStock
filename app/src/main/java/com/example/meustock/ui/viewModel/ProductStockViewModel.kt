package com.example.meustock.ui.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meustock.domain.repository.ProductMovementRepository
import com.example.meustock.domain.usecase.GetProductsByCodeOrNameUseCase
import com.example.meustock.domain.usecase.ListenProductByIdUseCase
import com.example.meustock.ui.states.ProductStockUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para a tela de movimentação de estoque.
 * Gerencia a pesquisa, seleção e aplicação de entradas/saídas de estoque.
 */
@HiltViewModel
class ProductStockViewModel @Inject constructor(
    private val repository: ProductMovementRepository,
    private val getProductsByCodeOrName: GetProductsByCodeOrNameUseCase,
    private val listenProductById: ListenProductByIdUseCase
) : ViewModel() {

    // Usa `mutableStateOf` para estados que não precisam de `StateFlow` e são mais simples
    var uiState by mutableStateOf(ProductStockUiState())
        private set
    var expanded by mutableStateOf(false)
        private set
    var searchResults by mutableStateOf<List<String>>(emptyList())
        private set

    fun onExpandedChange(exp: Boolean) { expanded = exp }
    fun onQuantityChange(quantity: String) { uiState = uiState.copy(quantity = quantity) }
    fun onQueryChange(query: String) {
        uiState = uiState.copy(query = query)
        fetchSearchSuggestions(query)
    }

    /**
     * Busca sugestões de produtos em segundo plano com base na consulta.
     */
    private fun fetchSearchSuggestions(query: String) {
        viewModelScope.launch {
            val allProducts = repository.getAllProducts()
            searchResults = allProducts
                .filter { it.idProduct.contains(query, true) || it.name.contains(query, true) }
                .map { "${it.idProduct} - ${it.name}" }
        }
    }

    /**
     * Lida com a seleção de um resultado de pesquisa.
     */
    fun onSearchResultClick(resultText: String) {
        val idProduct = resultText.substringBefore(" - ").trim()
        uiState = uiState.copy(query = idProduct)
        expanded = false
        searchProduct(query = idProduct)
    }

    /**
     * Busca um produto por código ou nome e escuta suas atualizações em tempo real.
     */
    fun searchProduct(query: String) {
        uiState = uiState.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            val product = getProductsByCodeOrName(query)
            if (product != null) {
                // Escuta as mudanças no produto para atualizar o estado da UI automaticamente
                listenProductById(product.idProduct).collect { updatedProduct ->
                    if (updatedProduct != null) {
                        uiState = uiState.copy(
                            selectedProduct = updatedProduct,
                            query = updatedProduct.idProduct,
                            isLoading = false
                        )
                    } else {
                        uiState = uiState.copy(errorMessage = "Produto não encontrado.", isLoading = false)
                    }
                }
            } else {
                uiState = uiState.copy(errorMessage = "Produto não encontrado.", isLoading = false)
            }
        }
    }

    /**
     * Aplica uma movimentação de estoque (entrada ou saída).
     */
    fun applyStockMovement(isEntrada: Boolean) {
        val product = uiState.selectedProduct ?: run {
            uiState = uiState.copy(errorMessage = "Nenhum produto selecionado.")
            return
        }
        val quantity = uiState.quantity.toIntOrNull() ?: run {
            uiState = uiState.copy(errorMessage = "Quantidade inválida.")
            return
        }

        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, errorMessage = null, successMessage = null)
            try {
                if (isEntrada) {
                    repository.addProductStock(product.idProduct, quantity)
                    repository.registerProductMovement(product.idProduct, quantity, "Entrada")
                } else {
                    repository.removeProductStock(product.idProduct, quantity)
                    repository.registerProductMovement(product.idProduct, quantity, "Saída")
                }
                uiState = uiState.copy(
                    successMessage = "Movimentação realizada com sucesso!",
                    isLoading = false,
                    quantity = ""
                )
            } catch (e: Exception) {
                uiState = uiState.copy(errorMessage = e.message, isLoading = false)
                Log.e("ProductStockViewModel", "Erro ao aplicar movimento de estoque", e)
            }
        }
    }
}
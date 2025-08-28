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
import com.example.meustock.domain.usecase.RegisterProductMovement
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
    private val listenProductById: ListenProductByIdUseCase,
    private val registerProductMovement : RegisterProductMovement,
    private val searchProducts: GetProductsByCodeOrNameUseCase
) : ViewModel() {

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
     * Busca sugestões no Firestore direto, sem trazer toda a coleção.
     */
    private fun fetchSearchSuggestions(query: String) {
        viewModelScope.launch {
            val products = repository.searchProducts(query)
            searchResults = products.map { "${it.idProduct} - ${it.name}" }
        }
    }

    fun onSearchResultClick(resultText: String) {
        val idProduct = resultText.substringBefore(" - ").trim()
        uiState = uiState.copy(query = idProduct)
        expanded = false
        searchProduct(idProduct)
    }

    /**
     * Busca produto e já mostra na UI (não espera listener).
     */
    fun searchProduct(query: String) {
        uiState = uiState.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch {
            val products = searchProducts(query)
            val product = products.firstOrNull()
            if (product != null) {
                // Mostra o produto imediatamente
                uiState = uiState.copy(
                    selectedProduct = product,
                    query = product.idProduct,
                    isLoading = false
                )

                // Depois escuta em tempo real
                listenProductById(product.idProduct).collect { updatedProduct ->
                    updatedProduct?.let {
                        uiState = uiState.copy(selectedProduct = it)
                    }
                }
            } else {
                uiState = uiState.copy(errorMessage = "Produto não encontrado.", isLoading = false)
            }
        }
    }

    /**
     * Aplica entrada ou saída em uma única transação.
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
                registerProductMovement(
                    product.idProduct,
                    quantity,
                    if (isEntrada) "Entrada" else "Saída"
                )

                uiState = uiState.copy(
                    successMessage = "Movimentação realizada com sucesso!",
                    isLoading = false,
                    quantity = ""
                )
            } catch (e: Exception) {
                uiState = uiState.copy(errorMessage = e.message, isLoading = false)
                Log.e("ProductStockVM", "Erro ao aplicar movimento", e)
            }
        }
    }
}

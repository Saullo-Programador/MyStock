package com.example.meustock.ui.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meustock.domain.repository.ProductMovementRepository
import com.example.meustock.domain.usecase.GetProductsByCodeOrNameUseCase
import com.example.meustock.ui.states.ProductStockUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductStockViewModel @Inject constructor(
    private val repository: ProductMovementRepository,
    private val getProductsByCodeOrName: GetProductsByCodeOrNameUseCase,

) : ViewModel() {

    var uiState by mutableStateOf(ProductStockUiState())
        private set
    var expanded by mutableStateOf(false)
        private set
    var searchResults by mutableStateOf<List<String>>(emptyList())
        private set
    fun onExpandedChange(exp: Boolean) {
        expanded = exp
    }

    fun onQuantityChange(quantity: String) {
        uiState = uiState.copy(quantity = quantity)
    }

    fun onQueryChange(query: String) {
        uiState = uiState.copy(query = query)
        fetchSearchSuggestions(query)
    }

    private fun fetchSearchSuggestions(query: String) {
        viewModelScope.launch {
            val allProducts = repository.getAllProducts() // Função que retorna todos os produtos
            searchResults = allProducts
                .filter { it.idProduct.contains(query, true) || it.name.contains(query, true) }
                .map { "${it.idProduct} - ${it.name}" }
        }
    }

    fun onSearchResultClick(resultText: String) {
        val productCode = resultText.substringBefore(" - ").trim()
        uiState = uiState.copy(query = productCode)
        expanded = false
        searchProduct(
            query = productCode
        )
    }


    fun searchProduct(query: String) {
        uiState = uiState.copy(query = query)
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, errorMessage = null)
            val product = getProductsByCodeOrName(query)
            if (product != null) {
                uiState = uiState.copy(selectedProduct = product, isLoading = false)
            } else {
                uiState = uiState.copy(errorMessage = "Produto não encontrado", isLoading = false)
            }
        }
    }

    fun applyStockMovement(isEntrada: Boolean) {
        val product = uiState.selectedProduct ?: return
        val quantity = uiState.quantity.toIntOrNull() ?: return

        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, errorMessage = null, successMessage = null)
            try {
                if (isEntrada) {
                    repository.addProductStock(product.id, quantity)
                } else {
                    repository.removeProductStock(product.id, quantity)
                }
                uiState = uiState.copy(
                    successMessage = "Movimentação realizada com sucesso",
                    isLoading = false,
                    quantity = ""
                )
                // Atualiza o produto
                searchProduct(
                    query = product.id
                )
            } catch (e: Exception) {
                uiState = uiState.copy(errorMessage = e.message, isLoading = false)
            }
        }
    }
}

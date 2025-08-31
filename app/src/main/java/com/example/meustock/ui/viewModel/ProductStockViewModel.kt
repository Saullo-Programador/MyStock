package com.example.meustock.ui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meustock.domain.repository.ProductMovementRepository
import com.example.meustock.domain.usecase.GetProductsByCodeOrNameUseCase
import com.example.meustock.domain.usecase.ListenProductByIdUseCase
import com.example.meustock.domain.usecase.RegisterProductMovement
import com.example.meustock.ui.states.ProductStockUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class WithdrawalScreenEvent {
    object Loading : WithdrawalScreenEvent()
    object Success : WithdrawalScreenEvent()
    data class Error(val message: String) : WithdrawalScreenEvent()
    object Idle : WithdrawalScreenEvent()
}

@HiltViewModel
class ProductStockViewModel @Inject constructor(
    private val repository: ProductMovementRepository,
    private val listenProductById: ListenProductByIdUseCase,
    private val registerProductMovement : RegisterProductMovement,
    private val searchProducts: GetProductsByCodeOrNameUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductStockUiState())
    val uiState: StateFlow<ProductStockUiState> = _uiState.asStateFlow()

    private val _event = MutableStateFlow<WithdrawalScreenEvent>(WithdrawalScreenEvent.Idle)
    val event: StateFlow<WithdrawalScreenEvent> = _event.asStateFlow()

    private val _searchResults = MutableStateFlow<List<String>>(emptyList())
    val searchResults: StateFlow<List<String>> = _searchResults.asStateFlow()

    var expanded = MutableStateFlow(false)
        private set

    fun onExpandedChange(exp: Boolean) { expanded.value = exp }
    fun onQuantityChange(quantity: String) { _uiState.update { it.copy(quantity = quantity) } }

    fun onQueryChange(query: String) {
        _uiState.update { it.copy(query = query) }
        fetchSearchSuggestions(query)
    }

    private fun fetchSearchSuggestions(query: String) {
        viewModelScope.launch {
            val products = repository.searchProducts(query)
            _searchResults.value = products.map { "${it.idProduct} - ${it.name}" }
        }
    }

    fun onSearchResultClick(resultText: String) {
        val idProduct = resultText.substringBefore(" - ").trim()
        _uiState.update { it.copy(query = idProduct) }
        expanded.value = false
        searchProduct(idProduct)
    }

    fun searchProduct(query: String) {
        _event.value = WithdrawalScreenEvent.Loading

        viewModelScope.launch {
            val products = searchProducts(query)
            val product = products.firstOrNull()
            if (product != null) {
                _uiState.update {
                    it.copy(
                        selectedProduct = product,
                        query = product.idProduct
                    )
                }
                _event.value = WithdrawalScreenEvent.Idle

                // escuta atualizações em tempo real
                listenProductById(product.idProduct).collect { updatedProduct ->
                    updatedProduct?.let { p ->
                        _uiState.update { it.copy(selectedProduct = p) }
                    }
                }
            } else {
                _event.value = WithdrawalScreenEvent.Error("Produto não encontrado.")
            }
        }
    }

    fun applyStockMovement(isEntrada: Boolean) {
        val product = _uiState.value.selectedProduct ?: run {
            _event.value = WithdrawalScreenEvent.Error("Nenhum produto selecionado.")
            return
        }
        val quantity = _uiState.value.quantity.toIntOrNull() ?: run {
            _event.value = WithdrawalScreenEvent.Error("Quantidade inválida.")
            return
        }

        viewModelScope.launch {
            _event.value = WithdrawalScreenEvent.Loading
            try {
                registerProductMovement(
                    product.idProduct,
                    quantity,
                    if (isEntrada) "Entrada" else "Saída"
                )

                _uiState.update { it.copy(quantity = "") }
                _event.value = WithdrawalScreenEvent.Success
            } catch (e: Exception) {
                _event.value = WithdrawalScreenEvent.Error(e.message ?: "Erro desconhecido")
                Log.e("ProductStockVM", "Erro ao aplicar movimento", e)
            }
        }
    }

    fun resetEvent() {
        _event.value = WithdrawalScreenEvent.Idle
    }
}

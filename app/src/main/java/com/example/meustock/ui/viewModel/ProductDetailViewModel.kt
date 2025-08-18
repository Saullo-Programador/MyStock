package com.example.meustock.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meustock.domain.model.Product
import com.example.meustock.domain.usecase.DetailProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para a tela de detalhes de um produto.
 * Escuta em tempo real as mudanças em um único produto.
 */
@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val detailProductUseCase: DetailProductUseCase,
) : ViewModel() {

    private val _productState = MutableStateFlow<Product?>(null)
    val productState: StateFlow<Product?> = _productState.asStateFlow()

    /**
     * Carrega e escuta em tempo real os detalhes de um produto pelo seu ID.
     */
    fun loadProduct(productId: String) {
        viewModelScope.launch {
            detailProductUseCase(productId).collect { product ->
                _productState.value = product
            }
        }
    }
}
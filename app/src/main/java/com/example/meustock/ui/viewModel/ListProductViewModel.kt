package com.example.meustock.ui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meustock.domain.model.Product
import com.example.meustock.domain.usecase.GetProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListProductViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase
): ViewModel() {
    private val _product = MutableStateFlow<List<Product>>(emptyList())
    val product: StateFlow<List<Product>> = _product.asStateFlow()

    init {
        fetchProducts()
    }

    private fun fetchProducts() {
        viewModelScope.launch {
            try {
                getProductsUseCase().collect { products ->
                    Log.d("ListProductViewModel", "Produtos recebidos: $products")
                    _product.value = products
                }
            } catch (e: Exception) {
                Log.e("ListProductViewModel", "Erro ao buscar produtos: ${e.message}", e)
            }
        }
    }


}
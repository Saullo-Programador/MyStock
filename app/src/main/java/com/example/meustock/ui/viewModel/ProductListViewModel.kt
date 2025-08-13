package com.example.meustock.ui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meustock.domain.model.Product
import com.example.meustock.domain.usecase.DeleteProductUseCase
import com.example.meustock.domain.usecase.GetProductsUseCase
import com.example.meustock.domain.usecase.UpdateProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    private val deleteProductUseCase: DeleteProductUseCase,
    private val updateProductUseCase: UpdateProductUseCase
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


    fun deleteProduct(productId: String) {
        viewModelScope.launch {
            try {
                deleteProductUseCase.invoke(product = product.value.first { it.idProduct == productId })
            } catch (e: Exception) {
                Log.e("ListProductViewModel", "Erro ao deletar produto: ${e.message}", e)
            }
        }
    }

}
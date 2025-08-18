package com.example.meustock.ui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meustock.domain.model.Product
import com.example.meustock.domain.usecase.DeleteProductUseCase
import com.example.meustock.domain.usecase.GetProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para a tela de listagem de produtos.
 * Gerencia a busca e a deleção de produtos.
 */
@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    private val deleteProductUseCase: DeleteProductUseCase,
) : ViewModel() {

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    init {
        fetchProducts()
    }

    /**
     * Busca todos os produtos e atualiza o estado da UI.
     * Escuta em tempo real o flow de produtos do repositório.
     */
    private fun fetchProducts() {
        viewModelScope.launch {
            try {
                getProductsUseCase().collect { products ->
                    Log.d("ProductListViewModel", "Produtos recebidos: ${products.size}")
                    _products.value = products
                }
            } catch (e: Exception) {
                Log.e("ProductListViewModel", "Erro ao buscar produtos", e)
            }
        }
    }

    /**
     * Deleta um produto com base no seu ID.
     */
    fun deleteProduct(productId: String) {
        viewModelScope.launch {
            try {
                val productToDelete = _products.value.firstOrNull { it.idProduct == productId }
                if (productToDelete != null) {
                    deleteProductUseCase(productToDelete)
                } else {
                    Log.w("ProductListViewModel", "Produto com ID $productId não encontrado para exclusão.")
                }
            } catch (e: Exception) {
                Log.e("ProductListViewModel", "Erro ao deletar produto: ${e.message}", e)
            }
        }
    }
}
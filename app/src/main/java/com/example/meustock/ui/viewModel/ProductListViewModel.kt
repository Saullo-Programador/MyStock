package com.example.meustock.ui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meustock.domain.model.Product
import com.example.meustock.domain.usecase.DeleteProductUseCase
import com.example.meustock.domain.usecase.GetProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
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

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    // Lista filtrada baseada no texto digitado
    val filteredProducts: StateFlow<List<Product>> =
        combine(_products, _query) { products, query ->
            if (query.isBlank()) {
                products
            } else {
                products.filter { product ->
                    product.name.contains(query, ignoreCase = true) ||
                            product.idProduct.contains(query, ignoreCase = true)
                }
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        fetchProducts()
    }

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

    // Atualiza a query conforme o usuário digita
    fun updateQuery(newQuery: String) {
        _query.value = newQuery
    }
}

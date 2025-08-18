package com.example.meustock.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meustock.domain.repository.ProductRepository
import com.example.meustock.ui.states.DashboardUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel(){
    private val _state = MutableStateFlow(DashboardUiState())
    val state: StateFlow<DashboardUiState> = _state.asStateFlow()

    init {
        loadDashboard()
    }

    private fun loadDashboard(){
        viewModelScope.launch {
            productRepository.getProducts().collect { products ->
                val totalProducts = products.size
                val totalItemsInStocks = products.sumOf { it.currentStock }
                val totalStockValue = products.sumOf { it.costPrice * it.currentStock }
                val lowStockItems = products.count { it.currentStock < it.minimumStock }

                val restockProducts = products.filter { it.currentStock <= it.minimumStock }

                _state.value = DashboardUiState(
                    totalProducts = totalProducts,
                    totalItemsInStock = totalItemsInStocks,
                    totalStockValue = totalStockValue,
                    lowStockItems = lowStockItems,
                    restockProducts = restockProducts
                )
            }
        }
    }
}
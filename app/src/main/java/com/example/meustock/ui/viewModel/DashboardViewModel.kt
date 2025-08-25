package com.example.meustock.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meustock.domain.model.Product
import com.example.meustock.domain.repository.ProductMovementRepository
import com.example.meustock.domain.repository.ProductRepository
import com.example.meustock.ui.states.DashboardUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * ViewModel responsável pela lógica da tela de Dashboard.
 * Coleta e combina dados de múltiplos repositórios para apresentar um resumo do estoque.
 */
@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val productMovementRepository: ProductMovementRepository
) : ViewModel() {

    private val _state = MutableStateFlow(DashboardUiState())
    val state: StateFlow<DashboardUiState> = _state.asStateFlow()

    init {
        loadDashboardData()
    }

    private fun loadDashboardData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }

            try {
                val productsFlow = productRepository.getProducts()
                val recentMovementsFlow = productMovementRepository.listenRecentMovements(limit = 5)

                combine(
                    productsFlow,
                    recentMovementsFlow,
                ) { products, movements ->



                    val totalProducts = products.size
                    val totalItemsInStock = products.sumOf { it.currentStock }
                    val totalStockValue = products.sumOf { it.costPrice * it.currentStock }
                    val lowStockItems = products.count { it.currentStock < it.minimumStock }
                    val restockProducts = products.filter { it.currentStock <= it.minimumStock }

                    DashboardUiState(
                        isLoading = false,
                        errorMessage = null,
                        totalProducts = totalProducts,
                        totalItemsInStock = totalItemsInStock,
                        totalStockValue = totalStockValue,
                        lowStockItems = lowStockItems,
                        restockProducts = restockProducts,
                        lastMovements = movements,
                    )
                }.collect { uiState ->
                    _state.value = uiState
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }
}

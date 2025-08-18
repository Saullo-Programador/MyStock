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

    /**
     * Carrega todos os dados do dashboard de forma reativa.
     * Combina os flows de produtos, movimentos recentes e top de vendas
     * para atualizar a UI de forma eficiente.
     */
    private fun loadDashboardData() {
        viewModelScope.launch {
            // Flow de produtos para calcular estatísticas
            val productsFlow = productRepository.getProducts()

            // Flow para os movimentos recentes
            val recentMovementsFlow = productMovementRepository.listenRecentMovements(limit = 5)

            // Flow para os produtos mais vendidos (usa `first()` para buscar apenas uma vez)
            val topSellingProductsFlow = productMovementRepository.getTopSellingProducts(limit = 5)

            // Combina os flows para garantir que o estado da UI seja atualizado com todos os dados
            combine(
                productsFlow,
                recentMovementsFlow
            ) { products, movements ->
                // O cálculo do top de vendas é feito apenas uma vez, então o valor é recuperado
                val topProducts = topSellingProductsFlow

                val totalProducts = products.size
                val totalItemsInStock = products.sumOf { it.currentStock }
                val totalStockValue = products.sumOf { it.costPrice * it.currentStock }
                val lowStockItems = products.count { it.currentStock < it.minimumStock }
                val restockProducts = products.filter { it.currentStock <= it.minimumStock }

                _state.value = _state.value.copy(
                    totalProducts = totalProducts,
                    totalItemsInStock = totalItemsInStock,
                    totalStockValue = totalStockValue,
                    lowStockItems = lowStockItems,
                    restockProducts = restockProducts,
                    lastMovements = movements,
                    topSellingProducts = topProducts
                )
            }.collect {
                // `collect` mantém o flow ativo e atualiza o estado
            }
        }
    }
}
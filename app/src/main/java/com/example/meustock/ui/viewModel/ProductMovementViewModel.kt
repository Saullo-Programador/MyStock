package com.example.meustock.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meustock.domain.model.ProductMovement
import com.example.meustock.domain.repository.ProductMovementRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para a tela de histórico de movimentos de um produto.
 * Escuta em tempo real os movimentos de um produto específico.
 */
@HiltViewModel
class ProductMovementViewModel @Inject constructor(
    private val repository: ProductMovementRepository
) : ViewModel() {

    private val _movements = MutableStateFlow<List<ProductMovement>>(emptyList())
    val movements: StateFlow<List<ProductMovement>> = _movements

    /**
     * Carrega os movimentos de um produto com base no seu ID.
     */
    fun loadMovements(productId: String) {
        viewModelScope.launch {
            repository.getProductMovements(productId)
                .collect { _movements.value = it }
        }
    }
}
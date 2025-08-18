package com.example.meustock.ui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meustock.domain.model.Product
import com.example.meustock.domain.usecase.GetNextProductCodeUseCase
import com.example.meustock.domain.usecase.SaveProductUseCase
import com.example.meustock.ui.states.ProductUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Eventos do formulário de registro de produto.
 */
sealed class ProductFormEvent {
    object Loading : ProductFormEvent()
    object Success : ProductFormEvent()
    data class Error(val message: String) : ProductFormEvent()
    object Idle : ProductFormEvent()
}

/**
 * ViewModel para a tela de registro de um novo produto.
 * Gerencia o estado do formulário e a lógica de salvamento.
 */
@HiltViewModel
class RegisterProductFormViewModel @Inject constructor(
    private val saveProductUseCase: SaveProductUseCase,
    private val getNextProductCodeUseCase: GetNextProductCodeUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductUiState())
    val uiState: StateFlow<ProductUiState> = _uiState.asStateFlow()

    private val _productFormEvent = MutableStateFlow<ProductFormEvent>(ProductFormEvent.Idle)
    val productFormEvent: StateFlow<ProductFormEvent> = _productFormEvent.asStateFlow()

    /**
     * Funções de atualização dos campos do formulário.
     * Usam `_uiState.update` para garantir que o estado seja atualizado de forma segura.
     */
    fun updateNameProduct(name: String) { _uiState.update { it.copy(nameProduct = name) } }
    fun updateDescription(description: String) { _uiState.update { it.copy(description = description) } }
    fun updateBarcodeSku(barcode: String) { _uiState.update { it.copy(barcodeSku = barcode) } }
    fun updateCostPrice(price: String) { _uiState.update { it.copy(costPrice = price) } }
    fun updateSellingPrice(price: String) { _uiState.update { it.copy(sellingPrice = price) } }
    fun updateCurrentStock(stock: String) { _uiState.update { it.copy(currentStock = stock) } }
    fun updateMinimumStock(stock: String) { _uiState.update { it.copy(minimumStock = stock) } }
    fun updateCategory(category: String) { _uiState.update { it.copy(category = category) } }
    fun updateBrand(brand: String) { _uiState.update { it.copy(brand = brand) } }
    fun updateUnitOfMeasurement(unit: String) { _uiState.update { it.copy(unitOfMeasurement = unit) } }
    fun updateSupplier(supplier: String) { _uiState.update { it.copy(supplier = supplier) } }
    fun updateStockLocation(location: String) { _uiState.update { it.copy(stockLocation = location) } }
    fun updateStatus(status: String) { _uiState.update { it.copy(status = status) } }
    fun updateNotes(notes: String) { _uiState.update { it.copy(notes = notes) } }

    /**
     * Valida os campos do formulário e salva o novo produto.
     */
    fun saveProduct() {
        viewModelScope.launch {
            val state = _uiState.value

            if (!isFormValid(state)) {
                _productFormEvent.value = ProductFormEvent.Error("Preencha todos os campos obrigatórios.")
                return@launch
            }

            _productFormEvent.value = ProductFormEvent.Loading

            try {
                val nextProductCode = getNextProductCodeUseCase()
                val productToSave = createProductFromUiState(state, nextProductCode)
                saveProductUseCase(productToSave)

                _productFormEvent.value = ProductFormEvent.Success
                _uiState.update { ProductUiState() } // Limpa o formulário
            } catch (e: Exception) {
                _productFormEvent.value = ProductFormEvent.Error("Erro ao salvar o produto: ${e.message}")
                Log.e("RegisterProductViewModel", "Erro ao salvar o produto", e)
            }
        }
    }

    /**
     * Valida os campos obrigatórios do formulário.
     */
    private fun isFormValid(state: ProductUiState): Boolean {
        return state.nameProduct.isNotBlank() &&
                state.costPrice.toDoubleOrNull() != null &&
                state.sellingPrice.toDoubleOrNull() != null &&
                state.currentStock.toIntOrNull() != null &&
                state.minimumStock.toIntOrNull() != null &&
                state.category.isNotBlank() &&
                state.unitOfMeasurement.isNotBlank()
    }

    /**
     * Mapeia o estado da UI para um objeto de domínio `Product`.
     */
    private fun createProductFromUiState(state: ProductUiState, id: String): Product {
        return Product(
            idProduct = id,
            name = state.nameProduct,
            description = state.description.takeIf { it?.isNotBlank() == true },
            barcodeSku = state.barcodeSku.takeIf { it?.isNotBlank() == true },
            costPrice = state.costPrice.toDoubleOrNull() ?: 0.0,
            sellingPrice = state.sellingPrice.toDoubleOrNull() ?: 0.0,
            currentStock = state.currentStock.toIntOrNull() ?: 0,
            minimumStock = state.minimumStock.toIntOrNull() ?: 0,
            category = state.category,
            brand = state.brand.takeIf { it?.isNotBlank() == true },
            unitOfMeasurement = state.unitOfMeasurement,
            supplier = state.supplier.takeIf { it?.isNotBlank() == true },
            stockLocation = state.stockLocation.takeIf { it?.isNotBlank() == true },
            status = state.status,
            notes = state.notes.takeIf { it?.isNotBlank() == true },
            registrationDate = state.registrationDate,
            lastUpdateDate = System.currentTimeMillis()
        )
    }

    /**
     * Reinicia o estado do evento do formulário.
     */
    fun resetProductFormEvent() {
        _productFormEvent.value = ProductFormEvent.Idle
    }
}
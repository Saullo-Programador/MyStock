package com.example.meustock.ui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meustock.domain.model.Product
import com.example.meustock.domain.usecase.SaveProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import com.example.meustock.ui.states.ProductUiState // Importe seu ProductUiState
import kotlinx.coroutines.launch


sealed class ProductFormEvent {
    object Loading : ProductFormEvent()
    object Success : ProductFormEvent()
    data class Error(val message: String) : ProductFormEvent()
    object Idle : ProductFormEvent() // Estado inicial ou reset
}
@HiltViewModel
class RegisterProductFormViewModel @Inject constructor(
    private val saveProductUseCase: SaveProductUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow(ProductUiState())
    val uiState: StateFlow<ProductUiState> = _uiState.asStateFlow()

    private val _productFormEvent = MutableStateFlow<ProductFormEvent>(ProductFormEvent.Idle)
    val productFormEvent: StateFlow<ProductFormEvent> = _productFormEvent.asStateFlow()

    // Funções de atualização dos campos (mantidas do seu código anterior)
    fun updateNameProduct(name: String) { _uiState.update { it.copy(nameProduct = name) } }
    fun updateDescription(description: String) { _uiState.update { it.copy(description = description) } }
    fun updateBarcodeSku(barcode: String) { _uiState.update { it.copy(barcodeSku = barcode) } }
    fun updateCostPrice(price: String) { _uiState.update { it.copy(costPrice = price.toDoubleOrNull() ?: 0.0) } }
    fun updateSellingPrice(price: String) { _uiState.update { it.copy(sellingPrice = price.toDoubleOrNull() ?: 0.0) } }
    fun updateCurrentStock(stock: String) { _uiState.update { it.copy(currentStock = stock.toIntOrNull() ?: 0) } }
    fun updateMinimumStock(stock: String) { _uiState.update { it.copy(minimumStock = stock.toIntOrNull() ?: 0) } }
    fun updateCategory(category: String) { _uiState.update { it.copy(category = category) } }
    fun updateBrand(brand: String) { _uiState.update { it.copy(brand = brand) } }
    fun updateUnitOfMeasurement(unit: String) { _uiState.update { it.copy(unitOfMeasurement = unit) } }
    fun updateSupplier(supplier: String) { _uiState.update { it.copy(supplier = supplier) } }
    fun updateStockLocation(location: String) { _uiState.update { it.copy(stockLocation = location) } }
    fun updateStatus(status: String) { _uiState.update { it.copy(status = status) } }
    fun updateNotes(notes: String) { _uiState.update { it.copy(notes = notes) } }


    // Função para salvar o produto (agora aceita um ProductUiState)
    fun saveProduct() {
        val currentProductUiState = _uiState.value

        // Validação (exemplo - refine conforme sua necessidade)
        if (currentProductUiState.nameProduct.isBlank() ||
            currentProductUiState.costPrice < 0.0 ||
            currentProductUiState.sellingPrice < 0.0 ||
            currentProductUiState.currentStock < 0 ||
            currentProductUiState.minimumStock < 0 ||
            currentProductUiState.category.isBlank() ||
            currentProductUiState.unitOfMeasurement.isBlank()
        ) {
            _productFormEvent.value = ProductFormEvent.Error("Campos obrigatórios não preenchidos ou inválidos.")
            Log.e("RegisterProductViewModel", "Erro de validação: Campos obrigatórios não preenchidos ou inválidos.")
            // Você pode expor um estado de erro para a UI aqui
            return
        }

        // Converte os Strings para os tipos finais antes de persistir (se necessário)
        val productToSave = Product(
            id = currentProductUiState.id,
            name = currentProductUiState.nameProduct,
            description = currentProductUiState.description.takeIf { it?.isNotBlank() ?: false },
            barcodeSku = currentProductUiState.barcodeSku.takeIf { it?.isNotBlank() ?: false },
            costPrice = currentProductUiState.costPrice,
            sellingPrice = currentProductUiState.sellingPrice,
            currentStock = currentProductUiState.currentStock,
            minimumStock = currentProductUiState.minimumStock,
            category = currentProductUiState.category,
            brand = currentProductUiState.brand.takeIf { it?.isNotBlank() ?: false },
            unitOfMeasurement = currentProductUiState.unitOfMeasurement,
            supplier = currentProductUiState.supplier.takeIf { it?.isNotBlank() ?: false },
            stockLocation = currentProductUiState.stockLocation.takeIf { it?.isNotBlank() ?: false },
            status = currentProductUiState.status,
            notes = currentProductUiState.notes.takeIf { it?.isNotBlank() ?: false },
            registrationDate = currentProductUiState.registrationDate,
            lastUpdateDate = currentProductUiState.lastUpdateDate
        )
        viewModelScope.launch {
            _productFormEvent.value = ProductFormEvent.Loading
            try {
                saveProductUseCase(productToSave)
                _productFormEvent.value = ProductFormEvent.Success
                _uiState.update { ProductUiState() } // Reseta o formulário principal
                Log.d("RegisterProductViewModel", "Produto salvo com sucesso: $productToSave")
            } catch (e: Exception) {
                _productFormEvent.value = ProductFormEvent.Error("Erro ao salvar o produto: ${e.message}")
                Log.e("RegisterProductViewModel", "Erro ao salvar o produto: ${e.message}", e)
            }
        }
    }
    fun resetProductFormEvent() {
        _productFormEvent.value = ProductFormEvent.Idle
    }
}
package com.example.meustock.ui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meustock.domain.model.Product
import com.example.meustock.domain.usecase.GetNextProductCodeUseCase
import com.example.meustock.domain.usecase.SaveProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import com.example.meustock.ui.states.ProductUiState
import kotlinx.coroutines.launch


sealed class ProductFormEvent {
    object Loading : ProductFormEvent()
    object Success : ProductFormEvent()
    data class Error(val message: String) : ProductFormEvent()
    object Idle : ProductFormEvent() // Estado inicial ou reset
}
@HiltViewModel
class RegisterProductFormViewModel @Inject constructor(
    private val saveProductUseCase: SaveProductUseCase,
    private val getNextProductCodeUseCase: GetNextProductCodeUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow(ProductUiState())
    val uiState: StateFlow<ProductUiState> = _uiState.asStateFlow()

    private val _productFormEvent = MutableStateFlow<ProductFormEvent>(ProductFormEvent.Idle)
    val productFormEvent: StateFlow<ProductFormEvent> = _productFormEvent.asStateFlow()

    // Funções de atualização dos campos
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


    // Função para salvar o produto
    fun saveProduct() {
        viewModelScope.launch {
            val currentProductUiState = _uiState.value

            if (currentProductUiState.nameProduct.isBlank() ||
                currentProductUiState.costPrice.isBlank() ||
                currentProductUiState.sellingPrice.isBlank() ||
                currentProductUiState.currentStock.isBlank() ||
                currentProductUiState.minimumStock.isBlank() ||
                currentProductUiState.category.isBlank() ||
                currentProductUiState.unitOfMeasurement.isBlank()
            ) {
                _productFormEvent.value = ProductFormEvent.Error("Campos obrigatórios não preenchidos ou inválidos.")
                return@launch
            }

            _productFormEvent.value = ProductFormEvent.Loading

            try {
                val nextProductCode = getNextProductCodeUseCase()

                val productToSave = Product(
                    id = currentProductUiState.id,
                    idProduct = nextProductCode,
                    name = currentProductUiState.nameProduct,
                    description = currentProductUiState.description.takeIf { it?.isNotBlank() ?: false },
                    barcodeSku = currentProductUiState.barcodeSku.takeIf { it?.isNotBlank() ?: false },
                    costPrice = currentProductUiState.costPrice.toDoubleOrNull() ?: 0.0,
                    sellingPrice = currentProductUiState.sellingPrice.toDoubleOrNull() ?: 0.0,
                    currentStock = currentProductUiState.currentStock.toIntOrNull() ?: 0,
                    minimumStock = currentProductUiState.minimumStock.toIntOrNull() ?: 0,
                    category = currentProductUiState.category,
                    brand = currentProductUiState.brand.takeIf { it?.isNotBlank() ?: false },
                    unitOfMeasurement = currentProductUiState.unitOfMeasurement,
                    supplier = currentProductUiState.supplier.takeIf { it?.isNotBlank() ?: false },
                    stockLocation = currentProductUiState.stockLocation.takeIf { it?.isNotBlank() ?: false },
                    status = currentProductUiState.status,
                    notes = currentProductUiState.notes.takeIf { it?.isNotBlank() ?: false },
                    registrationDate = currentProductUiState.registrationDate,
                    lastUpdateDate = System.currentTimeMillis() // atualiza o timestamp
                )

                saveProductUseCase(productToSave)
                _productFormEvent.value = ProductFormEvent.Success
                _uiState.update { ProductUiState() } // Limpa formulário
            } catch (e: Exception) {
                _productFormEvent.value = ProductFormEvent.Error("Erro ao salvar o produto: ${e.message}")
                Log.e("RegisterProductViewModel", "Erro ao salvar o produto", e)
            }
        }
    }

    fun resetProductFormEvent() {
        _productFormEvent.value = ProductFormEvent.Idle
    }
}
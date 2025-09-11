package com.example.meustock.ui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meustock.domain.model.Product
import com.example.meustock.domain.usecase.GetProductByIdUseCase
import com.example.meustock.domain.usecase.UpdateProductUseCase
import com.example.meustock.ui.states.ProductEditUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Eventos para a UI durante a edição de um produto.
 */
sealed class ProductEditFormEvent {
    object Loading : ProductEditFormEvent()
    object Success : ProductEditFormEvent()
    data class Error(val message: String) : ProductEditFormEvent()
    object Idle : ProductEditFormEvent()
}

/**
 * ViewModel para a tela de edição de um produto.
 * Gerencia o estado do formulário e as operações de atualização.
 */
@HiltViewModel
class ProductEditViewModel @Inject constructor(
    private val updateProductUseCase: UpdateProductUseCase,
    private val getProductByIdUseCase: GetProductByIdUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductEditUiState())
    val uiState: StateFlow<ProductEditUiState> = _uiState.asStateFlow()

    private val _productFormEvent = MutableStateFlow<ProductEditFormEvent>(ProductEditFormEvent.Idle)
    val productFormEvent: StateFlow<ProductEditFormEvent> = _productFormEvent.asStateFlow()


    init {
        _uiState.update { state ->
            state.copy(
                onNameProductChange = { newName ->
                    _uiState.value = _uiState.value.copy(nameProduct = newName)
                },
                onDescriptionChange = { newDescription ->
                    _uiState.value = _uiState.value.copy(description = newDescription)
                },
                onBarcodeSkuChange = { newBarcodeSku ->
                    _uiState.value = _uiState.value.copy(barcodeSku = newBarcodeSku)
                },
                onCostPriceChange = { newCostPrice ->
                    _uiState.value = _uiState.value.copy(costPrice = newCostPrice)
                },
                onSellingPriceChange = { newSellingPrice ->
                    _uiState.value = _uiState.value.copy(sellingPrice = newSellingPrice)
                },
                onCurrentStockChange = { newCurrentStock ->
                    _uiState.value = _uiState.value.copy(currentStock = newCurrentStock)
                },
                onMinimumStockChange = { newMinimumStock ->
                    _uiState.value = _uiState.value.copy(minimumStock = newMinimumStock)
                },
                onCategoryChange = { newCategory ->
                    _uiState.value = _uiState.value.copy(category = newCategory)
                },
                onBrandChange = { newBrand ->
                    _uiState.value = _uiState.value.copy(brand = newBrand)
                },
                onUnitOfMeasurementChange = { newUnitOfMeasurement ->
                    _uiState.value = _uiState.value.copy(unitOfMeasurement = newUnitOfMeasurement)
                },
                onSupplierChange = { newSupplier ->
                    _uiState.value = _uiState.value.copy(supplier = newSupplier)
                },
                onStockLocationChange = { newStockLocation ->
                    _uiState.value = _uiState.value.copy(stockLocation = newStockLocation)
                },
                onStatusChange = { newStatus ->
                    _uiState.value = _uiState.value.copy(status = newStatus)
                },
                onNotesChange = { newNotes ->
                    _uiState.value = _uiState.value.copy(notes = newNotes)
                }

            )
        }
    }

    /**
     * Inicia a edição de um produto com base no seu ID.
     * Carrega os dados do produto e preenche o estado da UI.
     */
    fun loadProduct(productId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val product = getProductByIdUseCase(productId)
                product?.let {
                    _uiState.update { state ->
                        state.copy(
                            // Mapeia o modelo de domínio para o estado da UI
                            idProduct = it.idProduct,
                            registrationDate = it.registrationDate,
                            lastUpdateDate = it.lastUpdateDate,
                            imageUrl = it.imageUrl,
                            nameProduct = it.name,
                            description = it.description,
                            barcodeSku = it.barcodeSku,
                            costPrice = it.costPrice.toString(),
                            sellingPrice = it.sellingPrice.toString(),
                            currentStock = it.currentStock.toString(),
                            minimumStock = it.minimumStock.toString(),
                            category = it.category,
                            brand = it.brand,
                            unitOfMeasurement = it.unitOfMeasurement,
                            supplier = it.supplier,
                            stockLocation = it.stockLocation,
                            status = it.status,
                            notes = it.notes,
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                _productFormEvent.value = ProductEditFormEvent.Error("Erro ao carregar o produto: ${e.message}")
                Log.e("ProductEditViewModel", "Erro ao carregar o produto", e)
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    /**
     * Salva as alterações de um produto.
     * Mapeia o estado da UI de volta para o modelo de domínio e chama o caso de uso.
     */
    fun editProduct() {
        viewModelScope.launch {
            _productFormEvent.value = ProductEditFormEvent.Loading
            try {
                val state = _uiState.value
                val productToUpdate = Product(
                    idProduct = state.idProduct,
                    name = state.nameProduct,
                    description = state.description,
                    barcodeSku = state.barcodeSku,
                    costPrice = state.costPrice.toDoubleOrNull() ?: 0.0,
                    sellingPrice = state.sellingPrice.toDoubleOrNull() ?: 0.0,
                    currentStock = state.currentStock.toIntOrNull() ?: 0,
                    minimumStock = state.minimumStock.toIntOrNull() ?: 0,
                    category = state.category,
                    brand = state.brand,
                    unitOfMeasurement = state.unitOfMeasurement,
                    supplier = state.supplier,
                    stockLocation = state.stockLocation,
                    status = state.status,
                    notes = state.notes,
                    registrationDate = state.registrationDate,
                    lastUpdateDate = System.currentTimeMillis(),
                    imageUrl = state.imageUrl,
                    createdBy = state.createdBy
                )
                updateProductUseCase(productToUpdate)
                _productFormEvent.value = ProductEditFormEvent.Success
                _uiState.update { ProductEditUiState() } // Limpa o estado após o sucesso
            } catch (e: Exception) {
                Log.e("ProductEditViewModel", "Erro ao editar o produto: ${e.message}", e)
                _productFormEvent.value = ProductEditFormEvent.Error("Erro ao editar o produto: ${e.message}")
            }
        }
    }

    /**
     * Reinicia o estado do evento do formulário.
     */
    fun resetProductFormEvent() {
        _productFormEvent.value = ProductEditFormEvent.Idle
    }
}
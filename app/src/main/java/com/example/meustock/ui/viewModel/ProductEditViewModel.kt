package com.example.meustock.ui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meustock.domain.model.Product
import com.example.meustock.domain.usecase.GetProductByIdUseCase
import com.example.meustock.domain.usecase.UpdateProductUseCase
import com.example.meustock.ui.states.ProductEditUiState
import com.example.meustock.ui.states.ProductFormEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed class ProductEditFormEvent {
    object Loading : ProductEditFormEvent()
    object Success : ProductEditFormEvent()
    data class Error(val message: String) : ProductEditFormEvent()
    object Idle : ProductEditFormEvent() // Estado inicial ou reset
}
@HiltViewModel
class ProductEditViewModel @Inject constructor(
    private val updateProductUseCase: UpdateProductUseCase,
    private val getProductByIdUseCase: GetProductByIdUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProductEditUiState())
    val uiState: StateFlow<ProductEditUiState> = _uiState.asStateFlow()

    private val _productFormEvent = MutableStateFlow<ProductEditFormEvent>(ProductEditFormEvent.Idle)
    val productFormEvent: StateFlow<ProductEditFormEvent> = _productFormEvent.asStateFlow()

    fun editProduct(){
        val state = _uiState.value
        val productEdit = Product(
            idProduct = state.idProduct,
            name = state.nameProduct,
            description = state.description,
            barcodeSku = state.barcodeSku,
            costPrice = state.costPrice.toDouble(),
            sellingPrice = state.sellingPrice.toDouble(),
            currentStock = state.currentStock.toInt(),
            minimumStock = state.minimumStock.toInt(),
            category = state.category,
            brand = state.brand,
            unitOfMeasurement = state.unitOfMeasurement,
            supplier = state.supplier,
            stockLocation = state.stockLocation,
            status = state.status,
            notes = state.notes,
            registrationDate = state.registrationDate,
            lastUpdateDate = state.lastUpdateDate,
            imageUrl = state.imageUrl,
        )
        viewModelScope.launch{
            _productFormEvent.value = ProductEditFormEvent.Loading
            try {
                updateProductUseCase(productEdit)
                _productFormEvent.value = ProductEditFormEvent.Success
                _uiState.update { ProductEditUiState() }
            }catch (e: Exception){
                Log.e("ProductEditViewModel","erro ao editar o produto: ${e.message}", e)
                _productFormEvent.value = ProductEditFormEvent.Error("Erro ao editar o produto: ${e.message}")
            }
        }
    }

    fun loadProduct(productId: String){
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val product = getProductByIdUseCase(productId)


                product?.let {
                    _uiState.update { state ->
                        state.copy(
                            idProduct = it.idProduct,
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
                            registrationDate = it.registrationDate,
                            lastUpdateDate = it.lastUpdateDate,
                            imageUrl = it.imageUrl,
                            onRegistrationDateChange = { newValue ->
                                _uiState.update { state ->
                                    state.copy(registrationDate = newValue)
                                }
                            },
                            onLastUpdateDateChange = { newValue ->
                                _uiState.update { state ->
                                    state.copy(lastUpdateDate = newValue)
                                }
                            },
                            onImageUrlChange = { newValue ->
                                _uiState.update { state ->
                                    state.copy(imageUrl = newValue)
                                }
                            },
                            onNameProductChange = { newValue ->
                                _uiState.update { state ->
                                    state.copy(nameProduct = newValue)
                                }
                            },
                            onDescriptionChange = { newValue ->
                                _uiState.update { state ->
                                    state.copy(description = newValue)
                                }
                            },
                            onBarcodeSkuChange = { newValue ->
                                _uiState.update { state ->
                                    state.copy(barcodeSku = newValue)
                                }
                            },
                            onCostPriceChange = { newValue ->
                                _uiState.update { state ->
                                    state.copy(costPrice = newValue)
                                }
                            },
                            onSellingPriceChange = { newValue ->
                                _uiState.update { state ->
                                    state.copy(sellingPrice = newValue)
                                }
                            },
                            onCurrentStockChange = { newValue ->
                                _uiState.update { state ->
                                    state.copy(currentStock = newValue)
                                }
                            },
                            onMinimumStockChange = { newValue ->
                                _uiState.update { state ->
                                    state.copy(minimumStock = newValue)
                                }
                            },
                            onCategoryChange = { newValue ->
                                _uiState.update { state ->
                                    state.copy(category = newValue)
                                }
                            },
                            onBrandChange = { newValue ->
                                _uiState.update { state ->
                                    state.copy(brand = newValue)
                                }
                            },
                            onUnitOfMeasurementChange = { newValue ->
                                _uiState.update { state ->
                                    state.copy(unitOfMeasurement = newValue)
                                }
                            },
                            onSupplierChange = { newValue ->
                                _uiState.update { state ->
                                    state.copy(supplier = newValue)
                                }
                            },
                            onStockLocationChange = { newValue ->
                                _uiState.update { state ->
                                    state.copy(stockLocation = newValue)
                                }
                            },
                            onStatusChange = { newValue ->
                                _uiState.update { state ->
                                    state.copy(status = newValue)
                                }
                            },
                            onNotesChange = { newValue ->
                                _uiState.update { state ->
                                    state.copy(notes = newValue)
                                }
                            },
                            isLoading = false
                        )
                    }
                }

            }catch (e: Exception) {
                _productFormEvent.value = ProductEditFormEvent.Error("Erro ao carregar o produto: ${e.message}")
                Log.e("ProductEditViewModel", "Erro ao carregar o produto: ${e.message}", e)
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
    fun resetProductFormEvent (){
        _productFormEvent.value = ProductEditFormEvent.Idle
    }
}
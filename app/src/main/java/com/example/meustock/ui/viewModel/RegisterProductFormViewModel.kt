package com.example.meustock.ui.viewModel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meustock.domain.model.Product
import com.example.meustock.domain.usecase.GetNextProductCodeUseCase
import com.example.meustock.domain.usecase.SaveProductUseCase
import com.example.meustock.ui.states.ProductUiState
import com.example.meustock.ui.utils.ImageUtils
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
     * Valida os campos do formulário e salva o novo produto.
     */
    fun saveProduct(imageUri: String?, context: Context) {
        viewModelScope.launch {
            val state = _uiState.value

            if (!isFormValid(state)) {
                _productFormEvent.value = ProductFormEvent.Error("Preencha todos os campos obrigatórios.")
                return@launch
            }

            _productFormEvent.value = ProductFormEvent.Loading

            try {
                val nextProductCode = getNextProductCodeUseCase()

                val imageBase64 = imageUri?.let{ uri ->
                    ImageUtils.uriToBase64(context, Uri.parse(uri))
                }

                val productToSave = createProductFromUiState(state, nextProductCode).copy(
                    imageUrl = imageBase64
                )
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
package com.example.meustock.ui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meustock.domain.model.Product
import com.example.meustock.domain.repository.ProductRepository
import com.example.meustock.domain.usecase.DetailProductUseCase
import com.example.meustock.domain.usecase.UpdateProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val detailProductUseCase: DetailProductUseCase,
) : ViewModel() {

    private val _productState = MutableStateFlow<Product?>(null)
    val productState: StateFlow<Product?> = _productState.asStateFlow()

    fun loadProduct(productId: String) {
        viewModelScope.launch {
            detailProductUseCase(productId).collect {
                _productState.value = it
            }
        }
    }

}


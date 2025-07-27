package com.example.meustock.ui.screens.product

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.meustock.ui.components.ItemProduct
import com.example.meustock.ui.viewModel.ListProductViewModel

@Composable
fun ProductListScreen(
    viewModel: ListProductViewModel,
    onDetailProduct: (String) -> Unit = {}
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ){
                ListProduct(
                    onDetailProduct = onDetailProduct,
                    viewModel = viewModel
                )
            }
        }
    }
}

@Composable
fun ListProduct(
    viewModel: ListProductViewModel,
    onDetailProduct: (String) -> Unit = {}
){
    val products by viewModel.product.collectAsState()
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 128.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ){
        items(products.size){ index ->
            val product = products[index]
            ItemProduct(
                nameProduct = product.name,
                quantity = product.currentStock,
                price = product.sellingPrice,
                detailItemProduct = {
                    onDetailProduct(product.id)
                }
            )
        }
    }
}
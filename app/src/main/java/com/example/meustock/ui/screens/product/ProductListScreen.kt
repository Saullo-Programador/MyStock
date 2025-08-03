package com.example.meustock.ui.screens.product

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.meustock.ui.components.AlertDialogComponent
import com.example.meustock.ui.components.ItemProduct
import com.example.meustock.ui.viewModel.ProductListViewModel

@Composable
fun ProductListScreen(
    viewModel: ProductListViewModel,
    onDetailProduct: (String) -> Unit = {},
    onNavigatorEdit: (String) -> Unit = {}
) {
    val openAlertDialog = remember { mutableStateOf(false) }
    val productId = remember { mutableStateOf<String?>(null) }

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
                if (openAlertDialog.value && productId.value != null) {
                    AlertDialogComponent(
                        onDismissRequest = {
                            openAlertDialog.value = false
                            productId.value = null
                        },
                        textDismiss = "Cancelar",
                        onConfirmation = {
                            openAlertDialog.value = false
                            productId.value?.let { id ->
                                viewModel.deleteProduct(id)
                                productId.value = null
                            }
                        },
                        textConfirmation = "Excluir",
                        dialogTitle = "Excluir Produto",
                        dialogText = "Tem certeza que deseja excluir este produto?",
                        icon = Icons.Default.Delete,
                        colorButtonConfirmation = Color.Red,
                        tint = Color.Red
                    )
                }

                ListProduct(
                    viewModel = viewModel,
                    onDetailProduct = onDetailProduct,
                    onDeleteProduct = { id ->
                        productId.value = id
                        openAlertDialog.value = true
                    },
                    onEditProduct = {id ->
                        onNavigatorEdit(id)
                    }
                )
            }
        }
    }
}

@Composable
fun ListProduct(
    viewModel: ProductListViewModel,
    onDetailProduct: (String) -> Unit = {},
    onDeleteProduct: (String) -> Unit = {},
    onEditProduct: (String) -> Unit = {}
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
                },
                onEdit = {
                    onEditProduct(product.id)
                },
                onDelete = {
                    onDeleteProduct(product.id)
                }
            )
        }
    }
}
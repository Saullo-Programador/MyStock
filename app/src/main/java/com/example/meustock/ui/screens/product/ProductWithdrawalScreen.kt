package com.example.meustock.ui.screens.product

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.meustock.R
import com.example.meustock.ui.components.SearchComponents
import com.example.meustock.ui.states.ProductStockUiState
import com.example.meustock.ui.viewModel.ProductStockViewModel

@Composable
fun ProductWithdrawalScreen(
    viewModel: ProductStockViewModel,
) {
    val uiState = viewModel.uiState
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            SearchComponents(
                query = uiState.query,
                onQueryChange = viewModel::onQueryChange,
                onSearch = viewModel::searchProduct,
                searchResults = viewModel.searchResults,
                onResultClick = viewModel::onSearchResultClick,
                placeholder = "ID ou Nome do Produto",
                leadingIcon = painterResource(id = R.drawable.icon_search),
                expanded = viewModel.expanded,
                onExpandedChange = viewModel::onExpandedChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ProductStockContent(
                viewModel,
                uiState
            )
        }
    }
}


@Composable
fun ProductStockContent(
    viewModel: ProductStockViewModel,
    uiState: ProductStockUiState
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {

        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            if (uiState.selectedProduct == null) {
                Text("Nenhum produto selecionado")
            } else {
                Text("ID do Produto: ${uiState.selectedProduct?.idProduct ?: ""}")
            }
        }

        uiState.selectedProduct?.let { product ->
            Spacer(modifier = Modifier.height(16.dp))
            Text("Produto: ${product.name}")
            Text("Estoque atual: ${product.currentStock}")
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = uiState.quantity,
                onValueChange = viewModel::onQuantityChange,
                label = { Text("Quantidade") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { viewModel.applyStockMovement(isEntrada = true) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                ) {
                    Text("Entrada")
                }

                Button(
                    onClick = { viewModel.applyStockMovement(isEntrada = false) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336))
                ) {
                    Text("Saída")
                }
            }
        }

        uiState.errorMessage?.let {
            Spacer(Modifier.height(16.dp))
            Text(it, color = Color.Red)
        }

        uiState.successMessage?.let {
            Spacer(Modifier.height(16.dp))
            Text(it, color = Color(0xFF388E3C))
        }
    }
}
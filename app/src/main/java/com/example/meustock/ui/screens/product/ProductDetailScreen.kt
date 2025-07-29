package com.example.meustock.ui.screens.product

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.meustock.domain.model.Product
import com.example.meustock.ui.components.DetailItem
import com.example.meustock.ui.components.TopBar
import com.example.meustock.ui.viewModel.ProductDetailViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ProductDetailScreen(
    productId: String,
    viewModel: ProductDetailViewModel,
    onBackClick: () -> Unit = {}
) {
    val productState by viewModel.productState.collectAsState()
    LaunchedEffect(productId) {
        Log.d("ProductDetail", "ProductId recebido: $productId")
        viewModel.loadProduct(productId)
    }

    when (val product = productState) {
        null -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        else -> ProductDetailContent(
            product = product,
            onBackClick = onBackClick
        )
    }
}

@Composable
fun ProductDetailContent(
    product: Product,
    onBackClick: () -> Unit = {},
){
    Scaffold(
        topBar = {
            TopBar(
                title = product.name,
                onNavigationIconClick = onBackClick
            )
        },
        modifier = Modifier
            .fillMaxSize(),

    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProductDetailForm(
                product = product
            )
        }
    }
}

@Composable
fun ProductDetailForm(
    product: Product
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Nome do Produto em destaque
        Text(
            text = product.name,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Código do Produto e Código de Barras
        if (product.idProduct.isNotBlank()) {
            DetailItem(label = "Código do Produto:", value = product.idProduct)
        }
        product.barcodeSku?.let {
            if (it.isNotBlank()) {
                DetailItem(label = "Código de Barras (SKU):", value = it)
            }
        }

        // Descrição
        product.description?.let {
            if (it.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Descrição:", style = MaterialTheme.typography.titleMedium)
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
                )
            }
        }

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp),
            thickness = DividerDefaults.Thickness,
            color = DividerDefaults.color
        )

        // Preços
        Text(text = "Informações de Preço:", style = MaterialTheme.typography.titleMedium)
        DetailItem(label = "Preço de Custo:", value = "R$ ${product.costPrice} ")
        DetailItem(label = "Preço de Venda:", value = "R$ ${product.sellingPrice}")

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp),
            thickness = DividerDefaults.Thickness,
            color = DividerDefaults.color
        )

        // Estoque
        Text(text = "Informações de Estoque:", style = MaterialTheme.typography.titleMedium)
        DetailItem(label = "Estoque Atual:", value = product.currentStock.toString())
        DetailItem(label = "Estoque Mínimo:", value = product.minimumStock.toString())
        product.stockLocation?.let {
            if (it.isNotBlank()) {
                DetailItem(label = "Localização no Estoque:", value = it)
            }
        }

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp),
            thickness = DividerDefaults.Thickness,
            color = DividerDefaults.color
        )

        // Categorização
        Text(text = "Classificação:", style = MaterialTheme.typography.titleMedium)
        DetailItem(label = "Categoria:", value = product.category)
        product.brand?.let {
            if (it.isNotBlank()) {
                DetailItem(label = "Marca:", value = it)
            }
        }

        DetailItem(label = "Unidade de Medida:", value = product.unitOfMeasurement)
        product.supplier?.let {
            if (it.isNotBlank()) {
                DetailItem(label = "Fornecedor:", value = it)
            }
        }

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp),
            thickness = DividerDefaults.Thickness,
            color = DividerDefaults.color
        )

        // Status e Datas
        Text(text = "Outras Informações:", style = MaterialTheme.typography.titleMedium)
        DetailItem(label = "Status:", value = product.status, valueColor = if (product.status == "Ativo") Color.Green else Color.Red)
        DetailItem(label = "Data de Registro:", value = formatTimestamp(product.registrationDate))
        DetailItem(label = "Última Atualização:", value = formatTimestamp(product.lastUpdateDate))

        // Observações
        product.notes?.let {
            if (it.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Observações:", style = MaterialTheme.typography.titleMedium)
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}


// Função auxiliar para formatar o timestamp
fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

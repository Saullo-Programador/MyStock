package com.example.meustock.ui.screens.product

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.meustock.domain.model.Product
import com.example.meustock.ui.components.DetailItem
import com.example.meustock.ui.components.TopBar
import com.example.meustock.ui.viewModel.ProductDetailViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.example.meustock.R
import com.example.meustock.ui.components.ViewReact
import com.example.meustock.ui.utils.ImageUtils

@Composable
fun ProductDetailScreen(
    productId: String,
    viewModel: ProductDetailViewModel,
    onBackClick: () -> Unit = {},
    onEditProduct: (String) -> Unit = {}
) {
    val productState by viewModel.productState.collectAsState()
    LaunchedEffect(productId) {
        Log.d("ProductDetail", "ProductId recebido: $productId")
        viewModel.loadProduct(productId)
    }

    when (val product = productState) {
        null -> ViewReact(type = "Loading")
        else -> {
            Scaffold(
                topBar = {
                    TopBar(
                        colorBackground = Color.Transparent,
                        title = "Detalhes do Produto", // Título genérico para a TopBar
                        onNavigationIconClick = onBackClick,
                        onTrailingIconClick = {
                            productState?.idProduct?.let { id ->
                                onEditProduct(id)
                            }
                        },
                        trailingIconPainter = painterResource(R.drawable.icon_edit),
                        colorTrailingIcon = MaterialTheme.colorScheme.primary
                    )
                },
                modifier = Modifier.fillMaxSize(),
            ) { innerPadding ->
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    ProductDetailContent(product = product)
                }
            }
        }
    }
}

@Composable
fun ProductDetailContent(
    product: Product,
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            shape = RoundedCornerShape(15.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                product.imageUrl?.let { imageUrl ->
                    if (imageUrl.isNotBlank()) {
                        val bitmap = ImageUtils.base64ToBitmap(imageUrl)
                        Image(
                            bitmap = bitmap?.asImageBitmap() ?: ImageBitmap(1, 1),
                            contentDescription = "Imagem do produto ${product.name}",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(shape = RoundedCornerShape(15.dp))
                        )
                    }
                } ?: run {
                    // Se a URL da imagem estiver vazia ou nula, mostre uma imagem padrão
                    Icon(
                        painter = painterResource(id = R.drawable.icon_image),
                        contentDescription = "Imagem do produto ${product.name}",
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .size(150.dp)
                    )
                }
            }
        }

        // Nome do Produto em destaque
        Box(
            modifier = Modifier.fillMaxWidth(),
        ){
            Text(
                text = product.name,
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 16.dp, top = 20.dp)
            )
        }

        ProductInfoCard(title = "Informações Gerais") {
            if (product.idProduct.isNotBlank()) {
                DetailItem(label = "Código do Produto:", value = product.idProduct)
            }
            product.barcodeSku?.let {
                if (it.isNotBlank()) {
                    DetailItem(label = "Código de Barras (SKU):", value = it)
                }
            }
            product.description?.let {
                if (it.isNotBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Descrição:", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        ProductInfoCard(title = "Preço") {
            DetailItem(label = "Preço de Custo:", value = "R$ ${String.format(Locale.getDefault(), "%.2f", product.costPrice)}")
            DetailItem(label = "Preço de Venda:", value = "R$ ${String.format(Locale.getDefault(), "%.2f", product.sellingPrice)}")
        }

        Spacer(modifier = Modifier.height(16.dp))

        ProductInfoCard(title = "Estoque") {
            DetailItem(label = "Estoque Atual:", value = product.currentStock.toString())
            DetailItem(label = "Estoque Mínimo:", value = product.minimumStock.toString())
            product.stockLocation?.let {
                if (it.isNotBlank()) {
                    DetailItem(label = "Localização no Estoque:", value = it)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        ProductInfoCard(title = "Classificação e Fornecedor") {
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
        }

        Spacer(modifier = Modifier.height(16.dp))

        ProductInfoCard(title = "Status e Datas") {
            DetailItem(
                label = "Status:",
                value = product.status,
                valueColor = if (product.status == "Ativo") Color(0xFF4CAF50) else Color(0xFFF44336) // Cores mais vibrantes
            )
            DetailItem(label = "Data de Registro:", value = formatTimestamp(product.registrationDate))
            DetailItem(label = "Última Atualização:", value = formatTimestamp(product.lastUpdateDate))

            product.notes?.let {
                if (it.isNotBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Observações:", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ProductInfoCard(
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(15.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            HorizontalDivider(modifier = Modifier.padding(bottom = 8.dp))
            content()
        }
    }
}

// Função auxiliar para formatar o timestamp
fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
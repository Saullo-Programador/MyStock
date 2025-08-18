package com.example.meustock.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.request.ImageRequest
import com.example.meustock.R
import com.example.meustock.ui.components.RecentActivityCard
import com.example.meustock.ui.components.RestockProductsCard
import com.example.meustock.ui.components.TopSellingProductsCard
import com.example.meustock.ui.states.DashboardUiState
import com.example.meustock.ui.viewModel.DashboardViewModel

@Composable
fun HomeScreen(
    viewModel: DashboardViewModel
){
    val state by viewModel.state.collectAsState()
    DashboardScreen(
        state = state
    )
}

@Composable
fun DashboardScreen(state: DashboardUiState) {
    Scaffold { innerPadding ->
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 80.dp)
        ) {
            Text(
                text = "Dashboard",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(16.dp))

            GifComposable(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                gifResourceId = R.drawable.checking_boxes_transparent_refined
            )

            Spacer(modifier = Modifier.height(36.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(17.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        DashboardSummaryCard(
                            title = "Total Produtos",
                            value = "${state.totalProducts} Produtos",
                            img = R.drawable.icon_product,
                            modifier = Modifier.weight(1f)
                        )
                        DashboardSummaryCard(
                            title = "Estoque Total",
                            value = "${state.totalItemsInStock} Itens",
                            img = R.drawable.icon_stock,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        DashboardSummaryCard(
                            title = "Estoque Baixo",
                            value = "${state.lowStockItems} Produtos",
                            img = R.drawable.icon_alert,
                            colorIcon = Color.Red,
                            modifier = Modifier.weight(1f)
                        )
                        DashboardSummaryCard(
                            title = "Valor Estoque",
                            value = "R$ ${String.format("%.2f", state.totalStockValue)}",
                            img = R.drawable.icon_money,
                            colorIcon = Color(0xFFBEA205),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                //TopSellingProductsCard(topProducts = state.topSellingProducts)
                RestockProductsCard(products = state.restockProducts)
                RecentActivityCard( movements = state.lastMovements)
            }
        }
    }
}

@Composable
fun DashboardSummaryCard(
    title: String,
    value: String,
    img: Int,
    colorIcon: Color = MaterialTheme.colorScheme.primary,
    modifier: Modifier = Modifier
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = modifier
            .height(100.dp)
            .graphicsLayer {
                this.shadowElevation = 10.dp.toPx()
                this.shape = RoundedCornerShape(12.dp)
                // A cor do brilho com opacidade sutil (0.5f)
                this.ambientShadowColor = colorIcon.copy(alpha = 0.5f)
                this.spotShadowColor = colorIcon.copy(alpha = 0.5f)
            }
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                Icon(
                    painter = painterResource(img),
                    tint = colorIcon,
                    contentDescription = null,
                    modifier = Modifier.size(35.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(title, style = MaterialTheme.typography.titleMedium)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                value,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun GifComposable(gifResourceId: Int, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val imgRequest = ImageRequest.Builder(context)
        .data(gifResourceId)
        .decoderFactory(GifDecoder.Factory())
        .build()

    val painter = rememberAsyncImagePainter(model = imgRequest)

    Image(
        painter = painter,
        contentDescription = null,
        modifier = modifier,
        contentScale = ContentScale.FillBounds
    )
}

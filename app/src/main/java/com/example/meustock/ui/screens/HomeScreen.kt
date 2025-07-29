package com.example.meustock.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.decode.GifDecoder
import coil.request.ImageRequest
import com.example.meustock.R
import com.example.meustock.ui.components.RecentActivityCard
import com.example.meustock.ui.components.RestockProductsCard
import com.example.meustock.ui.components.TopSellingProductsCard
import com.example.meustock.ui.states.DashboardUiState

@Composable
fun HomeScreen(
){
    HomeContent(
        state = DashboardUiState(
            totalProducts = 10,
            totalItemsInStock = 5,
            totalStockValue = 1000.0
        )
    )
}




@Composable
fun HomeContent(state: DashboardUiState) {
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

            Spacer(modifier = Modifier.height(24.dp))

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                DashboardSummaryCard(
                    title = "Produtos",
                    value = "${state.totalProducts}",
                    img = R.drawable.icon_product,
                    modifier = Modifier.weight(1f)
                )
                DashboardSummaryCard(
                    title = "Valor Estoque",
                    value = "R$ ${String.format("%.2f", state.totalStockValue)}",
                    img = R.drawable.icon_money,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            DashboardSummaryCard(
                title = "Estoque Baixo",
                value = "${state.totalItemsInStock}",
                img = R.drawable.icon_alert,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            TopSellingProductsCard()
            Spacer(modifier = Modifier.height(16.dp))
            RestockProductsCard()

            Spacer(modifier = Modifier.height(16.dp))
            RecentActivityCard()

            Spacer(modifier = Modifier.height(24.dp))

            Text("Ações Rápidas", style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Composable
fun DashboardSummaryCard(title: String, value: String, img: Int, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .height(100.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(img),
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .width(32.dp)
                    .height(32.dp)
            )
            Column {
                Text(title, style = MaterialTheme.typography.titleSmall)
                Text(value, style = MaterialTheme.typography.headlineSmall)
            }
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





@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}
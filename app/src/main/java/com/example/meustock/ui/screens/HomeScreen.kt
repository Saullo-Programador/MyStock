package com.example.meustock.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import java.text.NumberFormat
import java.util.Locale
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.meustock.R
import com.example.meustock.ui.components.LoadingScreen
import com.example.meustock.ui.components.RecentActivityCard
import com.example.meustock.ui.components.RestockProductsCard
import com.example.meustock.ui.states.DashboardUiState
import com.example.meustock.ui.viewModel.DashboardEvent
import com.example.meustock.ui.viewModel.DashboardViewModel

@Composable
fun HomeScreen(
    viewModel: DashboardViewModel
){
    val dashboardEvent by viewModel.dashboardEvent.collectAsState()

    when(dashboardEvent){
        DashboardEvent.Loading -> LoadingScreen()
        is DashboardEvent.Success -> {
            val state = (dashboardEvent as DashboardEvent.Success).uiState
            DashboardScreen(state)
        }
        is DashboardEvent.Error -> {
            val message = (dashboardEvent as DashboardEvent.Error).message
            ErrorScreen(message = message) { viewModel.retry() }
        }
        DashboardEvent.Idle -> {}
    }

}

@Composable
fun DashboardScreen(state: DashboardUiState) {

    val currencyFormatter = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
    val formattedValue = currencyFormatter.format(state.totalStockValue)
    val scrollState = rememberScrollState()


    Scaffold { innerPadding ->
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

            ImgComposable(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                img = R.raw.codingslide
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
                            value = "$formattedValue",
                            img = R.drawable.icon_money,
                            colorIcon = Color(0xFFBEA205),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
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
        elevation = CardDefaults.cardElevation(defaultElevation = 11.dp),
        modifier = modifier
            .height(100.dp)
            .border(
                width = 1.dp,
                color = colorIcon,
                shape = RoundedCornerShape(15.dp)
            )
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
fun ImgComposable(img: Int, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ){
        val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(img) )
        LottieAnimation(
            composition = composition,
            iterations = LottieConstants.IterateForever,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun ErrorScreen(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = message, color = Color.Red)
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onRetry) {
            Text("Tentar novamente")
        }
    }
}
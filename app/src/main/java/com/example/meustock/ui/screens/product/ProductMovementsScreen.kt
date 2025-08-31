package com.example.meustock.ui.screens.product

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.meustock.domain.model.ProductMovement
import com.example.meustock.ui.components.TopBar
import com.example.meustock.ui.viewModel.ProductMovementViewModel
import java.text.SimpleDateFormat

@Composable
fun ProductMovementsScreen(
    productId: String,
    viewModel: ProductMovementViewModel,
    onBack: () -> Unit
) {
    val movements by viewModel.movements.collectAsState()

    LaunchedEffect(productId) {
        viewModel.loadMovements(productId)
    }

    Scaffold(
        topBar = {
            TopBar(
                title = "Movimentações",
                onNavigationIconClick = {
                    onBack()
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){

            if ( movements.isEmpty()) {
                Text("Produto não encontrado")
            }else {
                LazyColumn {
                    items(movements) { movement ->
                        MovementItem(movement)
                    }
                }
            }
        }
    }
}

@Composable
fun MovementItem(movement: ProductMovement) {
    val dateFormatted = remember(movement.date) {
        SimpleDateFormat("dd/MM/yyyy HH:mm").format(java.util.Date(movement.date))
    }

    Card (
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(15.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = movement.type.uppercase(),
                color = if (movement.type == "Entrada" || movement.type == "entrada" || movement.type == "ENTRADA") Color(0xFF388E3C) else Color(0xFFD32F2F),
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(4.dp))
            Text("Quantidade: ${movement.quantity}")
            movement.responsible?.let {
                Text("Responsável: $it")
            }
            movement.notes?.let {
                Text("Notas: $it")
            }
            Text("Data: $dateFormatted", style = MaterialTheme.typography.labelSmall)
        }
    }
}

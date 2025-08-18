package com.example.meustock.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.meustock.domain.model.ProductMovement
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun RecentActivityCard(movements: List<ProductMovement>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.surface)
                .padding(16.dp),
        ) {
            Text(
                text = "Atividade Recente",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Cabeçalho
            Row(modifier = Modifier.fillMaxWidth()) {
                Text("Tipo", modifier = Modifier.weight(1f), style = MaterialTheme.typography.labelMedium)
                Text("Item", modifier = Modifier.weight(2f), style = MaterialTheme.typography.labelMedium)
                Text("Qtd.", modifier = Modifier.weight(1f), style = MaterialTheme.typography.labelMedium)
                Text("Data", modifier = Modifier.weight(1.5f), style = MaterialTheme.typography.labelMedium)
            }

            Spacer(modifier = Modifier.height(8.dp))

            movements.forEach { movement ->
                RecentActivityItem(
                    type = movement.type,
                    item = "Desconhecido",
                    quantity = movement.quantity,
                    date = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(movement.date))
                )
            }
        }
    }
}


@Composable
fun RecentActivityItem(
    type: String,
    item: String,
    quantity: Int,
    date: String
) {
    val typeColor = if (type == "Entrada" || type == "entrada") Color.Green else Color.Red

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = type,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodySmall,
            color = typeColor
        )
        Text(
            text = item,
            modifier = Modifier.weight(2f),
            style = MaterialTheme.typography.bodySmall
        )
        Text(
            text = "$quantity",
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodySmall
        )
        Text(
            text = date,
            modifier = Modifier.weight(1.5f),
            style = MaterialTheme.typography.bodySmall
        )
    }
}

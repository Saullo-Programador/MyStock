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

@Composable
fun RecentActivityCard() {
    Card (
        modifier = Modifier
            .fillMaxWidth(),
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
            Row (modifier = Modifier.fillMaxWidth()) {
                Text("Tipo", modifier = Modifier.weight(1f), style = MaterialTheme.typography.labelMedium)
                Text("Item", modifier = Modifier.weight(2f), style = MaterialTheme.typography.labelMedium)
                Text("Qtd.", modifier = Modifier.weight(1f), style = MaterialTheme.typography.labelMedium)
                Text("Data", modifier = Modifier.weight(1.5f), style = MaterialTheme.typography.labelMedium)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Linhas de movimentações
            RecentActivityItem("Entrada", "Smartphone X", 10, "28/07/2024")
            RecentActivityItem("Saída", "Fone de Ouvido Y", 5, "27/07/2024")
            RecentActivityItem("Entrada", "Carregador Z", 20, "27/07/2024")
            RecentActivityItem("Saída", "Mouse Pad A", 15, "26/07/2024")
            RecentActivityItem("Entrada", "Teclado B", 8, "26/07/2024")
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
    val typeColor = if (type == "Entrada") Color.Green else Color.Red

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

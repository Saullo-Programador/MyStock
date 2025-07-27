package com.example.meustock.ui.screens.product

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.meustock.ui.components.TopBar

@Composable
fun ScannerInvoiceScreen(
    onBackClick: () -> Unit = {},
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopBar(
                title = "Cadastrar Produto",
                onNavigationIconClick = { onBackClick() },
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Scanner Nota Fiscal")
        }
    }
}

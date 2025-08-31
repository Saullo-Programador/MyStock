package com.example.meustock.ui.screens.product

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.meustock.ui.components.ButtonComponent
import com.example.meustock.ui.components.TopBar
import com.example.meustock.R

@Composable
fun RegisterProductScreen(
    onBackClick: () -> Unit = {},
    onScannerInvoiceClick: () -> Unit = {},
    onRegisterProductFormClick: () -> Unit = {}
){
    RegisterProductContent(
        onBackClick = { onBackClick() },
        onScannerInvoiceClick = { onScannerInvoiceClick() },
        onRegisterProductFormClick = { onRegisterProductFormClick() }
    )
}

@Composable
fun RegisterProductContent(
    onBackClick: () -> Unit = {},
    onScannerInvoiceClick: () -> Unit = {},
    onRegisterProductFormClick: () -> Unit = {}
){
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopBar(
                title = "Registrar Produto",
                onNavigationIconClick = { onBackClick() },
            )
        }
    ) {  innerPadding ->
        Column (
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
        ){
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ButtonComponent(
                    text = "Scanner Nota Fiscal",
                    onClick = {onScannerInvoiceClick( )},
                    iconPainter = painterResource(R.drawable.icon_scanner_invoice),
                    cornerRadius = 8,
                )
                Spacer(modifier = Modifier.padding(8.dp))
                ButtonComponent(
                    text = "Cadastrar Produto",
                    onClick = { onRegisterProductFormClick() },
                    iconPainter = painterResource(R.drawable.icon_register_add),
                    cornerRadius = 8,
                )
            }
        }
    }
}


@Preview(showSystemUi = true,showBackground = true)
@Composable
fun RegisterProductScreenPreview(){
    RegisterProductScreen()
}


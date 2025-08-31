package com.example.meustock.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.meustock.R
import com.example.meustock.ui.components.AlertDialogComponent
import com.example.meustock.ui.components.ButtonComponent
import com.example.meustock.ui.components.SearchComponents
import com.example.meustock.ui.components.ViewReact
import com.example.meustock.ui.viewModel.ProductStockViewModel
import com.example.meustock.ui.viewModel.WithdrawalScreenEvent

@Composable
fun ProductWithdrawalScreen(
    viewModel: ProductStockViewModel,
    onNavMovements: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val expanded by viewModel.expanded.collectAsState()
    val event by viewModel.event.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var isEntrada by remember { mutableStateOf(true) }
    val context = LocalContext.current

    when(event) {
        is WithdrawalScreenEvent.Loading -> {
            ViewReact("Loading")
        }
        is WithdrawalScreenEvent.Success -> {
            ViewReact(
                type = "Success",
                onFinished = {
                    Toast.makeText(context, "Movimentação realizada com sucesso!", Toast.LENGTH_SHORT).show()
                    viewModel.resetEvent() // 🔥 reset aqui
                }
            )
        }
        is WithdrawalScreenEvent.Error -> {
            val message = (event as WithdrawalScreenEvent.Error).message
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            ViewReact(
                type = "Error",
                onFinished = {
                    viewModel.resetEvent()
                }
            )
        }
        WithdrawalScreenEvent.Idle -> {
            // Layout normal da tela
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    SearchComponents(
                        query = uiState.query,
                        onQueryChange = viewModel::onQueryChange,
                        onSearch = viewModel::searchProduct,
                        searchResults = searchResults,
                        onResultClick = viewModel::onSearchResultClick,
                        placeholder = "ID ou Nome do Produto",
                        leadingIcon = painterResource(id = R.drawable.icon_search),
                        expanded = expanded,
                        onExpandedChange = viewModel::onExpandedChange,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }
            ) { innerPadding ->
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 80.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    if (uiState.selectedProduct == null) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text("Pesquise o Produto para continuar",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    } else {
                        ProductStockContent(
                            name = uiState.selectedProduct!!.name,
                            brand = uiState.selectedProduct!!.brand ?: "",
                            price = uiState.selectedProduct!!.sellingPrice,
                            stock = uiState.selectedProduct!!.currentStock,
                            onEntradaClick = {
                                isEntrada = true
                                showDialog = true
                            },
                            onSaidaClick = {
                                isEntrada = false
                                showDialog = true
                            },
                            onNavMovements = { onNavMovements(uiState.selectedProduct!!.idProduct) }
                        )
                    }

                    if (showDialog) {
                        QuantityDialog(
                            isEntrada = isEntrada,
                            quantity = uiState.quantity,
                            onQuantityChange = viewModel::onQuantityChange,
                            onConfirm = {
                                viewModel.applyStockMovement(isEntrada)
                                showDialog = false
                            },
                            onDismiss = { showDialog = false }
                        )
                    }
                }
            }
        }
    }
}



@Composable
fun ProductStockContent(
    name: String,
    brand: String,
    price: Double,
    stock: Int,
    onEntradaClick: () -> Unit,
    onSaidaClick: () -> Unit,
    onNavMovements: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        ProductCard(
            name = name,
            brand = brand,
            price = price,
            stock = stock
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ButtonComponent(
                text = "Entrada",
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.surface),
                fontColor = Color(0xFF4CAF50),
                onClick = onEntradaClick,
                cornerRadius = 14,
                modifier = Modifier
                    .weight(1f)
            )
            ButtonComponent(
                text = "Saida",
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.surface),
                fontColor = Color(0xFFF44336),
                onClick = onSaidaClick,
                cornerRadius = 14,
                modifier = Modifier
                    .weight(1f)
            )
        }
        ButtonComponent(
            text = "Ver Movimentações",
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.surface),
            fontColor = Color.White,
            cornerRadius = 14,
            onClick = { onNavMovements() }
        )
    }
}


@Composable
fun ProductCard(
    name: String,
    brand: String,
    price: Double,
    stock: Int,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(15.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Imagem do produto
            Image(
                painter = painterResource(id = R.drawable._4), // substitua por a imagem real
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.height(8.dp))

            Text(name, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Row (
                modifier = Modifier
                    .padding(top = 8.dp, end = 8.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Text(brand, fontSize = 14.sp, color = Color.Gray)
                Text("R$ %.2f".format(price), fontSize = 14.sp, color = Color.Gray)
                Text("Estoque: $stock", fontSize = 14.sp, color = Color.Gray)
            }
        }
    }
}

@Composable
fun QuantityDialog(
    isEntrada: Boolean,
    quantity: String,
    onQuantityChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialogComponent(
        onDismissRequest = onDismiss,
        textDismiss = "Cancelar",
        onConfirmation = onConfirm,
        textConfirmation = if (isEntrada) "Adicionar" else "Retirar",
        dialogTitle = if (isEntrada) "Adicionar ao Estoque" else "Retirar do Estoque",
        icon = if (isEntrada) painterResource(id = R.drawable.icon_register_add) else painterResource(id = R.drawable.icon_remove),
        tint = if (isEntrada) Color(0xFF4CAF50) else Color(0xFFF44336),
        colorButtonConfirmation = if (isEntrada) Color(0xFF4CAF50) else Color(0xFFF44336),
        dialogText = {
            OutlinedTextField(
                value = quantity,
                onValueChange = onQuantityChange,
                label = { Text("Quantidade") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
    )
}
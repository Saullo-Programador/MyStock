package com.example.meustock.ui.screens.product

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.meustock.R
import com.example.meustock.ui.components.AlertDialogComponent
import com.example.meustock.ui.components.ButtonComponent
import com.example.meustock.ui.components.SearchComponents
import com.example.meustock.ui.components.ViewReact
import com.example.meustock.ui.theme.DangerRed
import com.example.meustock.ui.theme.SuccessGreen
import com.example.meustock.ui.utils.ImageUtils
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
                    viewModel.resetEvent()
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
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    if (uiState.selectedProduct == null) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center,
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    painter = painterResource(id = R.drawable.icon_search),
                                    contentDescription = null,
                                    modifier = Modifier.size(64.dp),
                                    tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
                                )
                                Spacer(Modifier.height(16.dp))
                                Text(
                                    "Pesquise um produto para\ngerenciar o estoque",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        }
                    } else {
                        ProductStockContent(
                            imgUrl = uiState.selectedProduct!!.imageUrl,
                            descImg = uiState.selectedProduct!!.name,
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
    imgUrl: String?,
    descImg: String,
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
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        ProductCard(
            imgUrl = imgUrl,
            descImg = descImg,
            name = name,
            brand = brand,
            price = price,
            stock = stock
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                "Ações de Estoque",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = 4.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ButtonComponent(
                    text = "Entrada",
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.surface),
                    fontColor = SuccessGreen,
                    onClick = onEntradaClick,
                    cornerRadius = 16,
                    elevation = ButtonDefaults.buttonElevation( defaultElevation = 4.dp),
                    modifier = Modifier.weight(1f)
                )
                ButtonComponent(
                    text = "Saida",
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.surface),
                    fontColor = DangerRed,
                    onClick = onSaidaClick,
                    cornerRadius = 16,
                    elevation = ButtonDefaults.buttonElevation( defaultElevation = 4.dp),
                    modifier = Modifier.weight(1f)
                )
            }

            ButtonComponent(
                text = "Ver Movimentações",
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.surface),
                fontColor = MaterialTheme.colorScheme.onBackground,
                elevation = ButtonDefaults.buttonElevation( defaultElevation = 4.dp),
                cornerRadius = 16,
                onClick = { onNavMovements() }
            )
        }
    }
}

@Composable
fun ProductCard(
    imgUrl: String?,
    descImg: String,
    name: String,
    brand: String?,
    price: Double,
    stock: Int,
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                imgUrl?.let { imageUrl ->
                    if (imageUrl.isNotBlank()) {
                        val bitmap = ImageUtils.base64ToBitmap(imageUrl)
                        Image(
                            painter = rememberAsyncImagePainter(bitmap),
                            contentDescription = "Imagem do produto $descImg",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(shape = RoundedCornerShape(16.dp))
                                .height(200.dp)
                        )
                    }
                } ?: run {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_image),
                            contentDescription = null,
                            modifier = Modifier.size(80.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                        )
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            Text(
                text = name,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(Modifier.height(8.dp))

            Row (
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Column {
                    Text("Marca", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(brand ?: "Indefinida", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Medium)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("Preço", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("R$ %.2f".format(price), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("Estoque", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("$stock un", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
                }
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
        tint = if (isEntrada) SuccessGreen else DangerRed,
        colorButtonConfirmation = if (isEntrada) SuccessGreen else DangerRed,
        dialogText = {
            OutlinedTextField(
                value = quantity,
                onValueChange = onQuantityChange,
                label = { Text("Quantidade") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
        }
    )
}

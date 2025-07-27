package com.example.meustock.ui.screens.product

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.meustock.ui.components.ButtonComponent
import com.example.meustock.ui.components.TopBar
import com.example.meustock.R
import com.example.meustock.ui.components.TextFeldComponent
import com.example.meustock.ui.states.ProductUiState
import com.example.meustock.ui.viewModel.ProductFormEvent
import com.example.meustock.ui.viewModel.RegisterProductFormViewModel

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


@Composable
fun RegisterProductFormScreen(
    uiState: ProductUiState,
    viewModel: RegisterProductFormViewModel,
    onBackClick: () -> Unit = {},
    onSuccessSave: () -> Unit = {}
){
    val productFormEvent by viewModel.productFormEvent.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(productFormEvent){
        when(productFormEvent){
            is ProductFormEvent.Loading -> {
                Toast.makeText(context, "Salvando Produto...", Toast.LENGTH_SHORT).show()
            }
            is ProductFormEvent.Success -> {
                Toast.makeText(context, "Produto Salvo com Sucesso!", Toast.LENGTH_SHORT).show()
                viewModel.resetProductFormEvent()
                onSuccessSave()
            }
            is ProductFormEvent.Error -> {
                val errorMessage = (productFormEvent as ProductFormEvent.Error).message
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                viewModel.resetProductFormEvent()
            }
            ProductFormEvent.Idle -> {}
        }
    }

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
            RegisterProductFrom(
                uiState = uiState,
                onNameProductChange = viewModel::updateNameProduct,
                onDescriptionChange = viewModel::updateDescription,
                onBarcodeSkuChange = viewModel::updateBarcodeSku,
                onCostPriceChange = viewModel::updateCostPrice,
                onSellingPriceChange = viewModel::updateSellingPrice,
                onCurrentStockChange = viewModel::updateCurrentStock,
                onMinimumStockChange = viewModel::updateMinimumStock,
                onCategoryChange = viewModel::updateCategory,
                onBrandChange = viewModel::updateBrand,
                onUnitOfMeasurementChange = viewModel::updateUnitOfMeasurement,
                onSupplierChange = viewModel::updateSupplier,
                onStockLocationChange = viewModel::updateStockLocation,
                onStatusChange = viewModel::updateStatus,
                onNotesChange = viewModel::updateNotes,
                onSaveClick = { viewModel.saveProduct()},
                isSaving = productFormEvent is ProductFormEvent.Loading

            )
        }
    }
}

@Composable
fun RegisterProductFrom(
    uiState: ProductUiState,
    onNameProductChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onBarcodeSkuChange: (String) -> Unit,
    onCostPriceChange: (String) -> Unit,
    onSellingPriceChange: (String) -> Unit,
    onCurrentStockChange: (String) -> Unit,
    onMinimumStockChange: (String) -> Unit,
    onCategoryChange: (String) -> Unit,
    onBrandChange: (String) -> Unit,
    onUnitOfMeasurementChange: (String) -> Unit,
    onSupplierChange: (String) -> Unit,
    onStockLocationChange: (String) -> Unit,
    onStatusChange: (String) -> Unit,
    onNotesChange: (String) -> Unit,
    onSaveClick: () -> Unit,
    isSaving: Boolean

){
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ){
        TextFeldComponent(
            value = uiState.nameProduct, // Lê do uiState
            onValueChange = onNameProductChange, // Chama a função do ViewModel
            label = "Nome do Produto*",
            placeholder = "Digite o nome do produto",
            trailingIcon = Icons.Default.Clear,
            onTrailingIconClick = { onNameProductChange("") },
            shape = RoundedCornerShape(20.dp)
        )

        // Descrição do Produto
        TextFeldComponent(
            value = uiState.description,
            onValueChange = onDescriptionChange,
            label = "Descrição do Produto",
            placeholder = "Detalhes do produto",
            trailingIcon = Icons.Default.Clear,
            onTrailingIconClick = { onDescriptionChange("") },
            shape = RoundedCornerShape(20.dp)
        )

        // Código de Barras
        TextFeldComponent(
            value = uiState.barcodeSku,
            onValueChange = onBarcodeSkuChange,
            label = "Código de Barras / SKU",
            placeholder = "Ex: 7891234567890",
            trailingIcon = Icons.Default.Clear,
            onTrailingIconClick = { onBarcodeSkuChange("") },
            shape = RoundedCornerShape(20.dp)
        )

        // Preço de Custo e Venda (em uma Row)
        Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TextFeldComponent(
                value = uiState.costPrice,
                onValueChange = onCostPriceChange,
                label = "Preço de Custo*",
                placeholder = "0.00",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(20.dp)
            )
            TextFeldComponent(
                value = uiState.sellingPrice,
                onValueChange = onSellingPriceChange,
                label = "Preço de Venda*",
                placeholder = "0.00",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(20.dp)
            )
        }

        // Estoque Atual e Mínimo (em uma Row)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TextFeldComponent(
                value = uiState.currentStock,
                onValueChange = onCurrentStockChange,
                label = "Estoque Atual*",
                placeholder = "0",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(20.dp)
            )
            TextFeldComponent(
                value = uiState.minimumStock,
                onValueChange = onMinimumStockChange,
                label = "Estoque Mínimo*",
                placeholder = "0",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(20.dp)
            )
        }

        // Categoria
        TextFeldComponent(
            value = uiState.category,
            onValueChange = onCategoryChange,
            label = "Categoria*",
            placeholder = "Ex: Eletrônicos",
            trailingIcon = Icons.Default.Clear,
            onTrailingIconClick = { onCategoryChange("") },
            shape = RoundedCornerShape(20.dp)
        )

        // Marca
        TextFeldComponent(
            value = uiState.brand,
            onValueChange = onBrandChange,
            label = "Marca",
            placeholder = "Ex: Samsung",
            trailingIcon = Icons.Default.Clear,
            onTrailingIconClick = { onBrandChange("") },
            shape = RoundedCornerShape(20.dp)
        )

        // Unidade de Medida
        TextFeldComponent(
            value = uiState.unitOfMeasurement,
            onValueChange = onUnitOfMeasurementChange,
            label = "Unidade de Medida*",
            placeholder = "Ex: unidade, kg, litro",
            trailingIcon = Icons.Default.Clear,
            onTrailingIconClick = { onUnitOfMeasurementChange("") },
            shape = RoundedCornerShape(20.dp)
        )

        // Fornecedor
        TextFeldComponent(
            value = uiState.supplier,
            onValueChange = onSupplierChange,
            label = "Fornecedor",
            placeholder = "Nome do fornecedor",
            trailingIcon = Icons.Default.Clear,
            onTrailingIconClick = { onSupplierChange("") },
            shape = RoundedCornerShape(20.dp)
        )

        // Localização no Estoque
        TextFeldComponent(
            value = uiState.stockLocation,
            onValueChange = onStockLocationChange,
            label = "Localização no Estoque",
            placeholder = "Ex: Armazém A, Corredor 3",
            trailingIcon = Icons.Default.Clear,
            onTrailingIconClick = { onStockLocationChange("") },
            shape = RoundedCornerShape(20.dp)
        )

        // Observações
        TextFeldComponent(
            value = uiState.notes,
            onValueChange = onNotesChange,
            label = "Observações",
            placeholder = "Informações adicionais",
            trailingIcon = Icons.Default.Clear,
            onTrailingIconClick = { onNotesChange("") },
            shape = RoundedCornerShape(20.dp),
        )

        Spacer(modifier = Modifier.height(6.dp))

        ButtonComponent(
            onClick = onSaveClick,
            text = if (isSaving) "Salvando..." else "Salvar",
            cornerRadius = 12,
        )

        Spacer(modifier = Modifier.height(8.dp))

    }
}


@Preview(showSystemUi = true,showBackground = true)
@Composable
fun RegisterProductScreenPreview(){
    RegisterProductScreen()
}

@Preview(showSystemUi = true,showBackground = true)
@Composable
fun ScannerInvoiceScreenPreview(){
    ScannerInvoiceScreen()
}

/*
@Preview(showSystemUi = true,showBackground = true)
@Composable
fun RegisterProductFromScreenPreview(){
    RegisterProductFormScreen(
        uiState = ProductUiState(),
        viewModel = viewModel(),
    )
}
*/

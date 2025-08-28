package com.example.meustock.ui.screens.product

import android.Manifest
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.meustock.R
import com.example.meustock.ui.components.ButtonComponent
import com.example.meustock.ui.components.CameraScreen
import com.example.meustock.ui.components.LoadingScreen
import com.example.meustock.ui.components.TextFeldComponent
import com.example.meustock.ui.components.TopBar
import com.example.meustock.ui.states.ProductEditUiState
import com.example.meustock.ui.utils.ImageUtils
import com.example.meustock.ui.viewModel.ProductEditFormEvent
import com.example.meustock.ui.viewModel.ProductEditViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ProductEditScreen (
    viewModel: ProductEditViewModel,
    uiState: ProductEditUiState,
    productId: String,
    onBackClick: () -> Unit = {},
    onSuccessEdit: () -> Unit
){
    val cameraPermissionState = rememberPermissionState( Manifest.permission.CAMERA )
    var clickCamera by remember { mutableStateOf(false) }
    var capturedImageUri by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(productId) {
        viewModel.loadProduct(productId)
    }

    if (cameraPermissionState.status.isGranted && clickCamera) {
        CameraScreen(
            onBackClick = { clickCamera = false },
            onPhotoTaken = { uri -> capturedImageUri = uri }
        )
    } else {
        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            topBar = {
                TopBar(
                    onNavigationIconClick = onBackClick,
                    title = "Editar Produto",
                    colorBackground = Color.Transparent
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ProductEditContent(
                    viewModel = viewModel,
                    uiState = uiState,
                    onSuccessEdit = onSuccessEdit,
                    onCameraClick = {
                        cameraPermissionState.launchPermissionRequest()
                        clickCamera = true
                    },
                    imgUrl = if (capturedImageUri != null){
                        capturedImageUri
                    }else{
                        uiState.imageUrl
                    }
                )
            }
        }
    }
}



@Composable
fun ProductEditContent(
    viewModel: ProductEditViewModel,
    uiState: ProductEditUiState,
    onSuccessEdit: () -> Unit = {},
    onCameraClick: () -> Unit = {},
    imgUrl: String?
){
    val context = LocalContext.current
    val productEditFormEvent by viewModel.productFormEvent.collectAsState()


    LaunchedEffect(productEditFormEvent) {
        when (productEditFormEvent) {
            is ProductEditFormEvent.Loading ->{
                Toast.makeText(context, "Salvando Produto...", Toast.LENGTH_SHORT).show()
            }
            is ProductEditFormEvent.Success -> {
                Toast.makeText(context, "Produto Salvo com Sucesso!", Toast.LENGTH_SHORT).show()
                viewModel.resetProductFormEvent()
                onSuccessEdit()
            }
            is ProductEditFormEvent.Error -> {
                val errorMessage = (productEditFormEvent as ProductEditFormEvent.Error).message
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }
            ProductEditFormEvent.Idle -> {}
        }
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (uiState.isLoading) {
            LoadingScreen(mensagem = "Carregando produto...")
        } else {
            ProductEditFrom(
                viewModel = viewModel,
                uiState = uiState,
                onCameraClick = onCameraClick,
                imgUrl = imgUrl
            )
        }
    }

}



@Composable
fun ProductEditFrom(
    viewModel: ProductEditViewModel,
    uiState: ProductEditUiState,
    onCameraClick: () -> Unit = {},
    imgUrl: String?
){
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ){

        CardImgCameraEdit(
            imgUrl = imgUrl,
            descImg = uiState.nameProduct,
            onClick = onCameraClick
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Nome do Product
        TextFeldComponent(
            value = uiState.nameProduct, // Lê do uiState
            onValueChange = uiState.onNameProductChange, // Chama a função do ViewModel
            label = "Nome do Produto*",
            placeholder = "Digite o nome do produto",
            trailingIcon = Icons.Default.Clear,
            onTrailingIconClick = { uiState.onNameProductChange("") },
            shape = RoundedCornerShape(20.dp)
        )

        // Descrição do Produto
        TextFeldComponent(
            value = uiState.description,
            onValueChange = uiState.onDescriptionChange,
            label = "Descrição do Produto",
            placeholder = "Detalhes do produto",
            trailingIcon = Icons.Default.Clear,
            onTrailingIconClick = { uiState.onDescriptionChange("") },
            shape = RoundedCornerShape(20.dp)
        )

        // Código de Barras
        TextFeldComponent(
            value = uiState.barcodeSku,
            onValueChange = uiState.onBarcodeSkuChange,
            label = "Código de Barras / SKU",
            placeholder = "Ex: 7891234567890",
            trailingIcon = Icons.Default.Clear,
            onTrailingIconClick = { uiState.onBarcodeSkuChange("") },
            shape = RoundedCornerShape(20.dp)
        )

        // Preço de Custo e Venda (em uma Row)
        Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TextFeldComponent(
                value = uiState.costPrice,
                onValueChange = uiState.onCostPriceChange,
                label = "Preço de Custo*",
                placeholder = "0.00",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(20.dp)
            )
            TextFeldComponent(
                value = uiState.sellingPrice,
                onValueChange = uiState.onSellingPriceChange,
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
                onValueChange = uiState.onCurrentStockChange,
                label = "Estoque Atual*",
                placeholder = "0",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(20.dp)
            )
            TextFeldComponent(
                value = uiState.minimumStock,
                onValueChange = uiState.onMinimumStockChange,
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
            onValueChange = uiState.onCategoryChange,
            label = "Categoria*",
            placeholder = "Ex: Eletrônicos",
            trailingIcon = Icons.Default.Clear,
            onTrailingIconClick = { uiState.onCategoryChange("") },
            shape = RoundedCornerShape(20.dp)
        )

        // Marca
        TextFeldComponent(
            value = uiState.brand,
            onValueChange = uiState.onBrandChange,
            label = "Marca",
            placeholder = "Ex: Samsung",
            trailingIcon = Icons.Default.Clear,
            onTrailingIconClick = { uiState.onBrandChange("") },
            shape = RoundedCornerShape(20.dp)
        )

        // Unidade de Medida
        TextFeldComponent(
            value = uiState.unitOfMeasurement,
            onValueChange = uiState.onUnitOfMeasurementChange,
            label = "Unidade de Medida*",
            placeholder = "Ex: unidade, kg, litro",
            trailingIcon = Icons.Default.Clear,
            onTrailingIconClick = { uiState.onUnitOfMeasurementChange("") },
            shape = RoundedCornerShape(20.dp)
        )

        // Fornecedor
        TextFeldComponent(
            value = uiState.supplier,
            onValueChange = uiState.onSupplierChange,
            label = "Fornecedor",
            placeholder = "Nome do fornecedor",
            trailingIcon = Icons.Default.Clear,
            onTrailingIconClick = { uiState.onSupplierChange("") },
            shape = RoundedCornerShape(20.dp)
        )

        // Localização no Estoque
        TextFeldComponent(
            value = uiState.stockLocation,
            onValueChange = uiState.onStockLocationChange,
            label = "Localização no Estoque",
            placeholder = "Ex: Armazém A, Corredor 3",
            trailingIcon = Icons.Default.Clear,
            onTrailingIconClick = { uiState.onStockLocationChange("") },
            shape = RoundedCornerShape(20.dp)
        )

        // Observações
        TextFeldComponent(
            value = uiState.notes,
            onValueChange = uiState.onNotesChange,
            label = "Observações",
            placeholder = "Informações adicionais",
            trailingIcon = Icons.Default.Clear,
            onTrailingIconClick = { uiState.onNotesChange("") },
            shape = RoundedCornerShape(20.dp),
        )

        Spacer(modifier = Modifier.height(6.dp))

        ButtonComponent(
            onClick = {
                viewModel.editProduct()
            },
            text = "Salvar Alterações",
            cornerRadius = 12,
        )

        Spacer(modifier = Modifier.height(8.dp))

    }
}



@Composable
private fun CardImgCameraEdit(
    imgUrl: String?,
    descImg: String,
    onClick: () -> Unit = {},
){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(15.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
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
                        .clip(shape = RoundedCornerShape(15.dp))
                        .height(250.dp)
                )

            }
        } ?: run {
            // Se a URL da imagem estiver vazia ou nula, mostre uma imagem padrão
            Image(
                painter = painterResource(id = R.drawable._4),
                contentDescription = "Imagem do produto $descImg",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(15.dp))
                    .height(250.dp)
            )
        }
    }
}

package com.example.meustock.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.meustock.R
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.meustock.ui.components.AlertDialogComponent
import com.example.meustock.ui.components.BottomSheetPassword
import com.example.meustock.ui.components.BottomSheetUser
import com.example.meustock.ui.components.TextFeldComponent
import com.example.meustock.ui.components.ViewReact
import com.example.meustock.ui.states.UserUiState
import com.example.meustock.ui.viewModel.SettingsViewModel
import com.example.meustock.ui.viewModel.UserEvent
import com.example.meustock.ui.viewModel.UserUpdateViewModel

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    viewModelUser: UserUpdateViewModel,
    onSignOutClick: () -> Unit = {}
) {
    SettingsContent(
        viewModel = viewModel,
        onSignOutClick = onSignOutClick,
        viewModelUser = viewModelUser
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsContent(
    onSignOutClick: () -> Unit = {},
    viewModel: SettingsViewModel,
    viewModelUser: UserUpdateViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val uiStateUser by viewModelUser.uiState.collectAsState()
    var showSheetPassword by remember { mutableStateOf(false) }
    val sheetStatePassword = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showSheetPerfil by remember { mutableStateOf(false) }
    val sheetStatePerfil = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val dialogState = remember { mutableStateOf(false) }
    val context = LocalContext.current

    val event by viewModelUser.userEvent.collectAsState()

    when (event) {
        UserEvent.Loading -> {
            CircularProgressIndicator()
        }
        is UserEvent.Error -> {
            Toast.makeText(
                context,
                (event as UserEvent.Error).message,
                Toast.LENGTH_SHORT
            ).show()
        }
        is UserEvent.Success -> {
            // Exemplo: feedback visual ou mensagem
            Toast.makeText(
                context,
                "Operação realizada com sucesso!",
                Toast.LENGTH_SHORT
            ).show()
        }
        UserEvent.Idle -> {
            // Estado inicial, não faz nada
        }
    }




    LaunchedEffect(uiState.error) {
        uiState.error?.let { error ->
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
            viewModel.clearError()
        }
    }

    when{
        uiState.isLoading -> {
            CircularProgressIndicator()
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                ),
                title = {
                    Text(
                        text = "Configurações",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = TextStyle(
                            fontSize = 22.sp,
                        )
                    )
                },
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
                .padding(bottom = 40.dp)
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
        ) {
            // ===== Perfil =====

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable() { showSheetPerfil = true }
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .border(1.dp, MaterialTheme.colorScheme.primary, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_person),
                        contentDescription = "Foto de perfil",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape),
                    )
                }
                Spacer(Modifier.width(16.dp))
                Column {
                    Text(
                        text = uiState.username ?: "Usuário",
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = uiState.user?.email ?: "email não disponível",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
            SettingsItem(
                icon = painterResource(R.drawable.icon_lock),
                title = "Alterar senha",
                onClick = { dialogState.value = true }
            )
            SettingsItem(
                icon = painterResource(R.drawable.icon_logout),
                title = "Sair",
                onClick = {
                    viewModel.signOut()
                    onSignOutClick()
                }
            )

            // ===== Preferências =====
            SettingSection(title = "Preferências") {
                // Definir tema
            }
            HorizontalDivider(thickness = DividerDefaults.Thickness, color = MaterialTheme.colorScheme.surfaceVariant)

            // ===== Segurança =====
            SettingSection(title = "Segurança") {
                // Ativar biometria
                //
            }
            HorizontalDivider(thickness = DividerDefaults.Thickness, color = MaterialTheme.colorScheme.surfaceVariant)



            // ===== Sobre =====
            SettingSection(title = "Sobre") {
                SettingsItem(
                    icon = painterResource(R.drawable.icon_info),
                    title = "Versão",
                    subtitle = "1.0.0",
                    onClick = {}
                )
            }
            Spacer(modifier = Modifier.height(35.dp))
        }
        if (dialogState.value) {
            DialogSenha(
                onDismiss = { dialogState.value = false },
                onConfirm = {
                    viewModelUser.verifyPassword(
                        onSuccess = {
                            showSheetPassword = true
                            dialogState.value = false
                        }
                    )
                },
                uiState = uiStateUser
            )
        }
        if ( showSheetPassword ) {
            ModalBottomSheet(
                onDismissRequest = {
                    showSheetPassword = false
                },
                sheetState = sheetStatePassword
            ) {
                BottomSheetPassword(
                    uiState = uiStateUser,
                    onDismiss = { showSheetPassword = false },
                    onConfirm = {
                        viewModelUser.updateSenha()
                        showSheetPassword = false
                    }
                )
            }
        }
        if(showSheetPerfil){
            ModalBottomSheet(
                onDismissRequest = {
                    showSheetPerfil = false
                },
                sheetState = sheetStatePerfil
            ) {
                BottomSheetUser(
                    uiState = uiStateUser,
                    onDismiss = { showSheetPerfil = false },
                    onConfirm = {
                        viewModelUser.updatePerfil()
                        showSheetPerfil = false
                    }
                )
            }
        }
    }
}

@Composable
fun SettingSection(title: String?, content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = title ?: "",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )
        content()
    }
}

@Composable
fun SettingsItem(
    icon: Painter,
    colorIcon: Color = MaterialTheme.colorScheme.onBackground,
    title: String,
    colorText: Color = MaterialTheme.colorScheme.onBackground,
    subtitle: String? = null,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable { onClick() }
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(24.dp), tint = colorIcon)
        Spacer(Modifier.width(16.dp))
        Column {
            Text(title,  fontWeight = FontWeight.Medium, color = colorText)
            if (subtitle != null) {
                Text(subtitle, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
fun SettingsSwitch(
    icon: Painter,
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(24.dp))
        Spacer(Modifier.width(16.dp))
        Text(
            text = title,
            modifier = Modifier.weight(1f),
            fontWeight = FontWeight.Medium
        )
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
fun DialogSenha(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    uiState: UserUiState
){
    AlertDialogComponent(
        onDismissRequest = { onDismiss() },
        textDismiss = "Cancelar",
        onConfirmation = { onConfirm() },
        textConfirmation = "Ok",
        dialogTitle = "Digite sua senha",
        dialogText = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TextFeldComponent(
                    value = uiState.passwordCurrent,
                    onValueChange = { uiState.onPasswordCurrentChange(it) },
                    label = "Senha",
                    placeholder = "Digite sua senha",
                    trailingIcon = Icons.Default.Clear,
                    isPasswordField = true,
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth().border(1.dp, MaterialTheme.colorScheme.onBackground, RoundedCornerShape(20.dp))
                )
            }
        },
        icon = painterResource(R.drawable.icon_lock)
    )
}
package com.example.meustock.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingsScreen(
    onScreenNotification: () -> Unit = {}
) {
    SettingsContent(
        onScreenNotification = onScreenNotification
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsContent(
    logout: () -> Unit = {},
    deleteConta: () -> Unit = {},
    onScreenNotification: () -> Unit = {},
) {
    var darkMode by remember { mutableStateOf(false) }
    var notificationsEnabled by remember { mutableStateOf(true) }
    var biometricEnabled by remember { mutableStateOf(false) }

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
                .padding(bottom = 40.dp)
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
        ) {
            // ===== Perfil =====

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable() { /* Abrir edição de perfil */ }
                    .padding(16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable._4),
                    contentDescription = "Foto de perfil",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                )
                Spacer(Modifier.width(16.dp))
                Column {
                    Text("Saullo Dantas", fontWeight = FontWeight.Bold)
                    Text("saullo@email.com", style = MaterialTheme.typography.bodyMedium)
                }
            }
            HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
            SettingsItem(
                icon = painterResource(R.drawable.round_lock_24),
                title = "Alterar senha",
                onClick = { /* Implementar */ }
            )
            SettingsItem(
                icon = painterResource(R.drawable.icon_logout),
                title = "Sair",
                onClick = { logout() }
            )

            // ===== Preferências =====
            SettingSection(title = "Preferências") {
                SettingsSwitch(
                    icon = if(darkMode == true){
                        painterResource(R.drawable.icon_dark_mode)
                    }else{
                        painterResource(R.drawable.icon_light_mode)
                    },
                    title = "Tema escuro",
                    checked = darkMode,
                    onCheckedChange = { darkMode = it }
                )
                SettingsItem(
                    icon = if (notificationsEnabled == true){
                        painterResource(R.drawable.icon_notifications_active)
                    }else{
                        painterResource(R.drawable.icon_notifications_off)
                    },
                    title = "Notificações",
                    onClick = { onScreenNotification() }
                )

            }
            HorizontalDivider(thickness = DividerDefaults.Thickness, color = MaterialTheme.colorScheme.surfaceVariant)

            // ===== Segurança =====
            SettingSection(title = "Segurança") {
                SettingsSwitch(
                    icon = if(biometricEnabled == true){
                        painterResource(R.drawable.icon_biometria)
                    }else{
                        painterResource(R.drawable.icon_biometria_off)
                    },
                    title = "Desbloqueio por biometria",
                    checked = biometricEnabled,
                    onCheckedChange = { biometricEnabled = it }
                )
                SettingsItem(
                    icon = painterResource(R.drawable.icon_delete),
                    title = "Apagar conta",
                    onClick = { deleteConta() }
                )

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
            Text(title,  fontWeight = FontWeight.Medium)
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


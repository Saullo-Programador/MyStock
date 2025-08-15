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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.meustock.ui.components.TopBar

@Composable
fun SettingsScreen(
) {
    SettingsContent()

}

@Composable
fun SettingsContent() {
    var darkMode by remember { mutableStateOf(false) }
    var notificationsEnabled by remember { mutableStateOf(true) }
    var vibrationEnabled by remember { mutableStateOf(true) }
    var biometricEnabled by remember { mutableStateOf(false) }
    var syncOverWifiOnly by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopBar(
                title = "Configurações",
                colorTitle = MaterialTheme.colorScheme.onBackground,
                colorBackground = MaterialTheme.colorScheme.background
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(bottom = 40.dp  )
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
                icon = Icons.Default.Lock,
                title = "Alterar senha",
                onClick = { /* Implementar */ }
            )
            SettingsItem(
                icon = Icons.AutoMirrored.Filled.ExitToApp,
                title = "Sair",
                onClick = { /* Logout */ }
            )

            // ===== Preferências =====
            SettingSection(title = "Preferências") {
                SettingsSwitch(
                    icon = Icons.Default.Star,
                    title = "Tema escuro",
                    checked = darkMode,
                    onCheckedChange = { darkMode = it }
                )
                SettingsItem(
                    icon = Icons.Default.AccountBox,
                    title = "Idioma",
                    subtitle = "Português (Brasil)",
                    onClick = { /* Seleção de idioma */ }
                )
                SettingsSwitch(
                    icon = Icons.Default.Notifications,
                    title = "Notificações",
                    checked = notificationsEnabled,
                    onCheckedChange = { notificationsEnabled = it }
                )
                SettingsSwitch(
                    icon = Icons.Default.Info,
                    title = "Vibração",
                    checked = vibrationEnabled,
                    onCheckedChange = { vibrationEnabled = it }
                )

            }
            HorizontalDivider(thickness = DividerDefaults.Thickness, color = MaterialTheme.colorScheme.surfaceVariant)

            // ===== Segurança =====
            SettingSection(title = "Segurança") {
                SettingsSwitch(
                    icon = Icons.AutoMirrored.Filled.List,
                    title = "Desbloqueio por biometria",
                    checked = biometricEnabled,
                    onCheckedChange = { biometricEnabled = it }
                )
                SettingsItem(
                    icon = Icons.Default.Delete,
                    title = "Apagar conta",
                    onClick = { /* Confirmar e apagar conta */ }
                )

            }
            HorizontalDivider(thickness = DividerDefaults.Thickness, color = MaterialTheme.colorScheme.surfaceVariant)

            // ===== Conectividade =====
            SettingSection(title = "Conectividade") {
                SettingsSwitch(
                    icon = Icons.Default.Email,
                    title = "Sincronizar apenas via Wi-Fi",
                    checked = syncOverWifiOnly,
                    onCheckedChange = { syncOverWifiOnly = it }
                )
                SettingsItem(
                    icon = Icons.Default.Star,
                    title = "Sincronizar agora",
                    onClick = { /* Sync manual */ }
                )
            }
            HorizontalDivider(thickness = DividerDefaults.Thickness, color = MaterialTheme.colorScheme.surfaceVariant)



            // ===== Sobre =====
            SettingSection(title = "Sobre") {
                SettingsItem(
                    icon = Icons.Default.Info,
                    title = "Versão",
                    subtitle = "1.0.0",
                    onClick = {}
                )
                SettingsItem(
                    icon = Icons.Default.Info,
                    title = "Termos de uso",
                    onClick = { /* Abrir termos */ }
                )
                SettingsItem(
                    icon = Icons.Default.Star,
                    title = "Política de privacidade",
                    onClick = { /* Abrir política */ }
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
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
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
        Icon(icon, contentDescription = null, modifier = Modifier.size(24.dp))
        Spacer(Modifier.width(16.dp))
        Column {
            Text(title, fontWeight = FontWeight.Medium)
            if (subtitle != null) {
                Text(subtitle, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
fun SettingsSwitch(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
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


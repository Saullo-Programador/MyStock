package com.example.meustock.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.meustock.ui.states.UserUiState

@Composable
fun BottomSheetPassword(
    uiState: UserUiState,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
){
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ){
        Text(
            text = "Alterar senha",
            modifier = Modifier.padding(vertical = 16.dp),
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        TextFeldComponent(
            value = uiState.password,
            onValueChange = { uiState.onPasswordChange(it) },
            label = "Senha",
            placeholder = "Digite sua senha",
            trailingIcon = Icons.Default.Clear,
            isPasswordField = true,
            shape = RoundedCornerShape(20.dp)
        )
        TextFeldComponent(
            value = uiState.passwordConfirm,
            onValueChange = { uiState.onPasswordConfirmChange(it) },
            label = "Confirmar Senha",
            placeholder = "Digite sua senha novamente",
            trailingIcon = Icons.Default.Clear,
            isPasswordField = true,
            shape = RoundedCornerShape(20.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp, bottom = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ButtonComponent(
                onClick = { onDismiss() },
                text = "Cancelar",
                cornerRadius = 12,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onBackground
                ),
                modifier = Modifier.weight(2f)
            )
            ButtonComponent(
                onClick = { onConfirm(uiState.password) },
                text = "Salvar",
                cornerRadius = 12,
                modifier = Modifier.weight(2f)
            )
        }
    }
}


@Composable
fun BottomSheetUser(
    uiState: UserUiState,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
){
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ){
        Text(
            text = "Alterar Perfil",
            modifier = Modifier.padding(vertical = 16.dp),
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        TextFeldComponent(
            value = uiState.empresa,
            onValueChange = { uiState.onEmpresaChange(it) },
            label = "Nome da Empresa",
            placeholder = "Digite o nome da empresa",
            trailingIcon = Icons.Default.Clear,
            onTrailingIconClick = { uiState.onEmpresaChange("") },
            shape = RoundedCornerShape(20.dp)
        )
        TextFeldComponent(
            value = uiState.username,
            onValueChange = { uiState.onUserNameChange(it) },
            label = "Nome de Usuário",
            placeholder = "Digite o nome de usuário",
            trailingIcon = Icons.Default.Clear,
            onTrailingIconClick = { uiState.onUserNameChange("") },
            shape = RoundedCornerShape(20.dp)
        )
        TextFeldComponent(
            value = uiState.email,
            onValueChange = { uiState.onEmailChange(it) },
            label = "E-mail",
            placeholder = "Digite seu e-mail",
            trailingIcon = Icons.Default.Clear,
            onTrailingIconClick = { uiState.onEmailChange("") },
            shape = RoundedCornerShape(20.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp, bottom = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ButtonComponent(
                onClick = { onDismiss() },
                text = "Cancelar",
                cornerRadius = 12,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onBackground
                ),
                modifier = Modifier.weight(2f)
            )
            ButtonComponent(
                onClick = { onConfirm() },
                text = "Salvar",
                cornerRadius = 12,
                modifier = Modifier.weight(2f)
            )
        }
    }
}
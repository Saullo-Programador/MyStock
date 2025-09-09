package com.example.meustock.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.meustock.R
import com.example.meustock.ui.components.ButtonGradient
import com.example.meustock.ui.components.TextFeldComponent
import com.example.meustock.ui.components.TopBar
import com.example.meustock.ui.states.ForgotPasswordUiState
import com.example.meustock.ui.theme.MeuStockTheme

@Composable
fun ForgotPasswordScreen(
    onBackClick: () -> Unit = {},
    uiState: ForgotPasswordUiState,
    onForgotPasswordClick: () -> Unit = {},
){
    ForgotPasswordContent(
        onBackClick = onBackClick,
        uiState = uiState,
        onForgotPasswordClick = onForgotPasswordClick
    )
}

@Composable
fun ForgotPasswordContent(
    onBackClick: () -> Unit = {},
    uiState: ForgotPasswordUiState,
    onForgotPasswordClick: () -> Unit = {},
){
    Scaffold(
        topBar = {
            TopBar(
                title = "Esqueceu sua Senha",
                onNavigationIconClick = onBackClick,
                colorBackground = Color.Transparent,
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(24.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ){
            //ForgotPasswordFormScreen( uiState = uiState, onForgotPasswordClick = onForgotPasswordClick)
            ForgotPasswordSuccessScreen(onResendClick = onForgotPasswordClick)
        }
    }
}

@Composable
fun ForgotPasswordFormScreen(
    onForgotPasswordClick: () -> Unit = {},
    uiState: ForgotPasswordUiState,
){
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ){
        Icon(
            imageVector = Icons.Rounded.Lock,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .size(120.dp)
        )
        Text(
            text = "Esqueceu sua Senha?",
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Text(
            text = "Insira seu email para receber um link de recuperação",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        TextFeldComponent(
            value = uiState.email ,
            onValueChange = { uiState.onEmailChange },
            label = "E-mail",
            placeholder = "Digite seu e-mail",
            leadingIcon = Icons.Default.Email,
            trailingIcon = Icons.Default.Clear,
            onTrailingIconClick = { uiState.onEmailChange("") },
            shape = RoundedCornerShape(12.dp)
        )
        ButtonGradient(
            onClick = onForgotPasswordClick,
            text = "Enviar",
            fontSize = 18,
            fontColor = Color.White
        )
    }
}

@Composable
fun ForgotPasswordSuccessScreen(
    onResendClick: () -> Unit = {},
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ){
        Image(
            painter = painterResource(id = R.drawable.emailenviado),
            modifier = Modifier
                .size(120.dp),
            contentDescription = "Email enviado",
        )
        Text(
            text = "Email enviado com sucesso!",
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Text(
            text = "Enviamos instruções de recuperação de senha para seu e-mail. Não recebeu o e-mail? Verifique seu filtro de spam ou reenvie",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        TextButton(
            onClick = { onResendClick() },
        ){
            Text(
                text = "Reenviar",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}


@Preview(
    showBackground = true,
    name = "SignUp Preview LightMode",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun ForgotPasswordPreviewLightMode(){
    MeuStockTheme(
        darkTheme = false,
        dynamicColor = false,
    ) {
        ForgotPasswordScreen(
            onBackClick = {},
            uiState = ForgotPasswordUiState(),
        )
    }
}
@Preview(
    showBackground = true,
    name = "SignUp Preview DarkMode",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun ForgotPasswordPreviewDarkMode(){
    MeuStockTheme(
        darkTheme = true,
        dynamicColor = false,
    ) {
        ForgotPasswordScreen(
            onBackClick = {},
            uiState = ForgotPasswordUiState(),
        )
    }
}


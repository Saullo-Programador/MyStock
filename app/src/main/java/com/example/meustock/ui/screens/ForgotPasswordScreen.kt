package com.example.meustock.ui.screens

import android.content.res.Configuration
import android.widget.Toast
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.meustock.R
import com.example.meustock.ui.components.ButtonGradient
import com.example.meustock.ui.components.TextFieldComponent
import com.example.meustock.ui.components.TopBar
import com.example.meustock.ui.components.ViewReact
import com.example.meustock.ui.states.ForgotPasswordUiState
import com.example.meustock.ui.theme.MeuStockTheme
import com.example.meustock.ui.viewModel.AuthViewModel

@Composable
fun ForgotPasswordScreen(
    onBackClick: () -> Unit = {},
    viewModel: AuthViewModel,
){
    ForgotPasswordContent(
        onBackClick = onBackClick,
        viewModel = viewModel,
    )
}

@Composable
fun ForgotPasswordContent(
    onBackClick: () -> Unit = {},
    viewModel: AuthViewModel,
){

    val uiState by viewModel.forgotPassword.collectAsState()
    val authUiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(authUiState.error) {
        authUiState.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearError() // cria essa função no ViewModel
        }
    }

    LaunchedEffect(uiState.success) {
        if (uiState.success) {
            Toast.makeText(context, "Email de recuperação enviado", Toast.LENGTH_SHORT).show()
        }
    }

    when{
        authUiState.isLoading -> {
            ViewReact(type = "Loading")
        }
    }
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
            if (uiState.success){
                ForgotPasswordSuccessScreen(
                    onResendClick = {
                        viewModel.forgotPassword()
                    },
                )
            }else {
                ForgotPasswordFormScreen(
                    uiState = uiState,
                    onForgotPasswordClick = {
                        viewModel.forgotPassword()
                    },
                )
            }
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
        TextFieldComponent(
            value = uiState.email ,
            onValueChange = { uiState.onEmailChange(it) },
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
            viewModel = hiltViewModel(),
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
            viewModel = hiltViewModel(),
        )
    }
}


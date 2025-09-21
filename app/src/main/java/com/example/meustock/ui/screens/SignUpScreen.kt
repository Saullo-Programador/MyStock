package com.example.meustock.ui.screens

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.meustock.R
import com.example.meustock.ui.components.ButtonGradient
import com.example.meustock.ui.components.TextFieldComponent
import com.example.meustock.ui.components.ViewReact
import com.example.meustock.ui.theme.MeuStockTheme
import com.example.meustock.ui.states.SignUpUiState
import com.example.meustock.ui.viewModel.AuthViewModel

@Composable
fun SignUpScreen(
    onSignUpClick: () -> Unit = {},
    onSignInClick: () -> Unit = {},
    viewModel: AuthViewModel,
) {
    SignUpContent(
        onSignUpClick = onSignUpClick,
        onSignInClick = onSignInClick,
        viewModel = viewModel,
    )
}

@Composable
fun SignUpContent(
    onSignUpClick: () -> Unit = {},
    onSignInClick: () -> Unit = {},
    viewModel: AuthViewModel,
) {
    val authUiState by viewModel.uiState.collectAsState()
    val uiState by viewModel.signUp.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(authUiState.error) {
        authUiState.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearError() // cria essa função no ViewModel
        }
    }


    when{
        authUiState.isLoading -> {
            ViewReact(type = "Loading")
        }
        authUiState.user != null -> {
            Toast.makeText(LocalContext.current, "Cadastro com sucesso", Toast.LENGTH_SHORT).show()
        }
    }
    if(authUiState.user != null){
        onSignUpClick()
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.TopCenter
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()

        ) {
            Image(
                painter = painterResource(id = R.drawable.shapesazul),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(end = 90.dp),
                contentScale = ContentScale.Crop
            )
        }
        SignUpForm(
            onSignUpClick = {
                viewModel.signUp()
            },
            onSignInClick = onSignInClick,
            uiState = uiState,
        )
    }
}
@Composable
fun SignUpForm(
    onSignUpClick: () -> Unit = {},
    onSignInClick: () -> Unit = {},
    uiState: SignUpUiState,
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(end = 24.dp, start = 24.dp, bottom = 15.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom

    ) {
        Text(
            text = "Cadastre-se",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 38.sp,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Left,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(15.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            TextFieldComponent(
                value = uiState.empresaName,
                onValueChange = { uiState.onEmpresaNameChange(it) },
                label = "Nome da Empresa",
                placeholder = "Digite o nome da empresa!",
                leadingIcon = Icons.Default.Email,
                trailingIcon = Icons.Default.Clear,
                onTrailingIconClick = { uiState.onEmpresaNameChange("") },
                shape = RoundedCornerShape(12.dp)
            )
            TextFieldComponent(
                value = uiState.username,
                onValueChange = { uiState.onUsernameChange(it) },
                label = "Nome do Usuário",
                placeholder = "Digite seu nome de usuário",
                leadingIcon = Icons.Default.Email,
                trailingIcon = Icons.Default.Clear,
                onTrailingIconClick = { uiState.onUsernameChange("") },
                shape = RoundedCornerShape(12.dp)
            )
            TextFieldComponent(
                value = uiState.email,
                onValueChange = { uiState.onEmailChange(it) },
                label = "E-mail",
                placeholder = "Digite seu e-mail",
                leadingIcon = Icons.Default.Email,
                trailingIcon = Icons.Default.Clear,
                onTrailingIconClick = { uiState.onEmailChange("") },
                shape = RoundedCornerShape(12.dp)
            )
            TextFieldComponent(
                value = uiState.password,
                onValueChange = { uiState.onPasswordChange(it) },
                label = "Senha",
                placeholder = "Digite sua Senha!",
                isPasswordField = true,
                leadingIcon = Icons.Default.Lock,
                shape = RoundedCornerShape(12.dp)
            )
            TextFieldComponent(
                value = uiState.passwordConfirmation,
                onValueChange = { uiState.onPasswordConfirmationChange(it) },
                label = "Confirmar Senha",
                placeholder = "Digite sua Senha Novamente!",
                isPasswordField = true,
                leadingIcon = Icons.Default.Lock,
                shape = RoundedCornerShape(12.dp)
            )
        }


        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 2.dp, horizontal = 2.dp),
        ){
            Checkbox(
                checked = uiState.check,
                onCheckedChange = { uiState.onCheckChange(it) },
                modifier = Modifier.scale(0.95f) // diminui o quadrado visual
            )
            Text(
                text = "Concordo com os termos de uso",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 13.sp,
                modifier = Modifier.padding(top = 1.dp)
            )
        }


        ButtonGradient(
            onClick = onSignUpClick,
            text = "Cadastrar",
            fontSize = 18,
            fontColor = Color.White
        )

        Spacer(modifier = Modifier.height(25.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Você já tem uma conta?", color = MaterialTheme.colorScheme.onSurfaceVariant)
            TextButton(onClick = onSignInClick) {
                Text("Faça Login", color = Color(0xFF1976D2))
            }
        }
    }
}

@Preview(
    showBackground = true,
    name = "SignUp Preview DarkMode",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun SignUpPreviewDarkMode(){
    MeuStockTheme(
        darkTheme = true,
        dynamicColor = false,
    ) {
        SignUpScreen(
            onSignUpClick = {},
            onSignInClick = {},
            viewModel = hiltViewModel(),
        )
    }
}

@Preview(
    showBackground = true,
    name = "SignUp Preview LightMode",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun SignUpPreviewLightMode(){
    MeuStockTheme(
        darkTheme = false,
        dynamicColor = false,
    ) {
        SignUpScreen(
            onSignUpClick = {},
            onSignInClick = {},
            viewModel = hiltViewModel(),
        )
    }
}
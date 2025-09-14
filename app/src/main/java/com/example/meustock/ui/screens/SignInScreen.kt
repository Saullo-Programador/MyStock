package com.example.meustock.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.meustock.R
import com.example.meustock.ui.components.ButtonGradient
import com.example.meustock.ui.components.TextFeldComponent
import com.example.meustock.ui.components.ViewReact
import com.example.meustock.ui.states.SignInUiState
import com.example.meustock.ui.viewModel.AuthViewModel

@Composable
fun SignInScreen(
    onSignUpClick: () -> Unit = {},
    onSignInClick: () -> Unit = {},
    onForgotPasswordClick: () -> Unit = {},
    viewModel: AuthViewModel,
) {
    SignInContent(
        onSignUpClick = onSignUpClick,
        onSignInClick = onSignInClick,
        onForgotPasswordClick = onForgotPasswordClick,
        viewModel = viewModel,
    )
}

@Composable
fun SignInContent(
    onSignUpClick: () -> Unit = {},
    onSignInClick: () -> Unit = {},
    onForgotPasswordClick: () -> Unit = {},
    viewModel: AuthViewModel,
) {

    val uiState by viewModel.signIn.collectAsState()
    val authUiState by viewModel.uiState.collectAsState()

    val context = LocalContext.current

    LaunchedEffect(authUiState.error) {
        authUiState.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearError() // cria essa função no ViewModel
        }
    }

    when {
        authUiState.isLoading -> {
            CircularProgressIndicator()
        }
        authUiState.user != null -> {
            Toast.makeText(LocalContext.current, "Login realizado com sucesso", Toast.LENGTH_SHORT)
                .show()
        }

    }
    if (authUiState.user != null) {
        onSignInClick()
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
                    .padding(end = 30.dp),
                contentScale = ContentScale.Crop
            )
        }
        SignInForm(
            onSignUpClick = onSignUpClick,
            onSignInClick = {viewModel.signIn()},
            onForgotPasswordClick = onForgotPasswordClick,
            uiState = uiState

        )
    }
}
@Composable
fun SignInForm(
    onSignUpClick: () -> Unit = {},
    onSignInClick: () -> Unit = {},
    onForgotPasswordClick: () -> Unit = {},
    uiState: SignInUiState,
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(end = 24.dp, start = 24.dp, bottom = 15.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom

    ) {
        Text(
            text = "Login",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 38.sp,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Left,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(20.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            TextFeldComponent(
                value = uiState.email,
                onValueChange = { uiState.onEmailChange(it) },
                label = "E-mail",
                placeholder = "Digite seu e-mail",
                leadingIcon = Icons.Default.Email,
                trailingIcon = Icons.Default.Clear,
                onTrailingIconClick = { uiState.onEmailChange("") },
                shape = RoundedCornerShape(12.dp)
            )
            TextFeldComponent(
                value = uiState.password,
                onValueChange = { uiState.onPasswordChange(it) },
                label = "Senha",
                placeholder = "Digite sua Senha!",
                isPasswordField = true,
                leadingIcon = Icons.Default.Lock,
                shape = RoundedCornerShape(12.dp)
            )
        }


        Spacer(modifier = Modifier.height(10.dp))

        Text(
            "Esqueceu a senha?",
            color = MaterialTheme.colorScheme.primary,
            fontSize = 14.sp,
            modifier = Modifier
                .align(Alignment.End)
                .clickable(onClick = onForgotPasswordClick)
        )

        Spacer(modifier = Modifier.height(15.dp))


        ButtonGradient(
            onClick = onSignInClick,
            text = "Entrar",
            fontSize = 18,
            fontColor = Color.White
        )

        Spacer(modifier = Modifier.height(90.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Você não tem uma conta?", color = MaterialTheme.colorScheme.onSurfaceVariant)
            TextButton(onClick = onSignUpClick) {
                Text("Cadastre-se", color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}


@Preview
@Composable
fun SignInPreview(){
    SignInScreen(
        onSignUpClick = {},
        onSignInClick = {},
        onForgotPasswordClick = {},
        viewModel = hiltViewModel()
    )
}
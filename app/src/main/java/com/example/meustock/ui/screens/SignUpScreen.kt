package com.example.meustock.ui.screens

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
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.meustock.R
import com.example.meustock.ui.components.ButtonGradient
import com.example.meustock.ui.components.TextFeldComponent
import com.example.meustock.ui.theme.MeuStockTheme
import com.example.meustock.ui.states.SignInUiState

@Composable
fun SignUpScreen(
    onSignUpClick: () -> Unit = {},
    onLoginClick: () -> Unit = {},
    onForgotPasswordClick: () -> Unit = {},
    uiState: SignInUiState,
) {
    SignUpContent(
        onSignUpClick = onSignUpClick,
        onLoginClick = onLoginClick,
        uiState = uiState
    )
}

@Composable
fun SignUpContent(
    onSignUpClick: () -> Unit = {},
    onLoginClick: () -> Unit = {},
    uiState: SignInUiState,
) {

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
            onSignUpClick = onSignUpClick,
            onLoginClick = onLoginClick,
            uiState = uiState

        )
    }
}
@Composable
fun SignUpForm(
    onSignUpClick: () -> Unit = {},
    onLoginClick: () -> Unit = {},
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
            TextFeldComponent(
                value = uiState.email,
                onValueChange = { uiState.onEmailChange },
                label = "Nome da Empresa",
                placeholder = "Digite o nome da empresa!",
                leadingIcon = Icons.Default.Email,
                trailingIcon = Icons.Default.Clear,
                onTrailingIconClick = { uiState.onEmailChange("") },
                shape = RoundedCornerShape(12.dp)
            )
            TextFeldComponent(
                value = uiState.email,
                onValueChange = { uiState.onEmailChange },
                label = "Nome do Usuário",
                placeholder = "Digite seu nome de usuário",
                leadingIcon = Icons.Default.Email,
                trailingIcon = Icons.Default.Clear,
                onTrailingIconClick = { uiState.onEmailChange("") },
                shape = RoundedCornerShape(12.dp)
            )
            TextFeldComponent(
                value = uiState.email,
                onValueChange = { uiState.onEmailChange },
                label = "E-mail",
                placeholder = "Digite seu e-mail",
                leadingIcon = Icons.Default.Email,
                trailingIcon = Icons.Default.Clear,
                onTrailingIconClick = { uiState.onEmailChange("") },
                shape = RoundedCornerShape(12.dp)
            )
            TextFeldComponent(
                value = uiState.password,
                onValueChange = { uiState.onPasswordChange },
                label = "Senha",
                placeholder = "Digite sua Senha!",
                isPasswordField = true,
                leadingIcon = Icons.Default.Lock,
                shape = RoundedCornerShape(12.dp)
            )
            TextFeldComponent(
                value = uiState.password,
                onValueChange = { uiState.onPasswordChange },
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
                .padding(vertical = 10.dp, horizontal = 10.dp),
        ){
            Checkbox(
                checked = false,
                onCheckedChange = {},
                modifier = Modifier.size(10.dp)
            )
            Text(
                text = "Concordo com os termos de uso",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 13.sp,
                modifier = Modifier.padding(start = 10.dp)
            )
        }


        ButtonGradient(
            onClick = onLoginClick,
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
            TextButton(onClick = onSignUpClick) {
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
            onLoginClick = {},
            onForgotPasswordClick = {},
            uiState = SignInUiState()
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
            onLoginClick = {},
            onForgotPasswordClick = {},
            uiState = SignInUiState()
        )
    }
}
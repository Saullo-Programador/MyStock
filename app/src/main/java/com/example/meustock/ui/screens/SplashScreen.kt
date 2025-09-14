package com.example.meustock.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.meustock.R
import com.example.meustock.ui.theme.MeuStockTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(
    onTimeout: () -> Unit
) {
    val scale = remember { Animatable(0f) }
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(true) {
        launch {
            scale.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
            )
        }
        launch {
            alpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing)
            )
        }

        delay(4000)
        onTimeout()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.icon_splash),
                contentDescription = null,
                modifier = Modifier
                    .padding(bottom = 10.dp)
                    .scale(scale.value)
            )
            Text(
                text = "Meu Stock",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.scale(alpha.value)
            )
        }
    }
}


@Preview(
    showBackground = true,
    name = "Splash Preview DarkMode",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun SplashPreviewDarkMode(){
    MeuStockTheme(
        darkTheme = true,
        dynamicColor = false,
    ) {
        SplashScreen(
            onTimeout = {}
        )
    }
}

@Preview(
    showBackground = true,
    name = "Splash Preview LightMode",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun SplashPreviewLightMode(){
    MeuStockTheme(
        darkTheme = false,
        dynamicColor = false,
    ) {
        SplashScreen(
            onTimeout = {}
        )
    }
}
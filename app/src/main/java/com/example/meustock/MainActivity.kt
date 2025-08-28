package com.example.meustock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.meustock.ui.navigation.AppNavigation
import com.example.meustock.ui.navigation.MainScreenWithBottomBar
import com.example.meustock.ui.navigation.Screen
import com.example.meustock.ui.theme.MeuStockTheme
import com.example.mytest.ui.notification.StockNotificationViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var isSystemTheme by rememberSaveable { mutableStateOf(true) }
            FirebaseFirestore.setLoggingEnabled(true)
            MeuStockTheme(
                dynamicColor = false,
                darkTheme = isSystemTheme,
            ) {
                val navController = rememberNavController()
                val systemUiController = rememberSystemUiController()
                SideEffect {
                    systemUiController.setStatusBarColor(
                        color = if(isSystemTheme) Color(0xFF000000) else Color(0xFFFFFFFF),
                        darkIcons = isSystemTheme
                    )
                    systemUiController.setNavigationBarColor(
                        color = if (isSystemTheme) Color(0xFF000000) else Color(0xFFFFFFFF),
                        darkIcons = isSystemTheme
                    )

                }



                Surface (
                    modifier = Modifier
                        .fillMaxSize()
                        .windowInsetsPadding(WindowInsets.systemBars),
                ){
                    MainScreenWithBottomBar(
                        navController = navController,
                        startDestination = Screen.Home.route
                    )
                }
            }
        }
    }
}

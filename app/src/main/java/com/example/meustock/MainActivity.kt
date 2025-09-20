package com.example.meustock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.meustock.ui.navigation.MainScreenWithBottomBar
import com.example.meustock.ui.navigation.Screen
import com.example.meustock.ui.theme.MeuStockTheme
import com.example.meustock.ui.viewModel.AppViewModel
import com.example.meustock.ui.viewModel.SettingsViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val isSystemThemeViewModel: SettingsViewModel = hiltViewModel()
            val isSystemTheme = isSystemThemeViewModel.isDarkMode.collectAsState(initial = false).value
            FirebaseFirestore.setLoggingEnabled(true)
            MeuStockTheme(
                dynamicColor = false,
                darkTheme = isSystemTheme,
            ) {
                val navController = rememberNavController()
                val systemUiController = rememberSystemUiController()
                val appViewModel: AppViewModel = hiltViewModel()
                val state by appViewModel.uiState.collectAsState()
                SideEffect {
                    systemUiController.setStatusBarColor(
                        color = if(isSystemTheme) Color(0xFF121212) else Color(0xFFFFFFFF),
                        darkIcons = isSystemTheme
                    )
                    systemUiController.setNavigationBarColor(
                        color = if (isSystemTheme) Color(0xFF121212) else Color(0xFFFFFFFF),
                        darkIcons = isSystemTheme
                    )

                }
                Surface (
                    modifier = Modifier
                        .fillMaxSize()
                        .windowInsetsPadding(WindowInsets.systemBars),
                ){
                    if (state.isLoading){
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                            CircularProgressIndicator()
                        }
                    }else {
                        MainScreenWithBottomBar(
                            navController = navController,
                            startDestination = Screen.Splash.route,
                        )
                    }
                }
            }
        }
    }
}

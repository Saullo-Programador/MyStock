package com.example.meustock.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.meustock.ui.screens.HomeScreen
import com.example.meustock.ui.screens.ProductListScreen
import com.example.meustock.ui.screens.ProductWithdrawalScreen
import com.example.meustock.ui.screens.RegisterProductFormScreen
import com.example.meustock.ui.screens.RegisterProductScreen
import com.example.meustock.ui.screens.ScannerInvoiceScreen
import com.example.meustock.ui.screens.SettingsScreen
import com.example.meustock.ui.viewModel.RegisterProductFormViewModel

@Composable
fun AppNavigation(
    navController: NavHostController,
    startDestination: String,
    modifier: Modifier = Modifier
) {

    NavHost(
        navController = navController,
        startDestination = startDestination
    ){
        composable(Screen.Home.route){
            HomeScreen()
        }
        composable(Screen.RegisterProduct.route){
            RegisterProductScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onScannerInvoiceClick = {
                    navController.navigate(Screen.ScannerInvoice.route)
                },
                onRegisterProductFormClick = {
                    navController.navigate(Screen.RegisterProductForm.route)
                }
            )
        }
        composable(Screen.ScannerInvoice.route){
            ScannerInvoiceScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        composable(Screen.RegisterProductForm.route){
            val viewModel: RegisterProductFormViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsState()
            RegisterProductFormScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onSuccessSave = {
                    navController.navigate(Screen.Home.route){
                        popUpTo(Screen.Home.route){
                            inclusive = true
                        }
                    }
                },
                uiState = uiState,
                viewModel = viewModel
            )
        }

        composable(Screen.Setting.route) {
            SettingsScreen()
        }

        composable(Screen.ProductList.route) {
            ProductListScreen()
        }

        composable(Screen.ProductWithdrawal.route){
            ProductWithdrawalScreen()
        }
    }
}
package com.example.meustock.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.meustock.ui.screens.HomeScreen
import com.example.meustock.ui.screens.product.ProductListScreen
import com.example.meustock.ui.screens.product.RegisterProductFormScreen
import com.example.meustock.ui.screens.product.RegisterProductScreen
import com.example.meustock.ui.screens.product.ScannerInvoiceScreen
import com.example.meustock.ui.screens.SettingsScreen
import com.example.meustock.ui.screens.product.ProductDetailScreen
import com.example.meustock.ui.screens.product.ProductEditScreen
import com.example.meustock.ui.screens.product.ProductMovementsScreen
import com.example.meustock.ui.screens.product.ProductWithdrawalScreen
import com.example.meustock.ui.viewModel.ProductListViewModel
import com.example.meustock.ui.viewModel.ProductDetailViewModel
import com.example.meustock.ui.viewModel.ProductEditViewModel
import com.example.meustock.ui.viewModel.ProductMovementViewModel
import com.example.meustock.ui.viewModel.ProductStockViewModel
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
            val viewModel: ProductListViewModel = hiltViewModel()
            ProductListScreen(
                viewModel = viewModel,
                onDetailProduct = { productId ->
                    navController.navigate(Screen.ProductDetail.createRoute(productId))
                },
                onNavigatorEdit = { productId ->
                    navController.navigate(Screen.ProductEdit.createRoute(productId))
                }
            )
        }

        composable(
            route = Screen.ProductDetail.route,
            arguments = listOf(navArgument("productId"){type = NavType.StringType})
        ){
            val viewModel: ProductDetailViewModel = hiltViewModel()
            val productId = it.arguments?.getString("productId") ?: return@composable
            ProductDetailScreen(
                productId = productId,
                viewModel = viewModel,
                onBackClick = {
                    navController.popBackStack()
                },
                onEditProduct ={productId ->
                    navController.navigate(Screen.ProductEdit.createRoute(productId))
                }
            )
        }

        composable (
            route = Screen.ProductEdit.route,
            arguments = listOf(navArgument("productId") {type = NavType.StringType})
        ){
            val viewModel: ProductEditViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsState()
            val productId = it.arguments?.getString("productId") ?: return@composable
            ProductEditScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                productId = productId,
                viewModel = viewModel,
                uiState = uiState,
                onSuccessEdit = {
                    navController.popBackStack()
                }
            )
        }
        composable(
            route = Screen.ProductMovements.route,
            arguments = listOf(navArgument("productId") {type = NavType.StringType})
        ) {
            val viewModel: ProductMovementViewModel = hiltViewModel()
            val productId = it.arguments?.getString("productId") ?: return@composable
            ProductMovementsScreen(
                viewModel = viewModel,
                productId = productId,
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.ProductWithdrawal.route){
            val viewModel: ProductStockViewModel = hiltViewModel()
            ProductWithdrawalScreen(
                viewModel = viewModel
            )
        }
    }
}
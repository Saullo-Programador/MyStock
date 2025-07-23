package com.example.meustock.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.meustock.ui.components.BottomBar

@Composable
fun MainScreenWithBottomBar(navController: NavHostController, startDestination: String) {

    val navBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute in listOf(
        Screen.Home.route,
        Screen.Setting.route,
        Screen.ProductList.route,
        Screen.ProductWithdrawal.route
    )
    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomBar(
                    navController = navController,
                    onFabClick = {
                        navController.navigate(Screen.RegisterProduct.route)
                    },
                )
            }
        }
    ) { innerPadding ->
        AppNavigation(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        )
    }
}
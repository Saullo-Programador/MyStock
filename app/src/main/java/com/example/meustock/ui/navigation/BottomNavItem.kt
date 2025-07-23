package com.example.meustock.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val title: String
) {
    object Home : BottomNavItem(Screen.Home.route, Icons.Default.Home, "Home")
    object Settings : BottomNavItem(Screen.Setting.route, Icons.Default.Settings, "Settings")
    object ProductList : BottomNavItem(Screen.ProductList.route, Icons.Default.Settings, "ProductList")
    object ProductWithdrawal : BottomNavItem(Screen.ProductWithdrawal.route, Icons.Default.Settings, "Withdrawal")
}
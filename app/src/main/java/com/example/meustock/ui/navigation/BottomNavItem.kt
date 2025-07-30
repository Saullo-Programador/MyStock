package com.example.meustock.ui.navigation

import com.example.meustock.R

sealed class BottomNavItem(
    val route: String,
    val icon: Int,
    val title: String
) {
    object Home : BottomNavItem(Screen.Home.route, R.drawable.icon_home, "Home")
    object Settings : BottomNavItem(Screen.Setting.route, R.drawable.icon_settings, "Settings")
    object ProductList : BottomNavItem(Screen.ProductList.route, R.drawable.icon_list_product, "ProductList")
    object ProductWithdrawal : BottomNavItem(Screen.ProductWithdrawal.route, R.drawable.icon_swap_vert, "Withdrawal")
}
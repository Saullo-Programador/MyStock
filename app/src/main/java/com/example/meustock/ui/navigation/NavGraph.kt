package com.example.meustock.ui.navigation

sealed class Screen(val route: String) {
    //Page Login
    object SignIn : Screen("sign_in")
    //Page Register
    object SignUp : Screen("sign_up")
    //Page Recuperar Senha
    object ForgotPassword : Screen("forgot_password")

    //Home
    object Home : Screen("home")


    // Cadastrar Produto
    object RegisterProduct : Screen("RegisterProduct")
    // Formulario de Cadastrar Produto
    object RegisterProductForm : Screen("RegisterProductForm")
    //Scanner Nota Fiscal
    object ScannerInvoice : Screen("ScannerInvoice")


    //Configurações
    object Setting : Screen("setting")


    //Lista de Produtos
    object ProductList : Screen("ProductList")

    //Detalhes do Produto
    object ProductDetail : Screen("ProductDetail/{productId}"){
        fun createRoute(productId: String) = "ProductDetail/$productId"
    }

    object ProductEdit: Screen("ProductEdit/{productId}"){
        fun createRoute(productId: String) = "ProductEdit/$productId"
    }

    //Vendas de Produtos
    object ProductWithdrawal : Screen("ProductWithdrawal")


}
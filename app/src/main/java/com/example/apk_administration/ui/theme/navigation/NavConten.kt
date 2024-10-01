package com.example.apk_administration.ui.theme.navigation

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.apk_administration.ui.theme.NFC.NFCWindow
import com.example.apk_administration.ui.theme.home.HomeScreen
import com.example.apk_administration.ui.theme.login.LoginStructre
import com.example.apk_administration.ui.theme.products.Product
import com.example.apk_administration.ui.theme.products.ProductManagementScreen
import com.example.apk_administration.ui.theme.settings.SettingsScreenContent

@Composable
fun NavigationHost(navController: NavHostController, padding: PaddingValues) {
    NavHost(navController = navController, startDestination = "home") {
        // Pantalla de login

        // Pantalla de Home
        composable("home") {
            HomeScreen(padding, navController)

        }
        composable("nfc") {
            NFCWindow(padding)
        }
        composable("admProducts"){ProductManagementScreen(products = listOf(
            Product(name = "Producto A", quantity = 10, price = 15.0),
            Product(name = "Producto B", quantity = 5, price = 25.0),
            Product(name = "Producto C", quantity = 12, price = 10.0)
        ),padding)}
        composable("setting") {
            SettingsScreenContent(padding)
        }
    }
}

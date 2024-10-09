package com.example.apk_administration.ui.theme.navigation

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.apk_administration.ui.theme.NFC.NFCWindow
import com.example.apk_administration.ui.theme.administraruser.User
import com.example.apk_administration.ui.theme.administraruser.UserManagementScreen
import com.example.apk_administration.ui.theme.home.HomeScreen
import com.example.apk_administration.ui.theme.login.LoginStructre
import com.example.apk_administration.ui.theme.products.Product
import com.example.apk_administration.ui.theme.products.ProductForm
import com.example.apk_administration.ui.theme.products.ProductManagementScreen
import com.example.apk_administration.ui.theme.registros.RegistroScreen
import com.example.apk_administration.ui.theme.settings.SettingsScreenContent
import com.example.apk_administration.ui.theme.user.PerfilScreen

@Composable
fun NavigationHost(navController: NavHostController, padding: PaddingValues) {
    NavHost(navController = navController, startDestination = "home") {
        // Pantalla de login

        // Pantalla de Home
        composable("home") {
            HomeScreen(padding, navController)

        }
        composable("nfc") {
            NFCWindow(padding, navController = navController)
        }
        composable("admProducts"){ProductManagementScreen(products = listOf(
            Product(name = "Producto A", quantity = 10, price = 15.0),
            Product(name = "Producto B", quantity = 5, price = 25.0),
            Product(name = "Producto C", quantity = 12, price = 10.0)
        ),padding, navController= navController)}
        composable("setting") {
            SettingsScreenContent(padding)
        }
        composable("perfil") { PerfilScreen(padding) }
        composable("registros"){RegistroScreen(padding)}
        // Pantalla de administración de usuarios
        composable("admUsers") {
            UserManagementScreen(
                users = listOf(
                    User(name = "Juan Pérez", role = "Administrador", email = "juan.perez@example.com"),
                    User(name = "Ana Gómez", role = "Supervisor", email = "ana.gomez@example.com"),
                    User(name = "Luis Martínez", role = "Operador", email = "luis.martinez@example.com")
                ),
                padding = padding
            )
        }

        // Pantalla del formulario de producto
        composable("product_form") {
            ProductForm { id, name, img, price, description, idTag, idCategory ->
                // Aquí iría la lógica para guardar el producto
                // Puedes hacer un `navController.popBackStack()` si deseas volver atrás después de guardar
            }
        }
    }
}

package com.example.apk_administration.ui.theme.navigation

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.apk_administration.ui.theme.Category.CategoryApiService
import com.example.apk_administration.ui.theme.NFC.NFCWindow
import com.example.apk_administration.ui.theme.NFC.NfcApiService
import com.example.apk_administration.ui.theme.administraruser.User
import com.example.apk_administration.ui.theme.administraruser.UserManagementScreen
import com.example.apk_administration.ui.theme.home.HomeScreen
import com.example.apk_administration.ui.theme.login.LoginStructre
import com.example.apk_administration.ui.theme.products.ContenidoProductoEliminar
import com.example.apk_administration.ui.theme.products.ProductForm
import com.example.apk_administration.ui.theme.products.ProductManagementScreen
import com.example.apk_administration.ui.theme.products.ProductoApiServiceC
import com.example.apk_administration.ui.theme.registros.RegistroScreen
import com.example.apk_administration.ui.theme.settings.SettingsScreenContent
import com.example.apk_administration.ui.theme.user.PerfilScreen

@Composable
fun NavigationHost(
    navController: NavHostController,
    padding: PaddingValues,
    nfcApiService: NfcApiService,
    categoryApiService: CategoryApiService,
    productoApiServiceC: ProductoApiServiceC
) {
    NavHost(navController = navController, startDestination = "home") {
        // Pantalla de login

        // Pantalla de Home
        composable("home") {
            HomeScreen(padding, navController)

        }
        composable("nfc") {
            NFCWindow(padding, navController = navController)
        }

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

        // Pantallas de productos
        composable("admProducts"){ProductManagementScreen(navController=navController, servicio=productoApiServiceC)}
        composable("product_form") { ProductForm(navController=navController,servicio= productoApiServiceC,0) }
        composable("productoEditar/{id}", arguments = listOf(
            navArgument("id") { type = NavType.IntType })
        ) {
            ProductForm(navController, productoApiServiceC, it.arguments!!.getInt("id"))
        }
        composable("productoDel/{id}", arguments = listOf(
            navArgument("id") { type = NavType.IntType })
        ) {
            ContenidoProductoEliminar(navController, productoApiServiceC, it.arguments!!.getInt("id"))
        }

    }
}

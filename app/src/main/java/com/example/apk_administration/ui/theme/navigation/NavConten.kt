package com.example.apk_administration.ui.theme.navigation

import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.apk_administration.ui.theme.Category.CategoryApiService
import com.example.apk_administration.ui.theme.Category.CategoryListScreen
import com.example.apk_administration.ui.theme.Category.ContenidoCategoryEditar
import com.example.apk_administration.ui.theme.Category.ContenidoCategoryEliminar
import com.example.apk_administration.ui.theme.NFC.NFCManager
import com.example.apk_administration.ui.theme.NFC.NFCReaderScreen
import com.example.apk_administration.ui.theme.NFC.NFCWindow
import com.example.apk_administration.ui.theme.NFC.NfcApiService
import com.example.apk_administration.ui.theme.NFC.ProductNFCReader
import com.example.apk_administration.ui.theme.NFC.ProductNFCReaderScreen
import com.example.apk_administration.ui.theme.administraruser.User
import com.example.apk_administration.ui.theme.administraruser.UserManagementScreen
import com.example.apk_administration.ui.theme.home.HomeScreen
import com.example.apk_administration.ui.theme.login.LoginStructre
import com.example.apk_administration.ui.theme.products.AddOrEditProductScreen
import com.example.apk_administration.ui.theme.products.ContenidoProductoEliminar
import com.example.apk_administration.ui.theme.products.ProductManagementScreen
import com.example.apk_administration.ui.theme.products.ProductModel
import com.example.apk_administration.ui.theme.products.ProductViewModel
import com.example.apk_administration.ui.theme.products.ProductoApiService
import com.example.apk_administration.ui.theme.products.ProductoApiServiceC
import com.example.apk_administration.ui.theme.products.ProductoDetailScreen
import com.example.apk_administration.ui.theme.registros.RegistroScreen
import com.example.apk_administration.ui.theme.settings.SettingsScreenContent
import com.example.apk_administration.ui.theme.user.PerfilScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationHost(
    navController: NavHostController,
    padding: PaddingValues,
    nfcApiService: NfcApiService,
    categoryApiService: CategoryApiService,
    productoApiServiceC: ProductoApiServiceC,
    activity: Activity,
    productViewModel: ProductViewModel,
    productoApiService: ProductoApiService
) {
    NavHost(navController = navController, startDestination = "home") {
        // Pantalla de login

        // Pantalla de Home
        composable("home") {
            HomeScreen(padding, navController, productoApiService)

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
        composable("admProducts"){ProductManagementScreen(navController=navController, servicio=productoApiService)}
        composable("AddProduct") {
            AddOrEditProductScreen( navController = navController,servicio = productoApiService, 0)
        }
        composable("productoEditar/{id}", arguments = listOf(
            navArgument("id") { type = NavType.IntType })
        ) {
            AddOrEditProductScreen(navController, productoApiService, it.arguments!!.getInt("id"))
        }
        composable("productoDel/{id}", arguments = listOf(
            navArgument("id") { type = NavType.IntType })
        ) {
            ContenidoProductoEliminar(navController, productoApiService, it.arguments!!.getInt("id"))
        }
        composable("productoVer/{id}", arguments = listOf(
            navArgument("id") { type = NavType.StringType })
        ) {
            ProductoDetailScreen(it.arguments!!.getString("id")!!, navController, productoApiServiceC)
        }


        //Pantallas de categorias
        composable("categoryList") {
            CategoryListScreen(navController, categoryApiService)
        }
        composable("categoriaNueva") {
            ContenidoCategoryEditar(navController, categoryApiService, 0)
        }
        composable("categoriaEditar/{id}", arguments = listOf(
            navArgument("id") { type = NavType.IntType })
        ) {
            ContenidoCategoryEditar(navController, categoryApiService, it.arguments!!.getInt("id"))
        }
        composable("categoriaDel/{id}", arguments = listOf(
            navArgument("id") { type = NavType.IntType })
        ) {
            ContenidoCategoryEliminar(navController, categoryApiService, it.arguments!!.getInt("id"))
        }

        //Pantallas NFC
        composable("nfc_reader") {
            val products = productViewModel.productList.collectAsState().value
            NFCReaderScreen(activity, nfcApiService, products = products ) }
        composable("nfc_producto") {
            ProductNFCReader(activity, productViewModel, nfcApiService)
        }
    }
}

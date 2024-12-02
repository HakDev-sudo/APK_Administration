package com.example.apk_administration.ui.theme.navigation

import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.apk_administration.ui.theme.Category.CategoryApiService
import com.example.apk_administration.ui.theme.Category.CategoryDetailsScreen
import com.example.apk_administration.ui.theme.Category.CategoryListScreen
import com.example.apk_administration.ui.theme.Category.CategoryViewModel
import com.example.apk_administration.ui.theme.Category.ContenidoCategoryEditar
import com.example.apk_administration.ui.theme.Category.ContenidoCategoryEliminar
import com.example.apk_administration.ui.theme.NFC.NFCManager
import com.example.apk_administration.ui.theme.NFC.NFCReaderScreen
import com.example.apk_administration.ui.theme.NFC.NFCWindow
import com.example.apk_administration.ui.theme.NFC.NfcApiService
import com.example.apk_administration.ui.theme.NFC.ProductDetail
import com.example.apk_administration.ui.theme.NFC.ProductNFCReader
import com.example.apk_administration.ui.theme.NFC.ProductNFCReaderScreen
import com.example.apk_administration.ui.theme.NFC.ReceiptScreen
import com.example.apk_administration.ui.theme.administraruser.User
import com.example.apk_administration.ui.theme.administraruser.UserManagementScreen
import com.example.apk_administration.ui.theme.home.HomeScreen
import com.example.apk_administration.ui.theme.login.LoginStructre
import com.example.apk_administration.ui.theme.products.AddOrEditProductScreen
import com.example.apk_administration.ui.theme.products.ContenidoProductoEliminar
import com.example.apk_administration.ui.theme.products.ProductManagementScreen
import com.example.apk_administration.ui.theme.products.ProductModel
import com.example.apk_administration.ui.theme.products.ProductSearchScreen
import com.example.apk_administration.ui.theme.products.ProductViewModel
import com.example.apk_administration.ui.theme.products.ProductoApiService
import com.example.apk_administration.ui.theme.products.ProductoDetailScreen
import com.example.apk_administration.ui.theme.registros.RegistroScreen
import com.example.apk_administration.ui.theme.settings.SettingsScreenContent
import com.example.apk_administration.ui.theme.stock.StockMovementViewModel
import com.example.apk_administration.ui.theme.user.PerfilScreen
import com.google.gson.Gson

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationHost(
    navController: NavHostController,
    padding: PaddingValues,
    nfcApiService: NfcApiService,
    categoryApiService: CategoryApiService,
    activity: Activity,
    productViewModel: ProductViewModel,
    productoApiService: ProductoApiService,
    stockMovementViewModel: StockMovementViewModel,
    categoryViewModel: CategoryViewModel
) {
    NavHost(navController = navController, startDestination = "home") {
        // Pantalla de login

        // Pantalla de Home
        composable("home") {
            HomeScreen(padding, navController, productoApiService, categoryApiService)

        }
        composable("nfc") {
            NFCWindow(padding, navController = navController)
        }

        composable("setting") {
            SettingsScreenContent(padding)
        }
        composable("perfil") { PerfilScreen(padding) }
        composable("registros"){RegistroScreen(padding, stockMovementViewModel, productViewModel)}
        // Pantalla de administración de usuarios
        composable("admUsers") {
            UserManagementScreen(
                users = listOf(
                    User(name = "Juan Pérez", role = "Administrador", email = "juan.perez@example.com"),
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
            navArgument("id") { type = NavType.IntType })
        ) {
            ProductoDetailScreen(productId = it.arguments!!.getInt("id"), navController = navController, productoApiService, nfcApiService)
        }
        composable("productoSearch") {ProductSearchScreen(productViewModel, navController)}


        //Pantallas de categorias
        composable("categoryList") {
            CategoryListScreen(navController, categoryApiService)
        }
        composable("categoriaNueva") {
            ContenidoCategoryEditar(navController, categoryApiService, productoApiService,0)
        }
        composable("categoriaEditar/{id}", arguments = listOf(
            navArgument("id") { type = NavType.IntType })
        ) {
            ContenidoCategoryEditar(navController, categoryApiService,productoApiService, it.arguments!!.getInt("id"))
        }
        composable("categoriaDel/{id}", arguments = listOf(
            navArgument("id") { type = NavType.IntType })
        ) {
            ContenidoCategoryEliminar(navController, categoryApiService, it.arguments!!.getInt("id"))
        }
        composable("categoryDetails/{categoryId}",
            arguments = listOf(navArgument("categoryId") { type = NavType.IntType })
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getInt("categoryId") ?: 0
            CategoryDetailsScreen(
                navController,
                categoryId = categoryId,
                categoryViewModel
            )
        }

        //Pantallas NFC
        composable("nfc_reader") {
            val products = productViewModel.productList.collectAsState().value
            NFCReaderScreen(activity, nfcApiService, productViewModel, stockMovementViewModel ) }
        composable("nfc_producto") {
            ProductNFCReader(activity, productViewModel, nfcApiService, stockMovementViewModel, navController)
        }
        //stock
        composable(
            route = "receipt/{receiptDataJson}",
            arguments = listOf(navArgument("receiptDataJson") { type = NavType.StringType })
        ) { backStackEntry ->
            // Obtener el JSON de los datos desde los argumentos
            val receiptDataJson = backStackEntry.arguments?.getString("receiptDataJson") ?: ""
            // Mostrar la pantalla de boleta
            ReceiptScreen(receiptDataJson = receiptDataJson,navController)
        }

    }
}

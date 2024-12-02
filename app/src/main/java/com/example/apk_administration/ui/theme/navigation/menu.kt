package com.example.apk_administration.ui.theme.navigation

import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.apk_administration.ui.theme.Category.CategoryApiService
import com.example.apk_administration.ui.theme.Category.CategoryViewModel
import com.example.apk_administration.ui.theme.NFC.NFCManager
import com.example.apk_administration.ui.theme.NFC.NfcApiService
import com.example.apk_administration.ui.theme.NFC.NfcViewModel
import com.example.apk_administration.ui.theme.account.AuthNavHost
import com.example.apk_administration.ui.theme.home.HomeScreen
import com.example.apk_administration.ui.theme.products.ProductViewModel
import com.example.apk_administration.ui.theme.products.ProductoApiService
import com.example.apk_administration.ui.theme.stock.StockMovementApiService
import com.example.apk_administration.ui.theme.stock.StockMovementViewModel
import com.google.gson.GsonBuilder
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AlmacenApp() {
    // Configuración de Retrofit y creación de servicios API
    val urlBase = "http://192.168.18.33:8000/" // o tu IP si usarás un dispositivo externo
    val gson = GsonBuilder().serializeNulls().create()
    val retrofit = Retrofit.Builder().baseUrl(urlBase)
        .addConverterFactory(GsonConverterFactory.create(gson)).build()

    // Crear instancias de los servicios API
    val nfcApiService = retrofit.create(NfcApiService::class.java)
    val categoryApiService = retrofit.create(CategoryApiService::class.java)


    // Inicializar el NavController
    val navController = rememberNavController()
    val productoApiService = retrofit.create(ProductoApiService::class.java)
    val productViewModel = ProductViewModel(productoApiService)
    val stockMovementApiService = retrofit.create(StockMovementApiService::class.java)
    val stockMovementViewModel = StockMovementViewModel(stockMovementApiService, nfcApiService, productViewModel)
    val categoryViewModel = CategoryViewModel(categoryApiService,productoApiService)
    val nfcViewModel = NfcViewModel(nfcApiService)

    // Llamar al CustomScaffold y pasar los servicios API como parámetros
    CustomScaffold(
        navController = navController,
        nfcApiService = nfcApiService,
        categoryApiService = categoryApiService,
        productViewModel = productViewModel,
        productoApiService = productoApiService,
        stockMovementViewModel = stockMovementViewModel,
        categoryViewModel = categoryViewModel,
        nfcViewModel = nfcViewModel

    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CustomScaffold(
    navController: NavHostController = rememberNavController(),
    nfcApiService: NfcApiService,
    categoryApiService: CategoryApiService,
    productViewModel: ProductViewModel,
    productoApiService: ProductoApiService,
    stockMovementViewModel: StockMovementViewModel,
    categoryViewModel: CategoryViewModel,
    nfcViewModel: NfcViewModel
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var isExtended by remember { mutableStateOf(true) }
    val isSearching = remember { mutableStateOf(false) }
    // Observa cambios en la ruta actual
    val currentRoute = navController.currentBackStackEntryFlow.collectAsState(initial = null)
    // Resetea la barra de búsqueda automáticamente al cambiar de pantalla
    // Resetea la barra de búsqueda solo si cambias de ruta
    LaunchedEffect(currentRoute.value) {
        if (currentRoute.value?.destination?.route != "productoSearch") {
            isSearching.value = false
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                navController = navController,
                drawerState = drawerState,
                scope = scope
            )
        }
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.systemBars) // Aquí respetamos las barras del sistema
        ) {
            Scaffold(

                topBar = { CustomTopBar(navController, productViewModel,isSearching) },
                bottomBar = { CustomBottomBar(navController) { scope.launch { drawerState.open() } } },

                // Aquí aseguramos que el contenido principal se ajuste correctamente
                content = { padding ->
                    Box(
                        modifier = Modifier.fillMaxSize()
                            .background(MaterialTheme.colorScheme.background).padding(padding)
                    ) {
                        NavigationHost(
                            navController = navController,
                            padding = PaddingValues(0.dp),
                            nfcApiService = nfcApiService,
                            categoryApiService = categoryApiService,
                            activity = navController.context as Activity,
                            productViewModel = productViewModel,
                            productoApiService = productoApiService,
                            stockMovementViewModel = stockMovementViewModel,
                            categoryViewModel = categoryViewModel,
                            nfcViewModel = nfcViewModel

                        )
                    }
                }
            )
        }
    }
}

@Composable
fun CustomFAB(isExtended: Boolean) {
    ExtendedFloatingActionButton(
        onClick = { /* TODO */ },
        expanded = isExtended,
        icon = { Icon(Icons.Filled.Add, "Añadir") },
        text = { Text("Añadir Registro", fontSize = 10.sp) },
        containerColor = MaterialTheme.colorScheme.primary
    )
}




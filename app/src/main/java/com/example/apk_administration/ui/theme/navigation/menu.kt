package com.example.apk_administration.ui.theme.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.apk_administration.ui.theme.Category.CategoryApiService
import com.example.apk_administration.ui.theme.NFC.NfcApiService
import com.example.apk_administration.ui.theme.account.AuthNavHost
import com.example.apk_administration.ui.theme.home.HomeScreen
import com.example.apk_administration.ui.theme.products.ProductoApiServiceC
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@Composable
fun AlmacenApp() {
    // Configuración de Retrofit y creación de servicios API
    val urlBase = "http://10.0.2.2:8000/" // o tu IP si usarás un dispositivo externo
    val retrofit = Retrofit.Builder().baseUrl(urlBase)
        .addConverterFactory(GsonConverterFactory.create()).build()

    // Crear instancias de los servicios API
    val nfcApiService = retrofit.create(NfcApiService::class.java)
    val categoryApiService = retrofit.create(CategoryApiService::class.java)
    val productoApiServiceC = retrofit.create(ProductoApiServiceC::class.java)

    // Inicializar el NavController
    val navController = rememberNavController()

    // Llamar al CustomScaffold y pasar los servicios API como parámetros
    CustomScaffold(
        navController = navController,
        nfcApiService = nfcApiService,
        categoryApiService = categoryApiService,
        productoApiServiceC = productoApiServiceC
    )
}

@Composable
fun CustomScaffold(
    navController: NavHostController = rememberNavController(),
    nfcApiService: NfcApiService,
    categoryApiService: CategoryApiService,
    productoApiServiceC: ProductoApiServiceC
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(navController)
        }
    ) {
        Scaffold(
            topBar = { CustomTopBar() },
            bottomBar = { CustomBottomBar(navController) { scope.launch { drawerState.open() } } },
            floatingActionButton = { CustomFAB() },
            // Aquí aseguramos que el contenido principal se ajuste correctamente
            content = { padding ->
                Box(modifier = Modifier.padding(padding)) {
                    NavigationHost(
                        navController = navController,
                        padding = PaddingValues(0.dp),
                        nfcApiService = nfcApiService,
                        categoryApiService = categoryApiService,
                        productoApiServiceC = productoApiServiceC
                    )
                }
            }
        )
    }
}

@Composable
fun CustomFAB() {
    FloatingActionButton(
        // Color de fondo
        //backgroundColor = MaterialTheme.colors.primary,
        // Acción al hacer clic en el botón (sin definir)
        onClick = { /*TODO*/ }) {
        Text(
            fontSize = 24.sp, // Tamaño de fuente del texto del botón
            text = "+" // Texto del botón
        )
    }
}




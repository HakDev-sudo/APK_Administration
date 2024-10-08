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
import com.example.apk_administration.ui.theme.account.AuthNavHost
import com.example.apk_administration.ui.theme.home.HomeScreen
import kotlinx.coroutines.launch


@Composable
fun MainScreen() {
    // Estado de autenticación (puedes usar lógica real para verificar si el usuario está autenticado)
    val isLoggedIn = remember { false } // Esto se actualizará a 'true' si el usuario ha iniciado sesión.

    // Si el usuario está autenticado, muestra la navegación principal. Si no, muestra la navegación de login/registro.
    if (isLoggedIn) {
        // Controlador de navegación principal
        val navController = rememberNavController()
        // Navegación principal (pantalla de home, etc.)
        NavigationHost(navController = navController, padding = PaddingValues(0.dp))
    } else {
        // Navegación para autenticación (login/registro)
        AuthNavHost()
    }
}

@Composable
fun CustomScaffold(
    navController: NavHostController = rememberNavController()
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
                    NavigationHost(navController = navController, padding = PaddingValues(0.dp))
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



@Preview(showBackground = true)
@Composable
fun PreviewCustomScaffold() {
    CustomScaffold()
}
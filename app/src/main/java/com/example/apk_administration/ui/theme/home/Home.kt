package com.example.apk_administration.ui.theme.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Input
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.apk_administration.R
import com.example.apk_administration.ui.theme.products.ProductoApiService


// Acá escribiras tu
@Composable
fun HomeScreen(
    padding: PaddingValues,
    navController: NavHostController,
    servicio: ProductoApiService
) {
    // Estado para almacenar el total del stock
    var totalStock by remember { mutableStateOf(0) }
    var isLoading by remember { mutableStateOf(true) }
    // Obtener y calcular el stock total
    LaunchedEffect(Unit) {
        val response = servicio.selectProductos()
        if (response.isNotEmpty()) {
            // Suma el stock de todos los productos
            totalStock = response.sumOf { it.stock ?: 0 } // Usa 0 si `stock` es null
        }
        isLoading = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    ) {

        // Carrusel de categorías (llamando al nuevo componente)
        val categories = listOf(
            Category("Filtros", R.drawable.filtros_1),
            Category("Aceite", R.drawable.filtros_1),
            Category("Motores", R.drawable.filtros_1),
            Category("Respuestos", R.drawable.filtros_1)
        )
        CategoryCarousel(categories = categories, navController = navController)
        Spacer(modifier = Modifier.height(16.dp))
        // Botones de acción rápida
        QuickActionButtons(navController = navController)
        Spacer(modifier = Modifier.height(16.dp))
        // Panel de estado

        // Panel de estado
        if (isLoading) {
            CircularProgressIndicator() // Indicador de carga
        } else {
            StatusCard(title = "Inventario actual", value = "$totalStock items", icon = Icons.Filled.Inventory)
        }
        StatusCard(title = "Pendiente de entrada", value = "300 items", icon = Icons.Filled.Input)

    }
}

@Composable
fun StatusCard(title: String, value: String, icon: ImageVector) {
    Card(
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.padding(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, contentDescription = null)
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = title, style = MaterialTheme.typography.bodyLarge)
                Text(text = value, style = MaterialTheme.typography.titleLarge)
            }
        }
    }
}


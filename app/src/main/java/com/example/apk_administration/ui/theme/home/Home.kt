package com.example.apk_administration.ui.theme.home

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.filled.Output
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.apk_administration.R
import com.example.apk_administration.ui.theme.Category.CategoryApiService
import com.example.apk_administration.ui.theme.products.ProductoApiService
import com.example.apk_administration.ui.theme.stock.StockMovementViewModel


// Acá escribiras tu
@Composable
fun HomeScreen(
    padding: PaddingValues,
    navController: NavHostController,
    servicio: ProductoApiService,
    categoriaServicio: CategoryApiService,
    stockMovementViewModel: StockMovementViewModel
) {
    var totalStock by remember { mutableStateOf(0) }
    var isLoading by remember { mutableStateOf(true) }
    var totalEntries by remember { mutableStateOf(0) }
    var totalExits by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        val response = servicio.selectProductos()
        if (response.isNotEmpty()) {
            totalStock = response.sumOf { it.stock ?: 0 }
        }
        // Obtener totales de entradas y salidas
        stockMovementViewModel.getTotalEntriesAndExits { totals ->
            totalEntries = totals.values.sumOf { it.first } // Suma de todas las entradas
            totalExits = totals.values.sumOf { it.second } // Suma de todas las salidas
        }
        isLoading = false
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Encabezado
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shadowElevation = 4.dp,
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(modifier = Modifier.padding(4.dp)) {
                    CategoryCarousel(navController = navController, servicio = categoriaServicio)
                }
            }
        }

        // Sección de acciones rápidas
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = MaterialTheme.shapes.medium,

            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Acciones Rápidas",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    QuickActionButtons(navController = navController)
                }
            }
        }

        // Panel de estado
        item {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
            ) {
                if (isLoading) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                } else {
                    StatusCard(
                        title = "Inventario actual",
                        value = "$totalStock items",
                        icon = Icons.Filled.Inventory,
                        backgroundColor = MaterialTheme.colorScheme.primaryContainer
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    StatusCard(
                        title = "Total Entradas",
                        value = "$totalEntries items",
                        icon = Icons.Filled.Input,
                        backgroundColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    StatusCard(
                        title = "Total Salidas",
                        value = "$totalExits items",
                        icon = Icons.Filled.Output,
                        backgroundColor = MaterialTheme.colorScheme.errorContainer
                    )
                }
            }
        }

        // Espacio adicional al final para asegurar que el último elemento sea visible
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun StatusCard(
    title: String,
    value: String,
    icon: ImageVector,
    backgroundColor: Color
) {
    Card(
        shape = MaterialTheme.shapes.large,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = value,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}


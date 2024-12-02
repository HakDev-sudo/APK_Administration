package com.example.apk_administration.ui.theme.registros

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.apk_administration.ui.theme.products.ProductViewModel
import com.example.apk_administration.ui.theme.stock.StockMovementViewModel

@Composable
fun RegistroScreen(
    padding: PaddingValues,
    viewModel: StockMovementViewModel,
    productViewModel: ProductViewModel
) {
    val groupedEntries = remember { mutableStateOf<Map<String, Map<Int, Int>>>(emptyMap()) }
    val groupedExits = remember { mutableStateOf<Map<String, Map<Int, Int>>>(emptyMap()) }
    val productMap by productViewModel.productIdToNameMap.collectAsState()
    val filterType = remember { mutableStateOf("Todos") }
    var showFilterSheet by remember { mutableStateOf(false) }

    // Llama a las funciones del ViewModel para obtener los datos agrupados
    LaunchedEffect(Unit) {
        viewModel.getGroupedProductEntriesByDate { groupedEntries.value = it }
        viewModel.getGroupedProductExitsByDate { groupedExits.value = it }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .verticalScroll(rememberScrollState())
    ) {
        // Filtro y botón para nuevo registro
        FiltroYBotonNuevoRegistro(filterType) { selectedType ->
            // Actualizar datos según el filtro seleccionado
            when (selectedType) {
                "Entradas" -> {
                }
                "Salidas" -> {
                }
                else -> {
                }
            }
        }
        if (showFilterSheet) {
            FilterBottomSheet(
                filterType = filterType,
                onFilterApply = { showFilterSheet = false },
                onDismissRequest = { showFilterSheet = false }
            )
        }

        // Mostrar entradas agrupadas por fecha
        when (filterType.value) {
            "Entradas" -> {
                Text("Entradas", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(16.dp))
                groupedEntries.value.forEach { (date, products) ->
                    MostrarProductosAgrupados(date, products, productMap, "Entrada")
                }
            }
            "Salidas" -> {
                Text("Salidas", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(16.dp))
                groupedExits.value.forEach { (date, products) ->
                    MostrarProductosAgrupados(date, products, productMap, "Salida")
                }
            }
            else -> {
                Text("Entradas", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(16.dp))
                groupedEntries.value.forEach { (date, products) ->
                    MostrarProductosAgrupados(date, products, productMap, "Entrada")
                }
                Spacer(Modifier.height(16.dp))
                Text("Salidas", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(16.dp))
                groupedExits.value.forEach { (date, products) ->
                    MostrarProductosAgrupados(date, products, productMap, "Salida")
                }
            }



        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiltroYBotonNuevoRegistro(
    filterType: MutableState<String>,
    onFilterApply: (String?) -> Unit
) {
    var showFilterSheet by remember { mutableStateOf(false) }

    Surface(
        tonalElevation = 2.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "Entradas y Salidas",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            actions = {
                IconButton(
                    onClick = { showFilterSheet = true },
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.FilterList,
                        contentDescription = "Filtrar Productos"
                    )
                }

            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface,
                titleContentColor = MaterialTheme.colorScheme.onSurface
            ),
            modifier = Modifier.shadow(4.dp)
        )
        // ModalBottomSheet para seleccionar filtros
        if (showFilterSheet) {
            FilterBottomSheet(
                filterType = filterType,
                onFilterApply = { selectedType ->
                    onFilterApply(selectedType)  // Aplicar el filtro
                    showFilterSheet = false
                },
                onDismissRequest = { showFilterSheet = false }
            )
        }
    }
}

@Composable
fun RegistroDeProductoCard(
    producto: String,
    cantidad: Int,
    fecha: String,
    tipo: String
) {
    val (backgroundColor, textColor, iconTint) = when (tipo) {
        "Entrada" -> Triple(
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f),
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.primary
        )
        "Salida" -> Triple(
            MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.2f),
            MaterialTheme.colorScheme.error,
            MaterialTheme.colorScheme.error
        )
        else -> Triple(
            MaterialTheme.colorScheme.surface,
            MaterialTheme.colorScheme.onSurface,
            MaterialTheme.colorScheme.onSurface
        )
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
            ),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = producto,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = "Cantidad: $cantidad",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Schedule,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                    Text(
                        text = fecha,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    color = when (tipo) {
                        "Entrada" -> MaterialTheme.colorScheme.primaryContainer
                        "Salida" -> MaterialTheme.colorScheme.errorContainer
                        else -> MaterialTheme.colorScheme.surfaceVariant
                    },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.padding(4.dp)
                ) {
                    Text(
                        text = tipo,
                        style = MaterialTheme.typography.labelLarge,
                        color = textColor,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun MostrarProductosAgrupados(
    date: String,
    products: Map<Int, Int>,
    productMap: Map<Int, String>,
    tipo: String
) {
    Text("Fecha: $date", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(start = 16.dp, top = 8.dp))
    products.forEach { (productId, quantity) ->
        val productName = productMap[productId] ?: "Producto desconocido"
        RegistroDeProductoCard(
            producto = productName,
            cantidad = quantity,
            fecha = date,
            tipo = tipo
        )
    }
}

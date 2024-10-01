package com.example.apk_administration.ui.theme.registros

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
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RegistroScreen(padding: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .verticalScroll(rememberScrollState())
    ) {
        // Filtro y botón para nuevo registro
        FiltroYBotonNuevoRegistro()

        // Lista de registros de productos
        Spacer(modifier = Modifier.height(16.dp))
        RegistroDeProductoCard(
            producto = "Producto A",
            cantidad = 50,
            fecha = "2024-10-01",
            tipo = "Entrada"
        )
        RegistroDeProductoCard(
            producto = "Producto B",
            cantidad = 20,
            fecha = "2024-09-30",
            tipo = "Salida"
        )
        RegistroDeProductoCard(
            producto = "Producto C",
            cantidad = 10,
            fecha = "2024-09-29",
            tipo = "Entrada"
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiltroYBotonNuevoRegistro() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TopAppBar(
            title = { Text("Entrada y salidas", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
            actions = {
                IconButton(onClick = { /* Acción para agregar producto */ }) {
                    Icon(Icons.Default.Add, contentDescription = "Agregar Producto")
                }
                IconButton(onClick = { /* Acción para filtrar productos */ }) {
                    Icon(Icons.Default.FilterList, contentDescription = "Filtrar Productos")
                }
            }
        )
    }
}

@Composable
fun RegistroDeProductoCard(
    producto: String,
    cantidad: Int,
    fecha: String,
    tipo: String
) {
    val (backgroundColor, textColor) = when (tipo) {
        "Entrada" -> Color(0xFFDFF0D8) to Color(0xFF3C763D)
        "Salida" -> Color(0xFFF2DEDE) to Color(0xFFA94442)
        else -> MaterialTheme.colorScheme.surface to MaterialTheme.colorScheme.onSurface
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Columna con detalles del producto
            Column {
                Text(
                    text = producto,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Cantidad: $cantidad",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Fecha: $fecha",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }

            // Columna con tipo y botón de eliminar
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = tipo,
                    style = MaterialTheme.typography.bodyMedium,
                    color = textColor,
                    modifier = Modifier.align(Alignment.End)
                )

                Spacer(modifier = Modifier.height(8.dp))

                IconButton(
                    onClick = { /* Implementar lógica de eliminar registro */ }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Eliminar registro",
                        tint = Color.Red
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegistroScreenPreview() {
    RegistroScreen(padding = PaddingValues(0.dp))
}

package com.example.apk_administration.ui.theme.products

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun ContenidoProductoEliminar(navController: NavHostController, servicio: ProductoApiService, id: Int) {
    val viewModel = remember { ProductViewModel(servicio) }
    val operationStatus by viewModel.operationStatus.collectAsState()

    var showDialog by remember { mutableStateOf(true) } // Controla si se muestra el diálogo
    var confirmDelete by remember { mutableStateOf(false) }

    // Lanza la eliminación del producto
    if (confirmDelete) {
        LaunchedEffect(confirmDelete) {
            viewModel.deleteProduct(id)
            confirmDelete = false
        }
    }

    // Mostrar el diálogo de confirmación
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { navController.navigate("admProducts") },
            confirmButton = {
                Button(onClick = { confirmDelete = true }) {
                    Text(text = "Eliminar")
                }
            },
            dismissButton = {
                Button(onClick = { navController.navigate("admProducts") }) {
                    Text(text = "Cancelar")
                }
            },
            title = { Text(text = "Eliminar Producto") },
            text = { Text(text = "¿Está seguro de eliminar este producto?") }
        )
    }

    // Mostrar mensajes de éxito o error en un Snackbar
    operationStatus?.let { message ->
        LaunchedEffect(message) {
            showDialog = false // Cierra el diálogo cuando haya un estado de operación
        }
        Snackbar(
            modifier = Modifier.padding(16.dp),
            action = {
                TextButton(onClick = {
                    viewModel.clearOperationStatus() // Limpia el estado
                    navController.navigate("admProducts")
                }) {
                    Text("CERRAR", color = MaterialTheme.colorScheme.inversePrimary)
                }
            }
        ) {
            Text(message)
        }
    }
}

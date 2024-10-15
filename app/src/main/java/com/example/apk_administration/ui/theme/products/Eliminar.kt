package com.example.apk_administration.ui.theme.products

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController

@Composable
fun ContenidoProductoEliminar(navController: NavHostController, servicio: ProductoApiServiceC, id: Int) {
    var confirmDelete by remember { mutableStateOf(false) }

    if (confirmDelete) {
        LaunchedEffect(Unit) {
            // Llamamos al nuevo método deleteProducto de ProductoApiServiceC
            val response = servicio.deleteProducto(id.toString())
            if (response.isSuccessful) {
                navController.navigate("admProducts") // Navegamos de vuelta a la lista de productos tras el borrado
            } else {
                // Manejar error si es necesario
                // Puedes mostrar un mensaje o hacer un log de la respuesta
            }
        }
    }

    // Configuración de AlertDialog para confirmar la eliminación
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
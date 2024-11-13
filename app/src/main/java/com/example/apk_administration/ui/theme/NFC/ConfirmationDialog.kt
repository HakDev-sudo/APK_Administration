package com.example.apk_administration.ui.theme.NFC

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.apk_administration.ui.theme.products.ProductModel
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ConfirmationDialog(
    nfcId: String?,
    products: List<ProductModel>,
    onConfirm: (selectedProduct: ProductModel?) -> Unit,
    onCancel: () -> Unit
) {
    val fechaDeHoy: LocalDate = LocalDate.now() // Obtiene la fecha actual
    var selectedProduct by remember { mutableStateOf<ProductModel?>(null) } // Producto seleccionado
    var expanded by remember { mutableStateOf(false) }
    AlertDialog(
        onDismissRequest = onCancel,
        title = { Text("Confirmar Registro NFC") },
        text = {
            Column {
                Text("ID de la etiqueta: $nfcId")
                Text("Estado: Asignado")
                Text("Fecha Asignado: $fechaDeHoy") // Fecha actual como ejemplo
                // Selección del producto
                Text("Seleccionar Producto:")
                Box {
                    Button(onClick = { expanded = true }) {
                        Text(selectedProduct?.name ?: "Seleccionar producto")
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        products.forEach { product ->
                            DropdownMenuItem(
                                onClick = {
                                selectedProduct = product
                                expanded = false
                                },
                                modifier = Modifier.fillMaxWidth(),
                                text = { Text(product.name) }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                onConfirm(selectedProduct)
                onCancel()}) {
                Text("Registrar")
            }
        },
        dismissButton = {
            Button(onClick = onCancel) {
                Text("Cancelar")
            }
        }
    )
}

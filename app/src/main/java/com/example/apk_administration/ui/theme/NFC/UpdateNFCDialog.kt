package com.example.apk_administration.ui.theme.NFC
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.apk_administration.ui.theme.products.ProductModel
import com.example.apk_administration.ui.theme.stock.StockMovementViewModel
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UpdateNFCDialog(
    nfcId: String?,
    products: List<ProductModel>,
    onConfirm: (selectedProduct: ProductModel?, quantity: Int, description: String) -> Unit,
    onCancel: () -> Unit
) {
    val fechaDeHoy: LocalDate = LocalDate.now()
    var selectedProduct by remember { mutableStateOf<ProductModel?>(null) } // Producto seleccionado
    var expanded by remember { mutableStateOf(false) }
    var quantity by remember { mutableStateOf(1) } // Cantidad fija para el registro
    var description by remember { mutableStateOf("Asignación automática") }

    AlertDialog(
        onDismissRequest = onCancel,
        title = { Text("Actualizar Registro NFC") },
        text = {
            Column {
                Text("ID de la etiqueta: $nfcId")
                Text("Estado: Actualización")
                Text("Fecha: $fechaDeHoy")
                Spacer(modifier = Modifier.height(8.dp))

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
                                text = { Text(product.name) }
                            )
                        }
                    }
                }

            }
        },
        confirmButton = {
            Button(onClick = {
                onConfirm(selectedProduct, quantity, description)
                onCancel()
            }) {
                Text("Actualizar")
            }
        },
        dismissButton = {
            Button(onClick = onCancel) {
                Text("Cancelar")
            }
        }
    )
}

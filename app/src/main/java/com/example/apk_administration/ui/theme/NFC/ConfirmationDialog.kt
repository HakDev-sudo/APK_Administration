package com.example.apk_administration.ui.theme.NFC

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ConfirmationDialog(
    nfcId: String?,
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    val fechaDeHoy: LocalDate = LocalDate.now() // Obtiene la fecha actual
    AlertDialog(
        onDismissRequest = onCancel,
        title = { Text("Confirmar Registro NFC") },
        text = {
            Column {
                Text("ID de la etiqueta: $nfcId")
                Text("Estado: Sin Asignar")
                Text("Fecha Asignado: $fechaDeHoy") // Fecha actual como ejemplo
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm()
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

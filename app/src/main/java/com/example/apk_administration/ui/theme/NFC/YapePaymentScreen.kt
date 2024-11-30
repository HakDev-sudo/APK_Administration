package com.example.apk_administration.ui.theme.NFC

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.apk_administration.R

@Composable
fun YapePaymentScreen() {
    var showDialog by remember { mutableStateOf(false) }

    // Botón principal que abre el diálogo
    IconButton(
        onClick = { showDialog = true }
    ) {
        Icon(
            imageVector = Icons.Default.Payment,
            contentDescription = "Pagar con Yape",
            tint = MaterialTheme.colorScheme.primary
        )
    }

    // Dialog flotante
    if (showDialog) {
        Dialog(
            onDismissRequest = { showDialog = false }
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.7f)
                    .padding(8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top // Cambiado a Top para controlar manualmente el espaciado
                ) {
                    // Título
                    Text(
                        text = "Paga con Yape",
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(bottom = 8.dp) // Reducido el padding vertical
                    )

                    // Contenedor para la imagen
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(vertical = 2.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        // Imagen QR de Yape
                        Image(
                            painter = painterResource(id = R.drawable.yape_qr),
                            contentDescription = "Código QR de Yape",
                            modifier = Modifier
                                .fillMaxSize(0.95f)
                                .padding(4.dp), // Reducido el padding
                            contentScale = ContentScale.Fit
                        )
                    }

                    // Botón para cerrar
                    Button(
                        onClick = { showDialog = false },
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .padding(top = 2.dp), // Reducido el padding vertical
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text(
                            "Cerrar",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(vertical = 4.dp) // Reducido el padding
                        )
                    }
                }
            }
        }
    }
}
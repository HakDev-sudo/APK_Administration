package com.example.apk_administration.ui.theme.NFC

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.apk_administration.R

@Composable
fun YapePaymentScreen() {
    var showDialog by remember { mutableStateOf(false) }

    // Botón principal mejorado
    IconButton(
        onClick = { showDialog = true },
        modifier = Modifier.size(48.dp),
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        Icon(
            imageVector = Icons.Default.Payment,
            contentDescription = "Pagar con Yape",
            modifier = Modifier.size(24.dp)
        )
    }

    if (showDialog) {
        Dialog(
            onDismissRequest = { showDialog = false },
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            )
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.7f)
                    .padding(16.dp),
                shape = MaterialTheme.shapes.large,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Encabezado con ícono y título
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Payment,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Paga con Yape",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    // Contenedor para el QR con borde y fondo
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        shape = MaterialTheme.shapes.medium,
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        border = BorderStroke(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                        )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.yape_qr),
                                contentDescription = "Código QR de Yape",
                                modifier = Modifier.fillMaxSize(0.9f),
                                contentScale = ContentScale.Fit
                            )
                        }
                    }

                    // Botones de acción
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Botón secundario para copiar
                        OutlinedButton(
                            onClick = { /* Implementar copia */ },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Copiar")
                        }

                        // Botón principal para cerrar
                        Button(
                            onClick = { showDialog = false },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text(
                                "Cerrar",
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }
                }
            }
        }
    }
}
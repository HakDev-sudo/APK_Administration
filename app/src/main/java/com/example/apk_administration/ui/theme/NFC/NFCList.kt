package com.example.apk_administration.ui.theme.NFC

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday

import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Nfc
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.apk_administration.ui.theme.common.ErrorStateWithSnackbar


@Composable
fun NfcScreenlist(nfcviewModel: NfcViewModel) {
    val nfcList by nfcviewModel.nfcList.collectAsState()
    val errorMessage by nfcviewModel.errorMessage.collectAsState()

    // Mostrar mensaje de error, si existe
    errorMessage?.let { message ->
        Text(
            text = message,
            color = Color.Red,
            modifier = Modifier.padding(8.dp)
        )
    }

    // Lista de etiquetas NFC
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(nfcList) { nfc ->
            NfcCard(nfc = nfc, onDelete = { nfcviewModel.deleteNfcTag(nfc.id) })
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
    // Manejo de error con Snackbar
    errorMessage?.let { message ->
        ErrorStateWithSnackbar(
            errorMessage = message,
            onRetry = { /* Aquí podrías volver a cargar las etiquetas */ },
            onDismiss = { nfcviewModel._errorMessage.value = null }
        )
    }
}

@Composable
fun NfcCard(nfc: NfcModel, onDelete: () -> Unit) {
    var confirmDelete by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icono NFC
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Nfc,
                        contentDescription = "NFC Icon",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier
                            .padding(8.dp)
                            .size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Información de la etiqueta
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "ID Tag: ${nfc.idTag}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.RadioButtonChecked,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = when(nfc.status.lowercase()) {
                                "activo" -> MaterialTheme.colorScheme.primary
                                "inactivo" -> MaterialTheme.colorScheme.error
                                else -> MaterialTheme.colorScheme.outline
                            }
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Estado: ${nfc.status}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CalendarToday,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.outline
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Asignado: ${nfc.fechaAsignado}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Botón de eliminar con efecto de ripple
            IconButton(
                onClick =  {confirmDelete = true },
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar etiqueta",
                    modifier = Modifier.size(24.dp)
                )
            }
            // Confirmación de eliminación
            if (confirmDelete) {
                AlertDialog(
                    onDismissRequest = { confirmDelete = false },
                    confirmButton = {
                        Button(onClick = {
                            confirmDelete = false
                            onDelete()
                        }) {
                            Text(text = "Eliminar")
                        }
                    },
                    dismissButton = {
                        Button(onClick = { confirmDelete = false }) {
                            Text(text = "Cancelar")
                        }
                    },
                    title = { Text(text = "Eliminar etiqueta NFC") },
                    text = { Text(text = "¿Está seguro de que desea eliminar esta etiqueta?") }
                )
            }
        }
    }
}


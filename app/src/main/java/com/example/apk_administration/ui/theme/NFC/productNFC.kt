package com.example.apk_administration.ui.theme.NFC

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.apk_administration.ui.theme.products.ProductViewModel
import com.example.apk_administration.ui.theme.products.ProductoApiServiceC
import com.example.apk_administration.ui.theme.products.ProductoModelGet
import kotlinx.coroutines.launch


@Composable
fun ProductNFCReader(
    activity: Activity,
    viewModel: ProductViewModel,
    apiService: NfcApiService) {

    LaunchedEffect(Unit) {
        viewModel.refreshProducts()
    }
    // Inicializa NFCManager y estados
    val nfcManager = remember { NFCManager(activity, apiService) }
    val isNFCEnabled = remember { mutableStateOf(nfcManager.isNFCEnabled()) }
    val isReaderActive = remember { mutableStateOf(false) }

    // Componente ProductNFCReaderScreen
    ProductNFCReaderScreen(
        activity = activity,
        viewModel = viewModel,
        nfcManager = nfcManager,
        isReaderActive = isReaderActive.value,
        onActivateReader = {
            isReaderActive.value = true
            nfcManager.enableReaderMode()
        },
        onDeactivateReader = {
            isReaderActive.value = false
            nfcManager.disableReaderMode()
        },
        onClearNFCId = {
            nfcManager.clearNFCId()
        }
    )
}

@Composable
fun ProductNFCReaderScreen(
    activity: Activity,
    viewModel: ProductViewModel,
    nfcManager: NFCManager,
    isReaderActive: Boolean,
    onActivateReader: () -> Unit,
    onDeactivateReader: () -> Unit,
    onClearNFCId: () -> Unit
) {
    val nfcId by nfcManager.nfcId.collectAsState()
    val productList by viewModel.productList.collectAsState()
    var matchedProduct by remember { mutableStateOf<ProductoModelGet?>(null) }
    val scannedProducts = remember { mutableStateListOf<ProductoModelGet>() }
    val snackbarHostState = remember { SnackbarHostState() }

    // Lógica para activar y desactivar el modo de lector NFC
    LaunchedEffect(isReaderActive) {
        if (isReaderActive) {
            onActivateReader()
        } else {
            onDeactivateReader()
        }
    }

    // Efecto para actualizar `matchedProduct` y agregar a `scannedProducts` cada vez que cambia `nfcId`
    LaunchedEffect(nfcId) {
        nfcId?.let { idTag ->
            matchedProduct = productList.find { it.idNFC?.id_tag == idTag }
            if (matchedProduct != null && !scannedProducts.contains(matchedProduct)) {
                // Agrega el producto encontrado a la lista de productos escaneados
                scannedProducts.add(matchedProduct!!)
            } else if (matchedProduct == null) {
                snackbarHostState.showSnackbar("No se encontró ningún producto vinculado")
            }
        }
    }

    // Interfaz de usuario para mostrar el producto vinculado o un mensaje si no se encuentra
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        SnackbarHost(hostState = snackbarHostState)

        if (matchedProduct != null) {
            // Mostrar la información del producto encontrado
            Text("Producto Escaneado: ${matchedProduct!!.name}")
            Text("Precio: ${matchedProduct!!.price}")
        } else {
            Text("Escanea una etiqueta NFC vinculada")
        }

        // Botón para activar o desactivar el lector
        Button(
            onClick = {
                if (isReaderActive) onDeactivateReader() else onActivateReader()
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = if (isReaderActive) "Desactivar Lector NFC" else "Activar Lector NFC")
        }

        // Botón para limpiar el ID y permitir una nueva lectura
        if (nfcId != null) {
            Button(
                onClick = { onClearNFCId() },
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = "Realizar nueva lectura")
            }
        }

        // Mostrar la lista de productos escaneados
        Text("Productos Escaneados:", style = MaterialTheme.typography.labelMedium)
        LazyColumn {
            items(scannedProducts) { product ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(8.dp)
                ) {
                    Text("Nombre: ${product.name}")
                    Text("Precio: ${product.price}")
                }
            }
        }
    }
}



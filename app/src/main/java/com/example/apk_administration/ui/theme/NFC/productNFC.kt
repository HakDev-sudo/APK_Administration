package com.example.apk_administration.ui.theme.NFC

import android.app.Activity
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.apk_administration.ui.theme.common.SuccessMessage
import com.example.apk_administration.ui.theme.products.ProductModel
import com.example.apk_administration.ui.theme.products.ProductViewModel
import com.example.apk_administration.ui.theme.stock.StockMovementViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun ProductNFCReader(
    activity: Activity,
    viewModel: ProductViewModel,
    apiService: NfcApiService,
    stockMovementViewModel: StockMovementViewModel,
    navController: NavHostController
) {

    LaunchedEffect(Unit) {
        viewModel.refreshProducts()
        viewModel.refreshNfcs()
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
        },
        stockMovementViewModel = stockMovementViewModel,
        navController = navController
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
    onClearNFCId: () -> Unit,
    stockMovementViewModel: StockMovementViewModel,
    navController: NavHostController
) {
    val nfcId by nfcManager.nfcId.collectAsState()
    val productList by viewModel.productList.collectAsState()
    val nfcList by viewModel.nfcList.collectAsState()
    var matchedProduct by remember { mutableStateOf<ProductModel?>(null) }
    val scannedProducts = remember { mutableStateMapOf<Pair<ProductModel, String>, Int>() }
    val scannedNfcTags = remember { mutableSetOf<String>() }
    val snackbarHostState = remember { SnackbarHostState() }

    var showSuccessMessage by remember { mutableStateOf(false) }
    var successMessage by remember { mutableStateOf("") }

    // Estado para el monto total de los productos escaneados
    var totalAmount by remember { mutableStateOf(0.0) }
    // Mantener la lista NFC local y sincronizada
    var currentNfcList by remember { mutableStateOf<List<NfcModel>>(emptyList()) }
    val coroutineScope = rememberCoroutineScope()

    // Actualiza la lista local cuando nfcList cambia
    LaunchedEffect(viewModel.nfcList) {
        currentNfcList = viewModel.nfcList.value
    }

    // Actualizar datos al cargar la pantalla
    LaunchedEffect(Unit) {
        viewModel.refreshProducts()
        viewModel.refreshNfcs()
    }
    // Lógica para activar y desactivar el modo de lector NFC
    LaunchedEffect(isReaderActive) {
        if (isReaderActive) {
            onActivateReader()
        } else {
            onDeactivateReader()
        }
    }
    LaunchedEffect(productList) {
        // Lógica para procesar productos actualizados, si aplica
        Log.d("ProductNFCReaderScreen", "Lista de productos actualizada: ${productList.size}")
    }

    // Efecto para procesar cada nueva lectura NFC
    LaunchedEffect(nfcId) {


        nfcId?.let { idTag ->
            if (nfcList.isEmpty()) {
                snackbarHostState.showSnackbar("No se han cargado las etiquetas NFC")
                return@LaunchedEffect
            }
            if (scannedNfcTags.contains(idTag)) {
                snackbarHostState.showSnackbar("Etiqueta NFC ya registrada")
            } else {
                // Buscar producto y agregarlo junto con su `idTag`
                val matchedNfc = nfcList.find { it.idTag == idTag }
                matchedProduct = matchedNfc?.let { nfc ->
                    productList.find { it.id == nfc.product }
                }

                matchedProduct?.let { product ->
                    // Usar el par (producto, idTag) como clave para escaneos únicos
                    val productKey = product to idTag
                    scannedNfcTags.add(idTag)
                    scannedProducts[productKey] = (scannedProducts[productKey] ?: 0) + 1
                    totalAmount += product.price
                } ?: snackbarHostState.showSnackbar("No se encontró ningún producto vinculado a esta etiqueta NFC")
            }
            onClearNFCId()
        }
    }
    SuccessMessage(
        message = successMessage,
        isVisible = showSuccessMessage,
        onDismiss = { showSuccessMessage = false }
    )
    // Interfaz de usuario para mostrar los productos escaneados y permitir eliminarlos
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        SnackbarHost(hostState = snackbarHostState)

        // Mostrar información del último producto escaneado
        if (matchedProduct != null) {
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

        // Lista de productos escaneados con la cantidad y subtotal
        Text("Productos Escaneados:", style = MaterialTheme.typography.labelMedium)
        LazyColumn {
            items(scannedProducts.keys.toList()) { productKey ->
                val (product, idTag) = productKey
                val quantity = scannedProducts[productKey] ?: 0
                val subtotal = product.price * quantity

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Nombre: ${product.name}")
                        Text("Precio Unitario: ${product.price}")
                        Text("Cantidad: $quantity")
                        Text("Subtotal: $${"%.2f".format(subtotal)}", fontWeight = FontWeight.Bold)
                    }

                    // Botón para eliminar el producto de la lista
                    IconButton(onClick = {
                        val productKey = product to idTag // Utilizar la clave de producto con su `idTag`

                        // Actualizar cantidad o eliminar completamente el producto
                        if (scannedProducts[productKey]!! > 1) {
                            scannedProducts[productKey] = scannedProducts[productKey]!! - 1
                            totalAmount -= product.price
                        } else {
                            scannedProducts.remove(productKey)
                            totalAmount -= product.price
                            scannedNfcTags.remove(idTag) // Eliminar `idTag` asociado con el producto
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Eliminar producto",
                            tint = Color.Red
                        )
                    }
                }
            }
        }

        // Mostrar el monto total
        Text(
            text = "Monto total: $${"%.2f".format(totalAmount)}",
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(top = 16.dp)
        )
        YapePaymentScreen()
        // Nuevo botón para registrar la salida de los productos escaneados
        Button(
            onClick = {
                scannedProducts.forEach { (productKey, quantity) ->
                    val (product, idTag) = productKey
                    val nfcTagId = nfcList.find { it.idTag == idTag }?.id

                    nfcTagId?.let { tagId ->
                        stockMovementViewModel.registerProductExit(
                            productId = product.id,
                            nfcTagId = tagId,
                            quantity = quantity,
                            description = "Salida de producto registrada desde el lector NFC",
                            onSuccess = {
                                successMessage = "¡Registro de salida exitoso"
                                showSuccessMessage = true
                                coroutineScope.launch {
                                    delay(2000) // Espera 2 segundos
                                    navController.navigate("nfc")
                                }

                            }
                        )
                    }
                }

                // Limpiar la lista de productos escaneados y el monto total después de registrar la salida
                scannedProducts.clear()
                scannedNfcTags.clear()
                totalAmount = 0.0
                viewModel.refreshNfcs()

            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Registrar Salida")
        }
    }
}
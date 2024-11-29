package com.example.apk_administration.ui.theme.NFC

import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Dialog
import com.example.apk_administration.ui.theme.common.SuccessMessage
import com.example.apk_administration.ui.theme.products.ProductModel
import com.example.apk_administration.ui.theme.products.ProductViewModel
import com.example.apk_administration.ui.theme.stock.StockMovementViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NFCReaderScreen(
    activity: Activity,
    apiService: NfcApiService,
    viewModel: ProductViewModel,
    stockViewModel: StockMovementViewModel
) {
    // Inicializa NFCManager y los estados necesarios
    val nfcManager = remember { NFCManager(activity, apiService) }
    val isNFCEnabled = remember { mutableStateOf(nfcManager.isNFCEnabled()) }
    val isReaderActive = remember { mutableStateOf(false) }
    val products by viewModel.productList.collectAsState()
    // Pasa los parámetros y funciones de activación/desactivación de lector al segundo NFCReaderScreen
    LaunchedEffect(Unit) {
        viewModel.loadProducts()
    }

    NFCReaderScreen(
        nfcManager = nfcManager,
        isNFCEnabled = isNFCEnabled.value,
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
        onUpdateNFCStatus = { isEnabled ->
            isNFCEnabled.value = isEnabled
        },
        products = products,
        viewModel = viewModel,
        stockViewModel
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NFCReaderScreen(
    nfcManager: NFCManager,
    isNFCEnabled: Boolean,
    isReaderActive: Boolean,
    onActivateReader: () -> Unit,
    onDeactivateReader: () -> Unit,
    onClearNFCId: () -> Unit, // Nueva función para limpiar el ID después de la lectura
    onUpdateNFCStatus: (Boolean) -> Unit,
    products: List<ProductModel>,
    viewModel: ProductViewModel,
    stockViewModel: StockMovementViewModel
) {
    val nfcId by nfcManager.nfcId.collectAsState()
    var showLoadingDialog by remember { mutableStateOf(false) }
    var showConfirmationDialog by remember { mutableStateOf(false) }
    var showUpdateDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val fechaDeHoy: LocalDate = LocalDate.now()
    var nfcTagId by remember { mutableStateOf<Int?>(null) }
    // Estado para controlar la visibilidad del mensaje
    var showSuccessMessage by remember { mutableStateOf(false) }
    var successMessage by remember { mutableStateOf("") }

    // Función para sincronizar NFC de forma asincrónica
    fun synchronizeNFC() {
        showLoadingDialog = true
        coroutineScope.launch {
            // Simulación de verificación de NFC con un retardo
            nfcManager.checkNFCStatus()
            kotlinx.coroutines.delay(2000) // Simulamos el tiempo de verificación

            // Actualizamos el estado de NFC de manera global usando la función de callback
            onUpdateNFCStatus(nfcManager.isNFCEnabled())

            // Ocultamos la ventana de carga
            showLoadingDialog = false
        }
    }

    // Función para obtener el ID del NFC basado en el idTag
    fun fetchNfcTagId() {
        coroutineScope.launch {
            nfcId?.let { idTag ->
                nfcTagId = nfcManager.getNfcIdByIdTag(idTag) // Llama a la función del manager
                showLoadingDialog = false

            }
        }
    }

    // Función para verificar y registrar el NFC
    fun checkAndRegisterNFC() {
        coroutineScope.launch {
            val (exists, isReusable) = nfcManager.isReusableNFC(nfcId ?: "") // Verificar si existe y es reutilizable

            if (exists && isReusable) {
                // Mostrar Dialog para confirmar el registro si la tarjeta es reutilizable
                showUpdateDialog = true
            } else if (exists) {
                // Mostrar Snackbar si la tarjeta ya está registrada y no es reutilizable
                snackbarHostState.showSnackbar("Tarjeta ya registrada y no reutilizable")
                showConfirmationDialog = false

            } else {
                // Mostrar Dialog para confirmar el registro si no existe
                showConfirmationDialog = true
            }
        }
    }

    SuccessMessage(
        message = successMessage,
        isVisible = showSuccessMessage,
        onDismiss = { showSuccessMessage = false }
    )
    // Función para registrar el NFC
    fun registerNFC(selectedProduct: ProductModel?) {
        coroutineScope.launch {
            // Crea el modelo NFC con los datos leídos; aquí product es null inicialmente porque no está asignado
            val newNfc = NfcModel(
                id = 0,
                idTag = nfcId ?: "", // Asegúrate de que coincida con el nombre correcto
                status = "Asignado",
                fechaAsignado = "$fechaDeHoy",
                product = selectedProduct?.id  // Se pasa null o el ID del producto si lo tienes
            )

            // Llama al servicio API para insertar el nuevo NFC
            val response = nfcManager.apiService.insertNfc(newNfc)

            if (response.isSuccessful) {
                viewModel.loadProducts()
                viewModel.refreshNfcs()
                showConfirmationDialog = false
                onClearNFCId()
                successMessage = "Tarjeta registrada con éxito" // Establecer el mensaje de éxito
                showSuccessMessage = true // Mostrar el mensaje
                snackbarHostState.showSnackbar("Tarjeta registrada con éxito")
            } else {
                snackbarHostState.showSnackbar("Error al registrar la tarjeta")
            }
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        SnackbarHost(hostState = snackbarHostState)
        if (!isNFCEnabled) {
            Text(
                text = "NFC no está habilitado",
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Botón para abrir la configuración de NFC
            Button(
                onClick = { nfcManager.goToNFCSettings() },
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = "Activar NFC")
            }
            // Botón para sincronizar NFC
            Button(
                onClick = { synchronizeNFC() },
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = "Sincronizar NFC")
            }

        } else {
            Text(
                text = "Presiona el botón para habilitar el lector NFC",
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Button(
                onClick = {
                    if (isReaderActive) onDeactivateReader() else onActivateReader()
                },
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = if (isReaderActive) "Desactivar Lector" else "Activar Lector")
            }

            if (isReaderActive) {
                Text(
                    text = "Acerca una tarjeta NFC",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 16.dp)
                )

                if (nfcId != null) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "ID de la tarjeta:", fontSize = 16.sp)
                            Text(
                                text = nfcId ?: "",
                                fontSize = 24.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    Button(
                        onClick = { onClearNFCId() }, // Limpia el ID para permitir una nueva lectura
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(text = "Realizar nueva lectura")
                    }
                }
            }
        }
        if (nfcId != null) {
            checkAndRegisterNFC() // Activar verificación solo si hay un nfcId válido
        }
    }

    // Ventana de carga mientras se sincroniza el NFC
    if (showLoadingDialog) {
        Dialog(onDismissRequest = { showLoadingDialog = false }) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.White, shape = RoundedCornerShape(8.dp))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }

    if (showConfirmationDialog && nfcId != null  ) {
        ConfirmationDialog(
            nfcId = nfcId,
            products = products,
            onConfirm = {selectedProduct ->
                registerNFC(selectedProduct) // Llama a la función para registrar en la API
            },
            onCancel = {
                onClearNFCId()
                showConfirmationDialog = false }
        )
    }
    if (showUpdateDialog && nfcId != null) {
        fetchNfcTagId()
        UpdateNFCDialog(
            nfcId = nfcId,
            products = products,
            onConfirm = { selectedProduct, quantity, description ->
                if (selectedProduct != null && nfcTagId != null) {
                    stockViewModel.registerProductWithNfc(
                        productId = selectedProduct.id,
                        nfcTagId = nfcTagId!!,
                        quantity = quantity,
                        description = description,
                        onSuccess = {
                            successMessage = "¡Etiqueta NFC actualizada exitosamente!"
                            showSuccessMessage = true
                        }
                    )
                }
            },
            onCancel = {
                onClearNFCId()
                showUpdateDialog = false
            }
        )
    }
}

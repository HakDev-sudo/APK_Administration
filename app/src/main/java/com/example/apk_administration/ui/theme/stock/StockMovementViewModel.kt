package com.example.apk_administration.ui.theme.stock
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apk_administration.ui.theme.NFC.NfcApiService
import com.example.apk_administration.ui.theme.products.ProductViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class StockMovementViewModel(
    private val stockMovementApiService: StockMovementApiService,
    private val nfcApiService: NfcApiService,
    private val viewModel: ProductViewModel,
) : ViewModel() {

    // Acceso al mapa de IDs a nombres
    val productIdToNameMap: StateFlow<Map<Int, String>> = viewModel.productIdToNameMap
    // Función para registrar una salida de producto
    fun registerProductExit(productId: Int, nfcTagId: Int, quantity: Int, description: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                // Crear el objeto StockMovementModel con tipo "salida"
                val stockMovement = StockMovementModel(
                    id = 0, // El ID será asignado por el backend
                    product = productId,
                    nfcTag = nfcTagId,
                    quantity = quantity,
                    movementType = "salida",
                    date = getCurrentDate(),
                    description = description
                )

                // Realizar el POST para crear el movimiento de stock
                val response: Response<StockMovementModel> = stockMovementApiService.insertStockMovement(stockMovement)

                if (response.isSuccessful) {
                    // Si el movimiento de salida fue exitoso, actualizar el estado de la etiqueta NFC
                    updateNfcStatusToUnassigned(nfcTagId)
                    onSuccess()
                } else {
                    // Manejo de error en el registro de movimiento
                    println("Error al registrar la salida de producto: ${response.errorBody()}")
                }
            } catch (e: Exception) {
                // Manejo de excepciones
                println("Exception: ${e.message}")
            }
        }
    }

    // Función para actualizar la etiqueta NFC como "sin asignar"
    private suspend fun updateNfcStatusToUnassigned(nfcTagId: Int) {
        try {
            // Obtener la etiqueta NFC actual para modificar sus datos
            val nfcResponse = nfcApiService.selectNfc(nfcTagId)
            if (nfcResponse.isSuccessful) {
                val nfc = nfcResponse.body()

                // Verificar que la etiqueta NFC fue obtenida
                if (nfc != null) {
                    val updatedNfc = nfc.copy(
                        status = "sin asignar",

                        product = null // Desvincular del producto
                    )

                    // Realizar el PUT para actualizar la etiqueta NFC
                    val updateResponse = nfcApiService.updateNfc(nfcTagId, updatedNfc)
                    if (updateResponse.isSuccessful) {
                        println("Etiqueta NFC actualizada correctamente.")
                    } else {
                        println("Error al actualizar la etiqueta NFC: ${updateResponse.errorBody()}")
                    }
                }
            } else {
                println("Error al obtener la etiqueta NFC: ${nfcResponse.errorBody()}")
            }
        } catch (e: Exception) {
            println("Exception: ${e.message}")
        }
    }

    // Función para obtener la fecha actual en formato ISO
    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        return dateFormat.format(Date())
    }

    // Función para registrar una entrada de producto
    fun registerProductWithNfc(productId: Int, nfcTagId: Int, quantity: Int, description: String, onSuccess: () -> Unit ) {
        viewModelScope.launch {
            try {
                // Crear el objeto StockMovementModel con tipo "salida"
                val stockMovement = StockMovementModel(
                    id = 0, // El ID será asignado por el backend
                    product = productId,
                    nfcTag = nfcTagId,
                    quantity = quantity,
                    movementType = "entrada",
                    date = getCurrentDate(),
                    description = description
                )

                // Realizar el POST para crear el movimiento de stock
                val response: Response<StockMovementModel> = stockMovementApiService.insertStockMovement(stockMovement)

                if (response.isSuccessful) {
                    // Actualizar la etiqueta NFC
                    updateNfcAfterStockMovement(nfcTagId, productId)
                    onSuccess()
                } else {
                    // Manejo de error en el registro de movimiento
                    println("Error al registrar la salida de producto: ${response.errorBody()}")
                }
            } catch (e: Exception) {
                // Manejo de excepciones
                println("Exception: ${e.message}")
            }
        }
    }

    fun getCurrentDateNFC(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(Date())
    }

    // Función para actualizar la etiqueta NFC tras registrar el stock
    private suspend fun updateNfcAfterStockMovement(nfcTagId: Int, productId: Int) {
        try {
            // Obtener la etiqueta NFC actual para modificar sus datos
            val nfcResponse = nfcApiService.selectNfc(nfcTagId)
            if (nfcResponse.isSuccessful) {
                val nfc = nfcResponse.body()

                // Verificar que la etiqueta NFC fue obtenida
                if (nfc != null) {
                    val updatedNfc = nfc.copy(
                        status = "asignado", // Cambiar el estado a "Asignado"
                        product = productId, // Asociar al nuevo producto
                        fechaAsignado = getCurrentDateNFC() // Actualizar la fecha de asignación
                    )

                    // Realizar el PUT para actualizar la etiqueta NFC
                    val updateResponse = nfcApiService.updateNfc(nfcTagId, updatedNfc)
                    if (updateResponse.isSuccessful) {
                        viewModel.loadProducts()
                        viewModel.refreshNfcs()

                        println("Etiqueta NFC actualizada correctamente.")
                    } else {
                        println("Error al actualizar la etiqueta NFC: ${updateResponse.errorBody()}")
                    }
                }
            } else {
                println("Error al obtener la etiqueta NFC: ${nfcResponse.errorBody()}")
            }
        } catch (e: Exception) {
            println("Exception: ${e.message}")
        }
    }
    //Regsitro
    fun getGroupedProductEntriesByDate(onSuccess: (Map<String, Map<Int, Int>>) -> Unit) {
        viewModelScope.launch {
            try {
                val response = stockMovementApiService.selectStockMovements()
                if (response.isNotEmpty()) {
                    // Filtrar solo los movimientos de tipo "entrada"
                    val entries = response.filter { it.movementType == "entrada" }

                    // Agrupar por fecha y producto, luego sumar las cantidades
                    val groupedByDateAndProduct = entries.groupBy { it.date.substring(0, 10) } // Agrupar por fecha (YYYY-MM-DD)
                        .mapValues { entry -> // Dentro de cada fecha...
                            entry.value.groupBy { it.product } // Agrupar por producto
                                .mapValues { productEntry -> // Dentro de cada producto...
                                    productEntry.value.sumOf { it.quantity } // Sumar las cantidades
                                }
                        }

                    onSuccess(groupedByDateAndProduct)
                } else {
                    println("No se encontraron movimientos de stock.")
                }
            } catch (e: Exception) {
                println("Error al agrupar entradas: ${e.message}")
            }
        }
    }
    fun getGroupedProductExitsByDate(onSuccess: (Map<String, Map<Int, Int>>) -> Unit) {
        viewModelScope.launch {
            try {
                val response = stockMovementApiService.selectStockMovements()
                if (response.isNotEmpty()) {
                    // Filtrar solo los movimientos de tipo "salida"
                    val exits = response.filter { it.movementType == "salida" }

                    // Agrupar por fecha y producto, luego sumar las cantidades
                    val groupedByDateAndProduct = exits.groupBy { it.date.substring(0, 10) } // Agrupar por fecha (YYYY-MM-DD)
                        .mapValues { entry -> // Dentro de cada fecha...
                            entry.value.groupBy { it.product } // Agrupar por producto
                                .mapValues { productEntry -> // Dentro de cada producto...
                                    productEntry.value.sumOf { it.quantity } // Sumar las cantidades
                                }
                        }

                    onSuccess(groupedByDateAndProduct)
                } else {
                    println("No se encontraron movimientos de stock.")
                }
            } catch (e: Exception) {
                println("Error al agrupar salidas: ${e.message}")
            }
        }
    }
}

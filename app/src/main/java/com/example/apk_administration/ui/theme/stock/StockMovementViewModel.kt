package com.example.apk_administration.ui.theme.stock
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apk_administration.ui.theme.NFC.NfcApiService
import kotlinx.coroutines.launch
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class StockMovementViewModel(
    private val stockMovementApiService: StockMovementApiService,
    private val nfcApiService: NfcApiService
) : ViewModel() {

    // Función para registrar una salida de producto
    fun registerProductExit(productId: Int, nfcTagId: Int, quantity: Int, description: String) {
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
                        fechaAsignado = getCurrentDate(),
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
}

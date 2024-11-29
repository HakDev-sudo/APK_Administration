package com.example.apk_administration.ui.theme.NFC

import android.app.Activity
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.MifareClassic
import android.nfc.tech.NfcA
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import android.content.Intent
import android.provider.Settings

class NFCManager(private val activity: Activity, val apiService: NfcApiService) {
    private var nfcAdapter: NfcAdapter? = null

    // Nueva propiedad para almacenar el estado de NFC
    private val _isNFCEnabled = MutableStateFlow(isNFCEnabled())
    val isNFCEnabled: StateFlow<Boolean> = _isNFCEnabled

    private val _nfcId = MutableStateFlow<String?>(null)
    val nfcId: StateFlow<String?> = _nfcId

    init {
        nfcAdapter = NfcAdapter.getDefaultAdapter(activity)
    }

    fun isNFCEnabled(): Boolean {
        return nfcAdapter?.isEnabled == true
    }

    fun checkNFCStatus() {
        // Actualiza el valor de _isNFCEnabled cada vez que se verifique el estado
        _isNFCEnabled.value = isNFCEnabled()
    }

    fun goToNFCSettings() {
        val intent = Intent(Settings.ACTION_NFC_SETTINGS)
        activity.startActivity(intent)
    }

    fun enableReaderMode() {
        nfcAdapter?.enableReaderMode(activity,
            { tag: Tag? ->
                tag?.let {
                    val idBytes = it.id
                    val id = bytesToHexString(idBytes)
                    _nfcId.value = id
                }
            },
            NfcAdapter.FLAG_READER_NFC_A or
                    NfcAdapter.FLAG_READER_NFC_B or
                    NfcAdapter.FLAG_READER_NFC_F or
                    NfcAdapter.FLAG_READER_NFC_V,
            null)
    }

    fun disableReaderMode() {
        nfcAdapter?.disableReaderMode(activity)
    }

    fun clearNFCId() {
        _nfcId.value = null // Limpia el ID para permitir una nueva lectura
    }

    private fun bytesToHexString(bytes: ByteArray): String {
        val sb = StringBuilder()
        for (b in bytes) {
            sb.append(String.format("%02x", b))
        }
        return sb.toString()
    }

    // Nueva función para obtener todas las etiquetas NFC y verificar si existe el idTag
    suspend fun checkIfNFCExists(idTag: String): Boolean {
        return try {
            val nfcList = apiService.selectNfcs() // Llama a la API para obtener todas las etiquetas
            nfcList.any { it.idTag == idTag } // Verifica si el idTag ya existe
        } catch (e: Exception) {
            false // Retorna falso si ocurre un error
        }
    }

    // Nueva función para verificar si el NFC está registrado y si tiene el campo product vacío
    suspend fun isReusableNFC(idTag: String): Pair<Boolean, Boolean> {
        return try {
            val nfcList = apiService.selectNfcs() // Llama a la API para obtener todas las etiquetas
            val matchingTag = nfcList.find { it.idTag == idTag } // Busca la etiqueta por idTag
            if (matchingTag != null) {
                // Retorna true si la etiqueta existe y si el campo product es null
                Pair(true, matchingTag.product == null)
            } else {
                // Si no existe, retorna false
                Pair(false, false)
            }
        } catch (e: Exception) {
            Pair(false, false) // Retorna falso si ocurre un error
        }
    }
    // Nueva función para obtener el ID de la etiqueta NFC basado en el idTag
    suspend fun getNfcIdByIdTag(idTag: String): Int? {
        return try {
            // Llama a la API para obtener todas las etiquetas
            val nfcList = apiService.selectNfcs()
            // Busca la etiqueta que coincida con el idTag
            val matchingTag = nfcList.find { it.idTag == idTag }
            // Retorna el id de la etiqueta, o null si no se encuentra
            matchingTag?.id
        } catch (e: Exception) {
            println("Error al buscar el ID por idTag: ${e.message}")
            null // Retorna null si ocurre un error
        }
    }


}
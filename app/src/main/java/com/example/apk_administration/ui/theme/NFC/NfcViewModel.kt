package com.example.apk_administration.ui.theme.NFC

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NfcViewModel(private val apiService: NfcApiService) : ViewModel() {
    private val _nfcList = MutableStateFlow<List<NfcModel>>(emptyList())
    val nfcList: StateFlow<List<NfcModel>> = _nfcList

    val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    init {
        loadNfcTags()
    }

    fun loadNfcTags() {
        viewModelScope.launch {
            try {
                _nfcList.value = apiService.selectNfcs()
            } catch (e: Exception) {
                _errorMessage.value = "Error al cargar las etiquetas NFC"
            }
        }
    }

    fun deleteNfcTag(id: Int) {
        viewModelScope.launch {
            try {
                val nfc = _nfcList.value.find { it.id == id }
                if (nfc?.product != null) {
                    _errorMessage.value = "No se puede eliminar la etiqueta. Está asociada a un producto."
                    return@launch
                }

                val response = apiService.deleteNfc(id)
                if (response.isSuccessful) {
                    _nfcList.value = _nfcList.value.filter { it.id != id }
                } else {
                    _errorMessage.value = "No se pudo eliminar la etiqueta"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error al eliminar la etiqueta"
            }
        }
    }

}

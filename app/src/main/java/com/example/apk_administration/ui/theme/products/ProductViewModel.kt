package com.example.apk_administration.ui.theme.products

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apk_administration.ui.theme.Category.CategoryModel
import com.example.apk_administration.ui.theme.NFC.NfcModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProductViewModel(private val apiService: ProductoApiService) : ViewModel() {

    // Lista de productos usando el nuevo modelo ProductModel
    private val _productList = MutableStateFlow<List<ProductModel>>(emptyList())
    val productList: StateFlow<List<ProductModel>> = _productList
    // Lista de categorías para almacenar los datos cargados desde el servicio
    private val _categoryList = MutableStateFlow<List<CategoryModel>>(emptyList())
    val categoryList: StateFlow<List<CategoryModel>> = _categoryList
    // Lista de etiquetas NFC
    private val _nfcList = MutableStateFlow<List<NfcModel>>(emptyList())
    val nfcList: StateFlow<List<NfcModel>> = _nfcList
    // Lista filtrada de productos (para búsquedas)
    private val _filteredProductList = MutableStateFlow<List<ProductModel>>(emptyList())
    val filteredProductList: StateFlow<List<ProductModel>> = _filteredProductList
    //Manejo de errores
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage
    // Mapa de IDs a nombres
    val productIdToNameMap: StateFlow<Map<Int, String>> = _productList.map { productList ->
        productList.associate { it.id to it.name }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyMap())

    init {
        loadProducts()
        loadCategories()
        loadNfcs()
    }

    public fun loadProducts() {
        viewModelScope.launch {
            try {
                val products = apiService.selectProductos() // Obtiene todos los productos de la API
                _productList.value = products
                _filteredProductList.value = products
            } catch (e: Exception) {
                // Manejo de errores
                Log.e("ProductViewModel", "Error al cargar productos: ${e.message}")
            }
        }
    }
    private fun loadCategories() {
        viewModelScope.launch {
            try {
                val categories = apiService.selectCategories() // Obtiene las categorías de la API
                _categoryList.value = categories // Asigna las categorías obtenidas
            } catch (e: Exception) {
                Log.e("ProductViewModel", "Error al cargar categorías: ${e.message}")
            }
        }
    }
    private fun loadNfcs() {
        viewModelScope.launch {
            try {
                val nfcs = apiService.selectnfcs() // Obtiene las etiquetas NFC de la API
                _nfcList.value = nfcs // Asigna las etiquetas NFC obtenidas
            } catch (e: Exception) {
                Log.e("ProductViewModel", "Error al cargar etiquetas NFC: ${e.message}")
            }
        }
    }
    // Método para contar los productos actuales
    fun getProductCount(): Int {
        return _productList.value.size
    }

    fun refreshProducts() {
        loadProducts() // Vuelve a cargar los productos desde la API
    }
    // Método para refrescar las categorías
    fun refreshCategories() {
        loadCategories() // Vuelve a cargar las categorías desde la API
    }

    // Método para refrescar las etiquetas NFC
    fun refreshNfcs() {
        loadNfcs() // Vuelve a cargar las etiquetas NFC desde la API
    }
    fun getNfcTagsByProduct(productId: Int): List<NfcModel> {
        return _nfcList.value.filter { it.product == productId }
    }

    // Método para buscar productos por nombre
    fun searchProducts(query: String) {
        viewModelScope.launch {
            _filteredProductList.value = if (query.isNotEmpty()) {
                _productList.value.filter { it.name.contains(query, ignoreCase = true) }
            } else {
                _productList.value // Devuelve todos los productos si la búsqueda está vacía
            }
        }
    }


}

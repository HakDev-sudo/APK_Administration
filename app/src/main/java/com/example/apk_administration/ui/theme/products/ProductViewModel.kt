package com.example.apk_administration.ui.theme.products

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductViewModel(private val apiService: ProductoApiServiceC) : ViewModel() {
    private val _productList = MutableStateFlow<List<ProductoModelGet>>(emptyList())
    val productList: StateFlow<List<ProductoModelGet>> = _productList

    init {
        loadProducts()
    }

    private fun loadProducts() {
        viewModelScope.launch {
            try {
                val products = apiService.selectProductos() // Obtiene todos los productos de la API
                _productList.value = products
            } catch (e: Exception) {
                // Manejo de errores
            }
        }
    }

    fun refreshProducts() {
        loadProducts() // Vuelve a cargar los productos desde la API
    }
}

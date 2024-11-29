package com.example.apk_administration.ui.theme.Category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apk_administration.ui.theme.products.ProductModel
import com.example.apk_administration.ui.theme.products.ProductoApiService
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CategoryViewModel(
    private val categoryApiService: CategoryApiService,
    private val productApiService: ProductoApiService
) : ViewModel() {

    private val _categories = MutableStateFlow<List<CategoryModel>>(emptyList())
    val categories: StateFlow<List<CategoryModel>> = _categories

    private val _productsByCategory = MutableStateFlow<List<ProductModel>>(emptyList())
    val productsByCategory: StateFlow<List<ProductModel>> = _productsByCategory

    private val _selectedCategory = MutableStateFlow<CategoryModel?>(null)
    val selectedCategory: StateFlow<CategoryModel?> = _selectedCategory

    fun loadCategories() {
        viewModelScope.launch {
            try {
                val categories = categoryApiService.selectCategories()
                _categories.value = categories
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun loadProductsByCategory(categoryId: Int) {
        viewModelScope.launch {
            try {
                val products = productApiService.selectProductos()
                _productsByCategory.value = products.filter { it.category == categoryId }
                // Establecer la categoría seleccionada
                val category = _categories.value.find { it.id == categoryId }
                _selectedCategory.value = category
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun selectCategory(category: CategoryModel) {
        _selectedCategory.value = category
    }
    fun getCategoryName(categoryId: Int): String {
        val category = _categories.value.find { it.id == categoryId }
        println("Buscando categoría con ID: $categoryId")
        println("Categoría encontrada: $category")
        return category?.name ?: "Categoría no encontrada"
    }


}


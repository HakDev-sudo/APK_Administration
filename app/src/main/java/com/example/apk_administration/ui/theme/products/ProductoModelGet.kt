package com.example.apk_administration.ui.theme.products


import com.example.apk_administration.ui.theme.Category.CategoryModel
import com.example.apk_administration.ui.theme.NFC.NfcModel
import com.google.gson.annotations.SerializedName

data class ProductoModelGet (
    @SerializedName("id")
    var id: Int,
    @SerializedName("name")
    var name: String,
    @SerializedName("img")
    var img: String?,
    @SerializedName("price")
    var price: Double,
    @SerializedName("description")
    var description: String,
    @SerializedName("category")
    var category: CategoryModel, // Recibe el objeto completo de la categoría
    @SerializedName("stock")
    var stock: Int,
    @SerializedName("idNFC")
    var idNFC: NfcModel?  // Puede ser nulo si no tiene NFC asociado
)
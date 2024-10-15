package com.example.apk_administration.ui.theme.products

import com.google.gson.annotations.SerializedName

data class ProductoModelPost (
    @SerializedName("name")
    var name: String,
    @SerializedName("img")
    var img: String?,
    @SerializedName("price")
    var price: Double,
    @SerializedName("description")
    var description: String,
    @SerializedName("category_id")
    var categoryId: Int, // Solo el ID de la categoría
    @SerializedName("stock")
    var stock: Int,
    @SerializedName("id_nfc")
    var idNfc: Int? // Puede ser nulo si no tiene NFC asociado
)

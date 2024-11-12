package com.example.apk_administration.ui.theme.products

import com.google.gson.annotations.SerializedName

data class ProductModel(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("img")
    val img: String?,
    @SerializedName("price")
    val price: Double,
    @SerializedName("description")
    val description: String,
    @SerializedName("stock")
    val stock: Int? = null, // Este campo recibirá el stock calculado desde la API
    @SerializedName("category")
    val category: Int,

)

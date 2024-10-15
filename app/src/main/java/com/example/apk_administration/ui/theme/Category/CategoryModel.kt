package com.example.apk_administration.ui.theme.Category

import com.google.gson.annotations.SerializedName

data class CategoryModel (
    @SerializedName("id")
    var id: Int,
    @SerializedName("name")
    var name: String,
    @SerializedName("img")
    var img: String? // La imagen puede ser nula
)
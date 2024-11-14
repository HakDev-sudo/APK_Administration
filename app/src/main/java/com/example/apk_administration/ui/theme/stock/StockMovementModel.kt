package com.example.apk_administration.ui.theme.stock

import com.google.gson.annotations.SerializedName

data class StockMovementModel (
    @SerializedName("id")
    var id: Int,

    @SerializedName("product")
    var product: Int, // ID del producto asociado

    @SerializedName("NFC_tag")
    var nfcTag: Int?, // ID de la etiqueta NFC asociada, puede ser null

    @SerializedName("quantity")
    var quantity: Int, // Cantidad de unidades afectadas

    @SerializedName("movement_type")
    var movementType: String, // "entrada" o "salida"

    @SerializedName("date")
    var date: String, // Fecha de movimiento en formato ISO

    @SerializedName("description")
    var description: String? // Descripción del movimiento, opcional
)

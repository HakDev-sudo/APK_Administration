package com.example.apk_administration.ui.theme.NFC

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class NfcModel (
    @SerializedName("id")
    var id: Int,
    @SerializedName("id_tag")
    var idTag: String,
    @SerializedName("status")
    var status: String,
    @SerializedName("fecha_asignado")
    var fechaAsignado: String,
    @SerializedName("product")
    @Expose
    var product: Int?=null // Representa el ID del producto asociado, puede ser null si no está asignado
)
package com.example.apk_administration.ui.theme.NFC

import com.google.gson.annotations.SerializedName

data class NfcModel (
    @SerializedName("id")
    var id: Int,
    @SerializedName("id_tag")
    var id_tag: String,
    @SerializedName("status")
    var status: String,
    @SerializedName("fecha_asignado")
    var fecha_asignado: String
)
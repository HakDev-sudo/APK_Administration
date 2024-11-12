package com.example.apk_administration.ui.theme.NFC

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface NfcApiService {
    @GET("api/nfc") // Ajusta la ruta para obtener todos los NFC
    suspend fun selectNfcs(): List<NfcModel>

    @GET("api/nfc/{id}") // Ajusta la ruta para obtener un NFC por ID
    suspend fun selectNfc(@Path("id") id: Int): Response<NfcModel>

    @Headers("Content-Type: application/json")
    @POST("api/nfc") // Ajusta la ruta para crear un nuevo NFC
    suspend fun insertNfc(@Body nfc: NfcModel): Response<NfcModel>

    @PUT("api/nfc/{id}") // Ajusta la ruta para actualizar un NFC
    suspend fun updateNfc(@Path("id") id: Int, @Body nfc: NfcModel): Response<NfcModel>

    @DELETE("api/nfc/{id}") // Ajusta la ruta para eliminar un NFC
    suspend fun deleteNfc(@Path("id") id: Int): Response<NfcModel>
}
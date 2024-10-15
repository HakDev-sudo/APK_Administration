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
    @GET("nfc")
    suspend fun selectNfcs(): List<NfcModel>

    @GET("nfc/{id}")
    suspend fun selectNfc(@Path("id") id: String): Response<NfcModel>

    @Headers("Content-Type: application/json")
    @POST("nfc")
    suspend fun insertNfc(@Body nfc: NfcModel): Response<NfcModel>

    @PUT("nfc/{id}")
    suspend fun updateNfc(@Path("id") id: String, @Body nfc: NfcModel): Response<NfcModel>

    @DELETE("nfc/{id}")
    suspend fun deleteNfc(@Path("id") id: String): Response<NfcModel>
}
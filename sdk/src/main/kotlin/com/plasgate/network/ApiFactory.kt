package com.plasgate.network

import com.plasgate.model.VerificationResponse
import com.plasgate.model.CheckUserRequest
import com.plasgate.model.CheckUserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Header
import retrofit2.http.Body

interface ApiService {

    @GET("authenticate")
    fun requestVerification(
        @Query("private_key") privateKey: String,
        @Query("scope") scope: String,
        @Query("redirect_uri") redirectUri: String,
        @Query("phone_number") phoneNumber: String
    ): Call<VerificationResponse>

    @POST("check-info")
    fun requestCheckInfo(
        @Header("X-Secret") secretKey: String, // ✅ Correct casing
        @Header("Content-Type") contentType: String = "application/json", // ✅ Explicit content type
        @Query("private_key") privateKey: String,
        @Body request: CheckUserRequest
    ): Call<CheckUserResponse>
}


object ApiFactory {

    fun createApi(retrofit: retrofit2.Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}
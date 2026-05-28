package com.example.myapplication.api

import com.example.myapplication.util.GlobalData
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetroFitClient {
//  const val BASE_URL = "http://10.0.2.2:5000/"
//  const val BASE_URL = "http://192.168.1.7:5079/"
    const val BASE_URL = "http://10.0.2.2:5079/"

    private val client = OkHttpClient.Builder().addInterceptor { chain ->
        val requestBuilder = chain.request().newBuilder()

        val token = GlobalData.tokenUser

        if (!token.isNullOrEmpty()) {
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }

        chain.proceed(requestBuilder.build())
    }.build()

    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client) // Pasang Satpamnya di sini
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
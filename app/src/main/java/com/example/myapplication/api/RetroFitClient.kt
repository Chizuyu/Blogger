package com.example.myapplication.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object RetroFitClient {
//  const val BASE_URL = "http://10.0.2.2:5000/"
//  const val BASE_URL = "http://192.168.1.10:5000/"
    const val BASE_URL = "http://192.168.1.7:5079/"

    val instance: ApiService by lazy{
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

}
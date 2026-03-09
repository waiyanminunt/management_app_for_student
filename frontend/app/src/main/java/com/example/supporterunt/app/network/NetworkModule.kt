package com.example.supporterunt.app.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkModule {
    // 10.0.2.2 is the special IP for Android emulator to access host loopback
    // Using 8080 as the backend port
    private const val BASE_URL = "http://10.0.2.2:8080/"

    val api: SupporterApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SupporterApi::class.java)
    }
}

package com.example.restaurante

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitRecipeClient {
    private const val BASE_URL = "https://6990a6036279728b0152f096.mockapi.io/api/v1/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: APIService by lazy {
        retrofit.create(APIService::class.java)
    }
}

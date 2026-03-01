package com.example.restaurante

import retrofit2.http.GET

interface APIService {
    //https://6990a6036279728b0152f096.mockapi.io/api/v1/recipes
    @GET("recipes")
    suspend fun getRecipes(): List<RecipeResponse>

}
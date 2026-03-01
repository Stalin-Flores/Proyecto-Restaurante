package com.example.restaurante

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class FavoritesManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("favorites_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun toggleFavorite(recipe: RecipeResponse) {
        val favorites = getFavorites().toMutableList()
        val existingIndex = favorites.indexOfFirst { it.id == recipe.id }
        
        if (existingIndex != -1) {
            favorites.removeAt(existingIndex)
        } else {
            favorites.add(recipe)
        }
        
        saveFavorites(favorites)
    }

    fun isFavorite(recipeId: Int): Boolean {
        return getFavorites().any { it.id == recipeId }
    }

    fun getFavorites(): List<RecipeResponse> {
        val json = sharedPreferences.getString("favorites_list", null) ?: return emptyList()
        val type = object : TypeToken<List<RecipeResponse>>() {}.type
        return gson.fromJson(json, type)
    }

    private fun saveFavorites(favorites: List<RecipeResponse>) {
        val json = gson.toJson(favorites)
        sharedPreferences.edit().putString("favorites_list", json).apply()
    }
}

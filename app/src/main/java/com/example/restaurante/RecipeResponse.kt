package com.example.restaurante

import java.io.Serializable

data class RecipeResponse(
    val id: Int,
    val category: String,
    val time: String,
    val title: String,
    val calories: String,
    val imageName: String, // Cambiado de Int a String para usar nombres de recursos
    val description: String = "",
    val stats: RecipeStatsResponse, // Nuevas estadísticas
    val ingredients: List<IngredientesResponse> = emptyList(),
    val steps: List<StepPreparationResponse> = emptyList()
): Serializable

data class RecipeStatsResponse(
    val protein: String,
    val carbs: String,
    val fat: String
): Serializable

data class StepPreparationResponse(
    val id: Int,
    val title: String,
    val description: String,
): Serializable

data class IngredientesResponse(
    val id: Int,
    val title: String,
    val description: String,
): Serializable

data class UserResponse(
    val id: Int,
    val name: String,
    val email: String,
)

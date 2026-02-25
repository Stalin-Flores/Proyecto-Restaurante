package com.example.restaurante
data class RecipeResponse(
    val id: Int,
    val category: String,
    val time: String,
    val title: String,
    val calories: String,
    val imageRes: Int,
    val description: String = "",
    val ingredients: List<IngredientesResponse> = emptyList(),
    val steps: List<StepPreparationResponse> = emptyList()
)

data class StepPreparationResponse(
    val id: Int,
    val title: String,
    val description: String,
)

data class IngredientesResponse(
    val id: Int,
    val title: String,
    val description: String,
)

data class UserResponse(
    val id: Int,
    val name: String,
    val email: String,
)

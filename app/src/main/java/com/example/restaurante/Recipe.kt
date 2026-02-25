package com.example.restaurante

data class Recipe(
    val id: Int,
    val category: String,
    val time: String,
    val title: String,
    val calories: String,
    val imageRes: Int
)

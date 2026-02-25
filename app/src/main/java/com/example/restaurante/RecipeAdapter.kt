package com.example.restaurante

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurante.databinding.ItemRecipeLayoutBinding

class RecipeAdapter(
    private val recipes: List<RecipeResponse>,
    private val onItemClick: (RecipeResponse) -> Unit
) : RecyclerView.Adapter<ItemRecipeViewHolder>() {

    //Cantidad de celdas que se pintaran
    override fun getItemCount(): Int {
        return recipes.size
    }

    //Instancia de la celda que se utilizara
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemRecipeViewHolder {
        val binding = ItemRecipeLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ItemRecipeViewHolder(binding)
    }

    //Configuracion de las celdas
    override fun onBindViewHolder(
        holder: ItemRecipeViewHolder,
        position: Int
    ) {
        val recipe = recipes[position]
        holder.binding.apply {
            cat1.text = recipe.category
            time.text = recipe.time
            recipetitle.text = recipe.title
            calorie.text = recipe.calories
            imgrecipe.setImageResource(recipe.imageRes)
            
            root.setOnClickListener {
                onItemClick(recipe)
            }
        }
    }
}

package com.example.restaurante

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurante.databinding.ItemRecipeLayoutBinding

class RecipeAdapter(
    private var recipes: List<RecipeResponse>,
    private val onItemClick: (RecipeResponse) -> Unit
) : RecyclerView.Adapter<ItemRecipeViewHolder>() {

    override fun getItemCount(): Int = recipes.size

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

    override fun onBindViewHolder(
        holder: ItemRecipeViewHolder,
        position: Int
    ) {
        val recipe = recipes[position]
        val context = holder.itemView.context
        
        holder.binding.apply {
            cat1.text = recipe.category
            time.text = recipe.time
            recipetitle.text = recipe.title
            calorie.text = recipe.calories
            tvServings.text = recipe.servings

            //Maneja las img del card
            imageRecipe.loadRecipeImage(recipe.imageName)

            root.setOnClickListener {
                onItemClick(recipe)
            }
        }
    }

    fun updateRecipes(newRecipes: List<RecipeResponse>) {
        recipes = newRecipes
        notifyDataSetChanged()
    }
}

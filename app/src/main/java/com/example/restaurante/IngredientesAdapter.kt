package com.example.restaurante

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurante.databinding.ItemIngredientesLayoutBinding

class IngredientesAdapter(private val listIngredients: List<IngredientesResponse>): RecyclerView.Adapter<ItemIngredientesViewHolder>() {

    override fun getItemCount(): Int {
        return listIngredients.size
    }
    
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemIngredientesViewHolder {
        val binding = ItemIngredientesLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ItemIngredientesViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ItemIngredientesViewHolder,
        position: Int
    ) {
        val ingredient = listIngredients[position]
        holder.binding.apply {
            tvIngredientName.text = "${ingredient.description} ${ingredient.title}"
        }
    }
}

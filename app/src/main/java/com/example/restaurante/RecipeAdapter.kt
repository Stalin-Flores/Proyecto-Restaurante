package com.example.restaurante

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurante.databinding.ItemRecipeLayoutBinding

class RecipeAdapter: RecyclerView.Adapter<ItemRecipeViewHolder>() {

    //Cantidad de celdas que se utilizaran (pintaran)
    override fun getItemCount(): Int {
        return 3
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
        holder.binding.cat1.text = "DESAYUNO"
        holder.binding.time.text = "25 min"
        holder.binding.recipetitle.text = "Pollo Grillado con Ensalada"
        holder.binding.calorie.text = "🔥 450 kcal"
        holder.binding.imgrecipe.setImageResource(android.R.drawable.ic_menu_agenda)
    }
}
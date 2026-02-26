package com.example.restaurante

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurante.databinding.ItemStepPreparationLayoutBinding

class StepPreparationAdapter(private val listStepsPreparation: List<StepPreparationResponse>): RecyclerView.Adapter<ItemStepPreparationViewHolder>() {

    override fun getItemCount(): Int {
        return listStepsPreparation.size
    }
    
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemStepPreparationViewHolder {
        val binding = ItemStepPreparationLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ItemStepPreparationViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ItemStepPreparationViewHolder,
        position: Int
    ) {
        val stepsPreparation = listStepsPreparation[position]
        holder.binding.apply {
            //Enumera los pasos par la preparacion
            tvStepNumber.text = (position + 1).toString()
            tvInstructionText.text = stepsPreparation.description
        }
    }
}

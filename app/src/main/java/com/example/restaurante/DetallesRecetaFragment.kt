package com.example.restaurante

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.restaurante.databinding.FragmentDetallesRecetaBinding

class DetallesRecetaFragment : Fragment() {

    private var _binding: FragmentDetallesRecetaBinding? = null
    private val binding get() = _binding!!
    private val args: DetallesRecetaFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetallesRecetaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recipe = args.recipe

        binding.apply {
            textrecipeTitle.text = recipe.title
            tvServingsDetail.text = recipe.servings
            textrecipeDescription.text = recipe.description
            tvCaloriesDetail.text = recipe.calories
            
            // Estadísticas
            tvProtein.text = recipe.stats.protein
            tvCarbs.text = recipe.stats.carbs
            tvFat.text = recipe.stats.fat

            // Carga la imagen usando la misma lógica que el adaptador
            imgRecipeDetail.loadRecipeImage(recipe.imageName)

            rvIngredients.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = IngredientesAdapter(recipe.ingredients)
            }

            rvSteps.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = StepPreparationAdapter(recipe.steps)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

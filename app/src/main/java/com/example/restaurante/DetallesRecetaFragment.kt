package com.example.restaurante

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.restaurante.databinding.FragmentDetallesRecetaBinding

class DetallesRecetaFragment : Fragment() {

    private var _binding: FragmentDetallesRecetaBinding? = null
    private val binding get() = _binding!!
    private val args: DetallesRecetaFragmentArgs by navArgs()
    private lateinit var favoritesManager: FavoritesManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetallesRecetaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoritesManager = FavoritesManager(requireContext())
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

            // Configurar botón de favoritos
            updateFavoriteIcon(recipe.id)
            fabFavorite.setOnClickListener {
                favoritesManager.toggleFavorite(recipe)
                updateFavoriteIcon(recipe.id)
            }

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

    private fun updateFavoriteIcon(recipeId: Int) {
        val isFav = favoritesManager.isFavorite(recipeId)
        val color = if (isFav) {
            ContextCompat.getColor(requireContext(), android.R.color.holo_red_dark)
        } else {
            ContextCompat.getColor(requireContext(), R.color.brand_green)
        }
        binding.fabFavorite.setColorFilter(color)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

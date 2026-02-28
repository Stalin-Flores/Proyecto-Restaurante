package com.example.restaurante

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.restaurante.databinding.FragmentRecipeBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecipeFragment : Fragment() {
    private var _binding: FragmentRecipeBinding? = null
    private val binding get() = _binding!!
    private lateinit var recipeAdapter: RecipeAdapter
    private val args: RecipeFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Pasa datos del email a la vista
        val emailRecibido = args.userEmail
        binding.tvGreeting.text = "Hola Bienvenido\n$emailRecibido"

        // Inicializa el adaptador vacío
        recipeAdapter = RecipeAdapter(emptyList()) { recipe ->
            val action =
                RecipeFragmentDirections.actionRecipeFragmentToDetallesRecetaFragment(recipe)
            findNavController().navigate(action)
        }

        binding.recycleViewRecipe.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = recipeAdapter
        }

        loadRecipesFromServiceAPI()
    }

    private fun loadRecipesFromServiceAPI() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                // Realizar la llamada en el hilo de IO
                val recipes = withContext(Dispatchers.IO) {
                    RetrofitRecipeClient.apiService.getRecipes()
                }
                if (recipes.isNotEmpty()) {
                    recipeAdapter.updateRecipes(recipes)
                    Snackbar.make(binding.root, "Se cargaron ${recipes.size} recetas", Snackbar.LENGTH_LONG)
                        .show()
                } else {
                    Snackbar.make(binding.root, "No se encontraron recetas", Snackbar.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Snackbar.make(binding.root, "Error al conectar con el servidor: ${e.message}", Snackbar.LENGTH_LONG)
                    .setAction("REINTENTAR") { loadRecipesFromServiceAPI() }
                    .show()
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

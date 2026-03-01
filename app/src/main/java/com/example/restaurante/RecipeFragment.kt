package com.example.restaurante

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class RecipeFragment : Fragment() {
    private var _binding: FragmentRecipeBinding? = null
    private val binding get() = _binding!!
    private lateinit var recipeAdapter: RecipeAdapter
    private val args: RecipeFragmentArgs by navArgs()
    private var allRecipes: List<RecipeResponse> = emptyList()

    companion object {
        private const val PREFS_NAME = "RecipeCache"
        private const val KEY_RECIPES = "cached_recipes"
        private const val KEY_CACHE_TIME = "cache_timestamp"
        private const val CACHE_DURATION = 5 * 60 * 1000L // 5 minutos
    }

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

        setupSearchBar()
        loadRecipesFromServiceAPI()
    }

    private fun setupSearchBar() {
        binding.etSearchRecipes.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterRecipes(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun filterRecipes(query: String) {
        if (query.isEmpty()) {
            recipeAdapter.updateRecipes(allRecipes)
        } else {
            val filteredRecipes = allRecipes.filter { recipe ->
                recipe.title.contains(query, ignoreCase = true) ||
                recipe.description.contains(query, ignoreCase = true)
            }
            recipeAdapter.updateRecipes(filteredRecipes)
        }
    }

    private fun loadRecipesFromServiceAPI() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                // Primero intentar cargar desde caché
                val cachedRecipes = loadFromCache()
                if (cachedRecipes != null) {
                    allRecipes = cachedRecipes
                    recipeAdapter.updateRecipes(cachedRecipes)
                    Snackbar.make(binding.root, "Recetas cargadas desde caché (${cachedRecipes.size})", Snackbar.LENGTH_SHORT)
                        .show()
                    return@launch
                }

                // Si no hay caché, cargar desde API
                val recipes = withContext(Dispatchers.IO) {
                    RetrofitRecipeClient.apiService.getRecipes()
                }

                if (recipes.isNotEmpty()) {
                    allRecipes = recipes
                    recipeAdapter.updateRecipes(recipes)
                    saveToCache(recipes)
                    Snackbar.make(binding.root, "Se cargaron ${recipes.size} recetas", Snackbar.LENGTH_LONG)
                        .show()
                } else {
                    Snackbar.make(binding.root, "No se encontraron recetas", Snackbar.LENGTH_SHORT).show()
                }
            } catch (e: HttpException) {
                when (e.code()) {
                    429 -> {
                        // Error de rate limit
                        Snackbar.make(
                            binding.root,
                            "Límite de solicitudes alcanzado. Reintentando en 5 segundos...",
                            Snackbar.LENGTH_LONG
                        ).show()

                        // Intentar cargar datos de ejemplo después de esperar
                        delay(5000)
                        loadMockRecipes()
                    }
                    else -> {
                        Snackbar.make(
                            binding.root,
                            "Error del servidor (${e.code()}): ${e.message()}",
                            Snackbar.LENGTH_LONG
                        ).setAction("REINTENTAR") { loadRecipesFromServiceAPI() }
                        .show()
                    }
                }
            } catch (e: Exception) {
                Snackbar.make(
                    binding.root,
                    "Error de conexión. Cargando recetas de ejemplo...",
                    Snackbar.LENGTH_LONG
                ).show()
                loadMockRecipes()
            }
        }
    }

    private fun saveToCache(recipes: List<RecipeResponse>) {
        try {
            val prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            val gson = Gson()
            val json = gson.toJson(recipes)
            prefs.edit().apply {
                putString(KEY_RECIPES, json)
                putLong(KEY_CACHE_TIME, System.currentTimeMillis())
                apply()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun loadFromCache(): List<RecipeResponse>? {
        try {
            val prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            val cacheTime = prefs.getLong(KEY_CACHE_TIME, 0)
            val currentTime = System.currentTimeMillis()

            // Verificar si el caché es válido (no ha expirado)
            if (currentTime - cacheTime > CACHE_DURATION) {
                return null
            }

            val json = prefs.getString(KEY_RECIPES, null) ?: return null
            val gson = Gson()
            val type = object : TypeToken<List<RecipeResponse>>() {}.type
            return gson.fromJson(json, type)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    private fun loadMockRecipes() {
        // Cargar recetas de ejemplo si falla la API
        val mockRecipes = listOf(
            RecipeResponse(
                id = 1,
                category = "Desayuno",
                time = "20 min",
                servings = "2 porciones",
                title = "Panqueques de Avena y Plátano",
                calories = "380 kcal",
                imageName = "panqueques",
                description = "Deliciosos panqueques saludables hechos con avena y plátano",
                stats = RecipeStatsResponse(protein = "12g", carbs = "45g", fat = "8g"),
                ingredients = listOf(
                    IngredientesResponse(1, "1 taza de avena"),
                    IngredientesResponse(2, "2 plátanos maduros"),
                    IngredientesResponse(3, "2 huevos"),
                    IngredientesResponse(4, "1/2 taza de leche")
                ),
                steps = listOf(
                    StepPreparationResponse(1, "Mezclar todos los ingredientes en un bowl"),
                    StepPreparationResponse(2, "Calentar una sartén antiadherente"),
                    StepPreparationResponse(3, "Verter la mezcla y cocinar por ambos lados")
                )
            ),
            RecipeResponse(
                id = 2,
                category = "Cena",
                time = "30 min",
                servings = "2 porciones",
                title = "Ensalada César con Pollo Grillé",
                calories = "420 kcal",
                imageName = "ensalada_cesar",
                description = "Ensalada fresca con pechuga de pollo a la parrilla",
                stats = RecipeStatsResponse(protein = "35g", carbs = "15g", fat = "25g"),
                ingredients = listOf(
                    IngredientesResponse(1, "2 pechugas de pollo"),
                    IngredientesResponse(2, "Lechuga romana"),
                    IngredientesResponse(3, "Queso parmesano"),
                    IngredientesResponse(4, "Aderezo césar ligero")
                ),
                steps = listOf(
                    StepPreparationResponse(1, "Sazonar y grillar el pollo"),
                    StepPreparationResponse(2, "Lavar y cortar la lechuga"),
                    StepPreparationResponse(3, "Mezclar todo con el aderezo")
                )
            )
        )

        allRecipes = mockRecipes
        recipeAdapter.updateRecipes(mockRecipes)
        Snackbar.make(
            binding.root,
            "Recetas de ejemplo cargadas (${mockRecipes.size})",
            Snackbar.LENGTH_LONG
        ).show()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

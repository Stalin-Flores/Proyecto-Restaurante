package com.example.restaurante

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.restaurante.databinding.FragmentRecipeBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException

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

        //pasa datos del email a la vista
        val emailRecibido = args.userEmail
        binding.tvGreeting.text = "Hola Bienvenido\n$emailRecibido"

        // Cargar las recetas desde el JSON local
        val recipeList = loadRecipesFromJson()

        recipeAdapter = RecipeAdapter(recipeList) { recipe ->
            val action =
                RecipeFragmentDirections.actionRecipeFragmentToDetallesRecetaFragment(recipe)
            findNavController().navigate(action)
        }

        binding.recycleViewRecipe.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = recipeAdapter
        }
    }

    private fun loadRecipesFromJson(): List<RecipeResponse> {
        val jsonString: String
        try {
            // Leer el archivo desde assets
            jsonString = requireContext().assets.open("recipes.json").bufferedReader()
                .use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return emptyList()
        }

        // Convertir String JSON a Lista de Objetos usando Gson
        val listType = object : TypeToken<List<RecipeResponse>>() {}.type
        return Gson().fromJson(jsonString, listType)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

package com.example.restaurante

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.restaurante.databinding.FragmentRecipeBinding

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
        binding.tvTitulo.text = "Hola Bienvenido\n$emailRecibido"

        // 1. Crear lista de datos de prueba
        val recipeList = listOf(
            Recipe(1, "DESAYUNO", "25 min", "Pollo Grillado con Ensalada", "🔥 450 kcal", android.R.drawable.ic_menu_agenda),
            Recipe(2, "ALMUERZO", "40 min", "Pasta Alfredo", "🔥 600 kcal", android.R.drawable.ic_menu_agenda),
            Recipe(3, "CENA", "15 min", "Ensalada Cesar", "🔥 300 kcal", android.R.drawable.ic_menu_agenda)
        )

        // 2. Configurar el Adapter con el listener de clic
        recipeAdapter = RecipeAdapter(recipeList) { recipe ->
            // 3. Navegar al detalle
            findNavController().navigate(R.id.action_recipeFragment_to_detallesRecetaFragment)
        }

        binding.recycleViewRecipe.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = recipeAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

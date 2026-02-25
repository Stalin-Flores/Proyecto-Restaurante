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

        //Datos de prueba
        val recipeList = listOf(
            RecipeResponse(
                id = 1,
                category = "DESAYUNO",
                time = "25 min",
                title = "Pollo Grillado con Ensalada",
                calories = "🔥 450 kcal",
                imageRes = android.R.drawable.ic_menu_agenda,
                description = "Una opción ligera y saludable, perfecta para un desayuno proteico que te mantendrá con energía toda la mañana.",
                ingredients = listOf(
                    IngredientesResponse(1, "Pechuga de Pollo", "200g"),
                    IngredientesResponse(2, "Lechuga Fresca", "1 taza"),
                    IngredientesResponse(3, "Tomates Cherry", "5 unidades"),
                    IngredientesResponse(4, "Aceite de Oliva", "1 cucharada"),
                    IngredientesResponse(5, "Sal y Pimienta", "al gusto")
                ),
                steps = listOf(
                    StepPreparationResponse(1, "Sazonar", "Sazona la pechuga de pollo con sal, pimienta y un poco de aceite."),
                    StepPreparationResponse(2, "Cocción", "Calienta el grill y cocina el pollo por ambos lados hasta que esté dorado."),
                    StepPreparationResponse(3, "Preparar vegetales", "Lava y corta la lechuga y los tomates cherry."),
                    StepPreparationResponse(4, "Emplatar", "Sirve el pollo sobre la cama de vegetales y añade el aderezo.")
                )
            ),
            RecipeResponse(
                id = 2,
                category = "ALMUERZO",
                time = "40 min",
                title = "Pasta Alfredo",
                calories = "🔥 600 kcal",
                imageRes = android.R.drawable.ic_menu_agenda,
                description = "La clásica receta italiana de pasta con una salsa cremosa a base de mantequilla y queso parmesano de alta calidad.",
                ingredients = listOf(
                    IngredientesResponse(1, "Fettuccine", "250g"),
                    IngredientesResponse(2, "Mantequilla", "50g"),
                    IngredientesResponse(3, "Crema de Leche", "1/2 taza"),
                    IngredientesResponse(4, "Queso Parmesano", "100g"),
                    IngredientesResponse(5, "Ajo picado", "1 diente")
                ),
                steps = listOf(
                    StepPreparationResponse(1, "Hervir pasta", "Cocina la pasta en abundante agua con sal hasta que esté al dente."),
                    StepPreparationResponse(2, "Preparar salsa", "Derrite la mantequilla con el ajo en una sartén grande."),
                    StepPreparationResponse(3, "Mezclar", "Añade la crema de leche y el queso parmesano, revolviendo constantemente."),
                    StepPreparationResponse(4, "Finalizar", "Incorpora la pasta a la salsa y mezcla bien antes de servir.")
                )
            ),
            RecipeResponse(
                id = 3,
                category = "CENA",
                time = "15 min",
                title = "Ensalada Cesar",
                calories = "🔥 300 kcal",
                imageRes = android.R.drawable.ic_menu_agenda,
                description = "Una ensalada refrescante y crujiente con el balance perfecto entre el aderezo cremoso y los crutones dorados.",
                ingredients = listOf(
                    IngredientesResponse(1, "Lechuga Romana", "2 tazas"),
                    IngredientesResponse(2, "Crutones", "1/2 taza"),
                    IngredientesResponse(3, "Queso Parmesano", "30g"),
                    IngredientesResponse(4, "Salsa Cesar", "2 cucharadas"),
                    IngredientesResponse(5, "Pechuga de Pollo", "100g")
                ),
                steps = listOf(
                    StepPreparationResponse(1, "Cortar lechuga", "Lava y corta la lechuga en trozos de tamaño bocado."),
                    StepPreparationResponse(2, "Preparar pollo", "Corta el pollo ya cocido en tiras finas."),
                    StepPreparationResponse(3, "Mezclar ingredientes", "En un bol grande, combina la lechuga, el pollo y los crutones."),
                    StepPreparationResponse(4, "Aderezar", "Vierte la salsa cesar y espolvorea el queso parmesano por encima.")
                )
            )
        )

        recipeAdapter = RecipeAdapter(recipeList) { recipe ->
            val action = RecipeFragmentDirections.actionRecipeFragmentToDetallesRecetaFragment(recipe)
            findNavController().navigate(action)
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

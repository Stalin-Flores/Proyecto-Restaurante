package com.example.restaurante

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.restaurante.databinding.FragmentFavoritosBinding

class FavoritosFragment : Fragment() {

    private var _binding: FragmentFavoritosBinding? = null
    private val binding get() = _binding!!
    private lateinit var favoritesManager: FavoritesManager
    private lateinit var recipeAdapter: RecipeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        favoritesManager = FavoritesManager(requireContext())
        setupRecyclerView()
        loadFavorites()
    }

    private fun setupRecyclerView() {
        recipeAdapter = RecipeAdapter(emptyList()) { recipe ->
            val action = FavoritosFragmentDirections.actionFavoritosFragmentToDetallesRecetaFragment(recipe)
            findNavController().navigate(action)
        }
        
        binding.rvFavorites.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = recipeAdapter
        }
    }

    private fun loadFavorites() {
        val favorites = favoritesManager.getFavorites()
        if (favorites.isEmpty()) {
            binding.tvEmptyMessage.visibility = View.VISIBLE
            binding.rvFavorites.visibility = View.GONE
        } else {
            binding.tvEmptyMessage.visibility = View.GONE
            binding.rvFavorites.visibility = View.VISIBLE
            recipeAdapter.updateRecipes(favorites)
        }
    }

    override fun onResume() {
        super.onResume()
        // Recargar favoritos al volver al fragmento por si se eliminó alguno en detalles
        loadFavorites()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

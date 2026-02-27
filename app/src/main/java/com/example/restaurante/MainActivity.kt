package com.example.restaurante

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurante.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // Instancia de Firebase Authentication
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Configura el botón de navegación
        settingBtnNavigationCard()
    }

    override fun onStart() {
        super.onStart()
        // La lógica de autenticación se maneja en onCreate
    }

    // Configura el botón de navegación
    private fun settingBtnNavigationCard() {
        var navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        var navController = navHostFragment.navController

        // Maneja el clic en el botón de navegación
        binding.recipeFragment.setOnClickListener {
            navController.navigate(R.id.recipeFragment)
        }

        binding.favoritosFragment.setOnClickListener {
            navController.navigate(R.id.favoritosFragment)
        }

        binding.perfilFragment.setOnClickListener {
            navController.navigate(R.id.perfilFragment)
        }


        // Maneja la visibilidad del botón de navegación en función de la pantalla actual
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.bienvenida, R.id.detallesRecetaFragment -> {
                    binding.btnNavigationCard.visibility = TextView.GONE
                }

                else -> {
                    binding.btnNavigationCard.visibility = TextView.VISIBLE
                }
            }
            updateNavVisuals(destination.id)
        }
    }

    // Maneja los estilos de los iconos de navegación
    private fun updateNavVisuals(destinationId: Int) {
        val navIconos = listOf(
            binding.recipeFragment,
            binding.favoritosFragment,
            binding.perfilFragment
        )
        navIconos.forEach { icono ->
            val isSelected = icono.id == destinationId
            val color = if (isSelected) getColor(R.color.primary) else getColor(R.color.light_gray)
            icono.imageTintList = android.content.res.ColorStateList.valueOf(color)

            val scale = if (isSelected) 1.4f else 1f
            icono.animate()
                .scaleX(scale)
                .scaleY(scale)
                .setDuration(200)
                .start()
        }
    }
}


package com.example.restaurante

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.restaurante.databinding.FragmentPerfilBinding
import com.google.firebase.auth.FirebaseAuth

class PerfilFragment : Fragment() {

    private var _binding: FragmentPerfilBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    companion object {
        private const val PREFS_NAME = "UserPreferences"
        private const val KEY_NOTIFICATIONS = "notifications_enabled"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPerfilBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializar Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Cargar datos del usuario
        loadUserData()

        // Configurar el estado de las notificaciones
        setupNotifications()

        // Configurar el botón de cerrar sesión
        setupLogoutButton()

        // Configurar el botón de editar perfil (opcional)
        setupEditProfile()
    }

    private fun loadUserData() {
        val currentUser = auth.currentUser

        if (currentUser != null) {
            // Cargar email del usuario
            binding.tvUserEmail.text = currentUser.email ?: "usuario@email.com"

            // Cargar nombre del usuario (Firebase puede no tener displayName)
            val displayName = currentUser.displayName
            if (!displayName.isNullOrEmpty()) {
                binding.tvUserName.text = displayName
            } else {
                // Si no hay nombre, usar la primera parte del email
                val emailName = currentUser.email?.split("@")?.get(0)?.replaceFirstChar { it.uppercase() }
                binding.tvUserName.text = emailName ?: "Usuario"
            }
        } else {
            // Si no hay usuario logueado, mostrar valores por defecto
            binding.tvUserEmail.text = "usuario@email.com"
            binding.tvUserName.text = "Usuario"
        }
    }

    private fun setupNotifications() {
        val prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        // Cargar el estado guardado de las notificaciones
        val notificationsEnabled = prefs.getBoolean(KEY_NOTIFICATIONS, true)
        binding.switchNotifications.isChecked = notificationsEnabled

        // Configurar el listener del switch
        binding.switchNotifications.setOnCheckedChangeListener { _, isChecked ->
            // Guardar el estado
            prefs.edit().putBoolean(KEY_NOTIFICATIONS, isChecked).apply()

            // Mostrar mensaje al usuario
            val message = if (isChecked) {
                "Notificaciones activadas"
            } else {
                "Notificaciones desactivadas"
            }
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupLogoutButton() {
        binding.btnCerrarSesion.setOnClickListener {
            // Mostrar diálogo de confirmación
            AlertDialog.Builder(requireContext())
                .setTitle("Cerrar sesión")
                .setMessage("¿Estás seguro de que deseas cerrar sesión?")
                .setPositiveButton("Sí") { dialog, _ ->
                    // Cerrar sesión en Firebase
                    auth.signOut()

                    // Limpiar el caché de recetas
                    clearRecipeCache()

                    // Navegar de vuelta a la pantalla de bienvenida
                    findNavController().navigate(R.id.bienvenida)

                    Toast.makeText(
                        requireContext(),
                        "Sesión cerrada correctamente",
                        Toast.LENGTH_SHORT
                    ).show()

                    dialog.dismiss()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    private fun setupEditProfile() {
        binding.ivEditProfile.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "Función de editar perfil próximamente",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun clearRecipeCache() {
        try {
            val prefs = requireContext().getSharedPreferences("RecipeCache", Context.MODE_PRIVATE)
            prefs.edit().clear().apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
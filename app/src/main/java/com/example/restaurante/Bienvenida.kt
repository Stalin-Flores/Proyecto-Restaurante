package com.example.restaurante

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth


class Bienvenida : Fragment() {

    // Declaración de vistas que se inicializarán después de inflar el layout
    private lateinit var correoEt: EditText
    private lateinit var passEt: EditText
    private lateinit var botonLogin: Button
    private lateinit var botonCrearCuenta: TextView

    // Instancia de Firebase Authentication
    private lateinit var auth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar el layout del fragmento de bienvenida
        return inflater.inflate(R.layout.fragment_bienvenida, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializar Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Inicializar las vistas del layout usando la vista del fragmento
        correoEt = view.findViewById(R.id.etEmail)
        passEt = view.findViewById(R.id.etPassword)
        botonLogin = view.findViewById(R.id.btnLogin)
        botonCrearCuenta = view.findViewById(R.id.tvRegister)

        // Configurar el listener para el botón de login
        botonLogin.setOnClickListener {
            // Obtener y limpiar los valores ingresados por el usuario
            val email = correoEt.text.toString().trim()
            val password = passEt.text.toString().trim()

            // Validar los datos de entrada
            when {
                // Verificar si el campo de correo está vacío
                email.isEmpty() -> {
                    Toast.makeText(requireContext(), "Escriba el correo.", Toast.LENGTH_LONG).show()
                }
                // Verificar si el formato del correo es válido
                !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    Toast.makeText(requireContext(), "Formato de correo incorrecto.", Toast.LENGTH_LONG).show()
                }
                // Verificar si el campo de contraseña está vacío
                password.isEmpty() -> {
                    Toast.makeText(requireContext(), "Escriba la contraseña.", Toast.LENGTH_LONG).show()
                }
                // Si todas las validaciones pasan, proceder con el login
                else -> {
                    loginFirebase(email, password)
                }
            }
        }

        // Configurar el listener para el botón de crear cuenta
        botonCrearCuenta.setOnClickListener {
            // Abrir el diálogo para crear una nueva cuenta
            DialogoCrearCuenta().show(parentFragmentManager, "DialogoCrearCuenta")
        }
    }

    /**
     * Función para autenticar al usuario con Firebase
     * @param email Correo electrónico del usuario
     * @param password Contraseña del usuario
     */
    private fun loginFirebase(email: String, password: String) {
        // Autenticar usuario con correo y contraseña usando Firebase
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Login exitoso
                    val user = auth.currentUser

                    // Cambia de fragment bienvenida a recipeFragment pasando el argumento del correo
                    val email = user?.email
                    val action = BienvenidaDirections.actionBienvenidaToRecipeFragment(email)
                    findNavController().navigate(action)

                    Toast.makeText(
                        requireContext(),
                        "Login exitoso. Bienvenido ${email}",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {
                    // Error en el login
                    val errorMessage = task.exception?.message ?: "Error desconocido"
                    Toast.makeText(
                        requireContext(),
                        "Error al iniciar sesión: $errorMessage",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

}


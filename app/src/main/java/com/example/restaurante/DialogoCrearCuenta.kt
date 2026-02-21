package com.example.restaurante

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.firebase.auth.FirebaseAuth


class DialogoCrearCuenta : DialogFragment() {

    // Instancia de Firebase Authentication
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Establecer un tema con fondo semi-transparente
        setStyle(STYLE_NORMAL, android.R.style.Theme_Material_Light_Dialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflar el layout del diálogo
        val view = inflater.inflate(R.layout.fragment_dialogo_crear_cuenta, container, false)

        // Inicializar Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Obtener referencias a las vistas
        val correo = view.findViewById<EditText>(R.id.edittext_correo)
        val pass = view.findViewById<EditText>(R.id.edittext_pass)
        val botonCrear = view.findViewById<Button>(R.id.boton_crear_cuenta)

        // Configurar el listener del botón de crear cuenta
        botonCrear.setOnClickListener {
            // Obtener y limpiar los valores ingresados
            val email = correo.text.toString().trim()
            val password = pass.text.toString().trim()

            // Validar los datos de entrada
            when {
                // Verificar si el campo de correo está vacío o es inválido
                email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    Toast.makeText(requireContext(), "Formato de correo incorrecto.", Toast.LENGTH_LONG).show()
                }
                // Verificar si el campo de contraseña está vacío
                password.isEmpty() -> {
                    Toast.makeText(requireContext(), "Escriba la contraseña.", Toast.LENGTH_LONG).show()
                }
                // Verificar que la contraseña tenga al menos 6 caracteres (requisito de Firebase)
                password.length < 6 -> {
                    Toast.makeText(requireContext(), "La contraseña debe tener al menos 6 caracteres.", Toast.LENGTH_LONG).show()
                }
                // Si todas las validaciones pasan, crear la cuenta
                else -> {
                    crearCuentaFirebase(email, password)
                }
            }
        }

        return view
    }

    /**
     * Crea una nueva cuenta de usuario en Firebase Authentication
     * @param correo Correo electrónico del usuario
     * @param pass Contraseña del usuario
     */
    private fun crearCuentaFirebase(correo: String, pass: String) {
        // Crear usuario con correo y contraseña usando Firebase
        auth.createUserWithEmailAndPassword(correo, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // La cuenta fue creada exitosamente
                    Toast.makeText(
                        requireContext(),
                        "Cuenta creada exitosamente",
                        Toast.LENGTH_LONG
                    ).show()

                    // Cerrar el diálogo después de crear la cuenta
                    dismiss()

                    // Opcional: Puedes navegar a otra pantalla o actualizar el UI
                    // Por ejemplo, podrías enviar el usuario de vuelta al login
                } else {
                    // Error al crear la cuenta
                    val errorMessage = task.exception?.message ?: "Error desconocido"
                    Toast.makeText(
                        requireContext(),
                        "Error al crear cuenta: $errorMessage",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }
}


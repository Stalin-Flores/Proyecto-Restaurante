package com.example.restaurante

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {

    // Instancia de Firebase Authentication
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializar Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Siempre cargar el Fragment de Bienvenida primero
        // El usuario debe hacer login cada vez que abre la app
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main, Bienvenida())
                .commit()
        }
    }


    fun mostrarPantallaInicio(email: String?) {
        // Actualizar el TextView con el email del usuario
        val tvTitulo = findViewById<TextView>(R.id.tvTitulo)
        tvTitulo.text = "Hola Bienvenido\n$email"
    }


    override fun onStart() {
        super.onStart()
        // La lógica de autenticación se maneja en onCreate
    }
}


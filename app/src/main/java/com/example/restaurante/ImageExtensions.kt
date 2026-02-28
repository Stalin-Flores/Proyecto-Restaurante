package com.example.restaurante

import android.widget.ImageView
import coil.load

/**
 * Carga una imagen de receta de forma inteligente:
 * 1. Si el path empieza con "http", la descarga de la web usando Coil.
 * 2. Si es un nombre (ej: "comida1"), busca el recurso en la carpeta drawable.
 * 3. Si falla, pone una imagen por defecto.
 */
fun ImageView.loadRecipeImage(imagePath: String?) {
    val context = this.context
    val localResId = if (imagePath != null) {
        context.resources.getIdentifier(imagePath, "drawable", context.packageName)
    } else 0

    if (imagePath != null && imagePath.startsWith("http")) {
        // Carga de web
        this.load(imagePath) {
            crossfade(true)
            placeholder(android.R.drawable.ic_menu_report_image)
            error(if (localResId != 0) localResId else android.R.drawable.ic_menu_report_image)
        }
    } else if (localResId != 0) {
        // Carga local
        this.setImageResource(localResId)
    } else {
        // Imagen por defecto
        this.setImageResource(android.R.drawable.ic_menu_report_image)
    }
}

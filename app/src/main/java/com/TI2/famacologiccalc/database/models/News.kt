package com.TI2.famacologiccalc.database.models

data class News(
    val title: String,        // Título de la noticia
    val description: String,  // Breve descripción
    val imageResId: Int       // Recurso de imagen (opcional, usa 0 si no hay)
)



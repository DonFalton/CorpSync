package com.example.corpsyncmobile.data.repository

object CategoryRepository {

    val estado: Map<String, String> = mapOf(
        "pendiente" to "Pendiente",
        "en_proceso" to "En proceso",
        "resuelto" to "Resuelto"
    )

    val categoria: LinkedHashMap<String, String> = linkedMapOf(
        "sin_categorizar" to "Sin categorizar",
        "hardware" to "Hardware",
        "software" to "Software",
        "red" to "Red",
        "otro" to "Otros"
    )

    val prioridad: LinkedHashMap<String, String> = linkedMapOf(
        "por_asignar" to "Por asignar",
        "baja" to "Baja",
        "media" to "Media",
        "alta" to "Alta",
        "critica" to "Crítica"
    )
}

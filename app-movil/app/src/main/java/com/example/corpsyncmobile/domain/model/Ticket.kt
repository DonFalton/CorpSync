package com.example.corpsyncmobile.domain.model

data class Ticket(
    val id: Long,
    val titulo: String,
    val descripcion: String,
    val categoria: String? = null,
    val prioridad: String? = null,
    val estado: String,
    val empleadoId: String,
    val tecnicoId: String? = null,
    val imagenUrl: String? = null,
    val creadoEn: String,
    val actualizadoEn: String
)

data class PerfilRef(
    val id: String,
    val nombre: String,
    val email: String? = null
)

data class TicketDetail(
    val id: Long,
    val titulo: String,
    val descripcion: String,
    val categoria: String? = null,
    val prioridad: String? = null,
    val estado: String,
    val empleadoId: String,
    val tecnicoId: String? = null,
    val imagenUrl: String? = null,
    val creadoEn: String,
    val actualizadoEn: String,
    val empleado: PerfilRef? = null,
    val tecnico: PerfilRef? = null
)

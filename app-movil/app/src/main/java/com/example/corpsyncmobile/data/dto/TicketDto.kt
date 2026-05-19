package com.example.corpsyncmobile.data.dto

import com.example.corpsyncmobile.domain.model.PerfilRef
import com.example.corpsyncmobile.domain.model.Ticket
import com.example.corpsyncmobile.domain.model.TicketDetail
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TicketDto(
    val id: Long,
    val titulo: String,
    val descripcion: String,
    val categoria: String? = null,
    val prioridad: String? = null,
    val estado: String,
    @SerialName("empleado_id") val empleadoId: String,
    @SerialName("tecnico_id") val tecnicoId: String? = null,
    @SerialName("imagen_url") val imagenUrl: String? = null,
    @SerialName("creado_en") val creadoEn: String,
    @SerialName("actualizado_en") val actualizadoEn: String
)

@Serializable
data class PerfilRefDto(
    val id: String,
    val nombre: String,
    val email: String? = null
)

@Serializable
data class TicketDetailDto(
    val id: Long,
    val titulo: String,
    val descripcion: String,
    val categoria: String? = null,
    val prioridad: String? = null,
    val estado: String,
    @SerialName("empleado_id") val empleadoId: String,
    @SerialName("tecnico_id") val tecnicoId: String? = null,
    @SerialName("imagen_url") val imagenUrl: String? = null,
    @SerialName("creado_en") val creadoEn: String,
    @SerialName("actualizado_en") val actualizadoEn: String,
    val empleado: PerfilRefDto? = null,
    val tecnico: PerfilRefDto? = null
)

@Serializable
data class NewTicketDto(
    val titulo: String,
    val descripcion: String,
    val categoria: String,
    val prioridad: String,
    @SerialName("empleado_id") val empleadoId: String,
    @SerialName("imagen_url") val imagenUrl: String? = null
)

fun TicketDto.toDomain(): Ticket = Ticket(
    id = id,
    titulo = titulo,
    descripcion = descripcion,
    categoria = categoria,
    prioridad = prioridad,
    estado = estado,
    empleadoId = empleadoId,
    tecnicoId = tecnicoId,
    imagenUrl = imagenUrl,
    creadoEn = creadoEn,
    actualizadoEn = actualizadoEn
)

fun PerfilRefDto.toDomain(): PerfilRef = PerfilRef(
    id = id,
    nombre = nombre,
    email = email
)

fun TicketDetailDto.toDomain(): TicketDetail = TicketDetail(
    id = id,
    titulo = titulo,
    descripcion = descripcion,
    categoria = categoria,
    prioridad = prioridad,
    estado = estado,
    empleadoId = empleadoId,
    tecnicoId = tecnicoId,
    imagenUrl = imagenUrl,
    creadoEn = creadoEn,
    actualizadoEn = actualizadoEn,
    empleado = empleado?.toDomain(),
    tecnico = tecnico?.toDomain()
)

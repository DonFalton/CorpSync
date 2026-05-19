package com.example.corpsyncmobile.data

import com.example.corpsyncmobile.supabase
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
import io.github.jan.supabase.storage.storage
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Ticket(
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
data class PerfilRef(
    val id: String,
    val nombre: String,
    val email: String? = null
)

@Serializable
data class TicketDetail(
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
    val empleado: PerfilRef? = null,
    val tecnico: PerfilRef? = null
)

@Serializable
private data class NewTicket(
    val titulo: String,
    val descripcion: String,
    val categoria: String,
    val prioridad: String,
    @SerialName("empleado_id") val empleadoId: String,
    @SerialName("imagen_url") val imagenUrl: String? = null
)

object TicketService {

    private const val BUCKET = "ticket-images"
    private const val TABLE = "tickets"

    suspend fun create(
        titulo: String,
        descripcion: String,
        categoria: String,
        prioridad: String,
        imageBytes: ByteArray? = null
    ): Ticket {
        val userId = supabase.auth.currentUserOrNull()?.id
            ?: throw IllegalStateException("Not authenticated")

        val imageUrl = imageBytes?.let { bytes ->
            val fileName = "ticket_${System.currentTimeMillis()}.jpg"
            supabase.storage.from(BUCKET).upload(fileName, bytes)
            supabase.storage.from(BUCKET).publicUrl(fileName)
        }

        return supabase.from(TABLE).insert(
            NewTicket(
                titulo = titulo.trim(),
                descripcion = descripcion.trim(),
                categoria = categoria,
                prioridad = prioridad,
                empleadoId = userId,
                imagenUrl = imageUrl
            )
        ) {
            select()
        }.decodeSingle()
    }

    suspend fun listMine(): List<Ticket> {
        val userId = supabase.auth.currentUserOrNull()?.id
            ?: throw IllegalStateException("Not authenticated")

        return supabase.from(TABLE).select {
            filter { eq("empleado_id", userId) }
            order("creado_en", Order.DESCENDING)
        }.decodeList()
    }

    suspend fun getById(id: Long): TicketDetail {
        return supabase.from(TABLE).select(
            columns = Columns.raw(
                "*, empleado:perfiles!empleado_id(id,nombre,email), tecnico:perfiles!tecnico_id(id,nombre,email)"
            )
        ) {
            filter { eq("id", id) }
        }.decodeSingle()
    }
}

package com.example.corpsyncmobile.data.repository

import com.example.corpsyncmobile.data.dto.NewTicketDto
import com.example.corpsyncmobile.data.dto.TicketDetailDto
import com.example.corpsyncmobile.data.dto.TicketDto
import com.example.corpsyncmobile.data.dto.toDomain
import com.example.corpsyncmobile.domain.model.Ticket
import com.example.corpsyncmobile.domain.model.TicketDetail
import com.example.corpsyncmobile.data.supabase
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order

object TicketRepository {

    private const val TABLE = "tickets"

    suspend fun create(
        titulo: String,
        descripcion: String,
        categoria: String,
        prioridad: String,
        imageBytes: ByteArray? = null
    ): Ticket {
        val userId = AuthRepository.requireUserId()
        val imageUrl = imageBytes?.let { AttachmentRepository.uploadTicketImage(it) }

        return supabase.from(TABLE).insert(
            NewTicketDto(
                titulo = titulo.trim(),
                descripcion = descripcion.trim(),
                categoria = categoria,
                prioridad = prioridad,
                empleadoId = userId,
                imagenUrl = imageUrl
            )
        ) {
            select()
        }.decodeSingle<TicketDto>().toDomain()
    }

    suspend fun listMine(): List<Ticket> {
        val userId = AuthRepository.requireUserId()
        return supabase.from(TABLE).select {
            filter { eq("empleado_id", userId) }
            order("creado_en", Order.DESCENDING)
        }.decodeList<TicketDto>().map { it.toDomain() }
    }

    suspend fun getById(id: Long): TicketDetail =
        supabase.from(TABLE).select(
            columns = Columns.raw(
                "*, empleado:perfiles!empleado_id(id,nombre,email), tecnico:perfiles!tecnico_id(id,nombre,email)"
            )
        ) {
            filter { eq("id", id) }
        }.decodeSingle<TicketDetailDto>().toDomain()
}

package com.example.corpsyncmobile.data.repository

import com.example.corpsyncmobile.data.dto.RoleDto
import com.example.corpsyncmobile.data.supabase
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns

object ProfileRepository {

    private const val TABLE = "perfiles"

    const val ROLE_EMPLOYEE = "empleado"

    suspend fun currentRole(): String? {
        val userId = AuthRepository.currentUserId() ?: return null
        return supabase.from(TABLE).select(columns = Columns.list("rol")) {
            filter { eq("id", userId) }
        }.decodeSingleOrNull<RoleDto>()?.rol
    }
}

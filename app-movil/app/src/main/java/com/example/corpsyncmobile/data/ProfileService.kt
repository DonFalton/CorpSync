package com.example.corpsyncmobile.data

import com.example.corpsyncmobile.supabase
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.serialization.Serializable

object ProfileService {

    private const val TABLE = "perfiles"

    const val ROLE_EMPLOYEE = "empleado"

    @Serializable
    private data class RoleRow(val rol: String)

    suspend fun currentRole(): String? {
        val userId = supabase.auth.currentUserOrNull()?.id ?: return null
        return supabase.from(TABLE).select(columns = Columns.list("rol")) {
            filter { eq("id", userId) }
        }.decodeSingleOrNull<RoleRow>()?.rol
    }
}

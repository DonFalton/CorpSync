package com.example.corpsyncmobile.data

import com.example.corpsyncmobile.supabase
import io.github.jan.supabase.storage.storage

object ImageService {

    private const val TICKET_BUCKET = "ticket-images"

    fun resolveTicketImage(stored: String?): String? {
        val trimmed = stored?.trim().orEmpty()
        if (trimmed.isEmpty()) return null
        if (trimmed.startsWith("http://", ignoreCase = true) ||
            trimmed.startsWith("https://", ignoreCase = true)
        ) {
            return trimmed
        }
        val path = trimmed.removePrefix("$TICKET_BUCKET/")
        return supabase.storage.from(TICKET_BUCKET).publicUrl(path)
    }
}

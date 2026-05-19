package com.example.corpsyncmobile.data.repository

import android.content.Context
import android.net.Uri

object AttachmentRepository {

    private const val TICKET_BUCKET = "ticket-images"

    fun readBytes(context: Context, uri: Uri): ByteArray =
        context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
            ?: throw IllegalStateException("No se pudo leer la imagen")

    suspend fun uploadTicketImage(bytes: ByteArray): String {
        val fileName = "ticket_${System.currentTimeMillis()}.jpg"
        StorageRepository.upload(TICKET_BUCKET, fileName, bytes)
        return StorageRepository.publicUrl(TICKET_BUCKET, fileName)
    }

    fun resolveTicketImage(stored: String?): String? {
        val trimmed = stored?.trim().orEmpty()
        if (trimmed.isEmpty()) return null
        if (trimmed.startsWith("http://", ignoreCase = true) ||
            trimmed.startsWith("https://", ignoreCase = true)
        ) {
            return trimmed
        }
        val path = trimmed.removePrefix("$TICKET_BUCKET/")
        return StorageRepository.publicUrl(TICKET_BUCKET, path)
    }
}

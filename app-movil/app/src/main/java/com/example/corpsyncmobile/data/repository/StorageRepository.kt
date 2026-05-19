package com.example.corpsyncmobile.data.repository

import com.example.corpsyncmobile.data.supabase
import io.github.jan.supabase.storage.storage

object StorageRepository {

    suspend fun upload(bucket: String, path: String, bytes: ByteArray) {
        supabase.storage.from(bucket).upload(path, bytes)
    }

    fun publicUrl(bucket: String, path: String): String =
        supabase.storage.from(bucket).publicUrl(path)
}

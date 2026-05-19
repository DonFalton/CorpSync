package com.example.corpsyncmobile.data.repository

import com.example.corpsyncmobile.data.supabase
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.serialization.json.JsonObject

object AuthRepository {

    suspend fun signIn(email: String, password: String) {
        supabase.auth.signInWith(Email) {
            this.email = email.trim()
            this.password = password
        }
    }

    suspend fun signUp(email: String, password: String) {
        supabase.auth.signUpWith(Email) {
            this.email = email.trim()
            this.password = password
        }
    }

    suspend fun signOut() {
        supabase.auth.signOut()
    }

    fun hasSession(): Boolean = supabase.auth.currentSessionOrNull() != null

    fun currentUserId(): String? = supabase.auth.currentUserOrNull()?.id

    fun requireUserId(): String =
        currentUserId() ?: throw IllegalStateException("Not authenticated")

    fun currentUserEmail(): String? = supabase.auth.currentUserOrNull()?.email

    fun currentUserMetadata(): JsonObject? = supabase.auth.currentUserOrNull()?.userMetadata
}

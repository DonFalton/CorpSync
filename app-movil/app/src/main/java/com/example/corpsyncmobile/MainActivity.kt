package com.example.corpsyncmobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.corpsyncmobile.ui.theme.CorpSyncMobileTheme
import io.github.jan.supabase.auth.auth

private enum class Screen { Login, Home }

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CorpSyncMobileTheme {
                var screen by remember {
                    val initial = if (supabase.auth.currentSessionOrNull() != null) Screen.Home else Screen.Login
                    mutableStateOf(initial)
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        when (screen) {
                            Screen.Login -> LoginScreen(
                                onLoginSuccess = { screen = Screen.Home }
                            )
                            Screen.Home -> CreateTicketScreen()
                        }
                    }
                }
            }
        }
    }
}

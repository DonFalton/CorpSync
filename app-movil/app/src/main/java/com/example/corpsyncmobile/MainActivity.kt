package com.example.corpsyncmobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.corpsyncmobile.data.ProfileService
import com.example.corpsyncmobile.ui.screens.AccessDeniedScreen
import com.example.corpsyncmobile.ui.screens.CreateTicketScreen
import com.example.corpsyncmobile.ui.screens.LoginScreen
import com.example.corpsyncmobile.ui.screens.MyTicketsScreen
import com.example.corpsyncmobile.ui.screens.TicketDetailScreen
import com.example.corpsyncmobile.ui.theme.CorpIndigo
import com.example.corpsyncmobile.ui.theme.CorpSyncMobileTheme
import com.example.corpsyncmobile.ui.theme.PageBackground
import io.github.jan.supabase.auth.auth

private sealed interface Screen {
    object Splash : Screen
    object Login : Screen
    object AccessDenied : Screen
    object MyTickets : Screen
    object CreateTicket : Screen
    data class TicketDetail(val ticketId: Long) : Screen
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CorpSyncMobileTheme {
                var screen by remember {
                    val initial: Screen = if (supabase.auth.currentSessionOrNull() != null) Screen.Splash else Screen.Login
                    mutableStateOf(initial)
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        when (val current = screen) {
                            is Screen.Splash -> SplashGate(
                                onEmployee = { screen = Screen.MyTickets },
                                onDenied = { screen = Screen.AccessDenied },
                                onUnauthenticated = { screen = Screen.Login }
                            )
                            is Screen.Login -> LoginScreen(
                                onLoginSuccess = { screen = Screen.MyTickets },
                                onAccessDenied = { screen = Screen.AccessDenied }
                            )
                            is Screen.AccessDenied -> AccessDeniedScreen(
                                onBackToLogin = { screen = Screen.Login }
                            )
                            is Screen.MyTickets -> MyTicketsScreen(
                                onCreateClick = { screen = Screen.CreateTicket },
                                onLogout = { screen = Screen.Login },
                                onTicketClick = { id -> screen = Screen.TicketDetail(id) }
                            )
                            is Screen.CreateTicket -> CreateTicketScreen(
                                onBack = { screen = Screen.MyTickets },
                                onCreated = { screen = Screen.MyTickets }
                            )
                            is Screen.TicketDetail -> TicketDetailScreen(
                                ticketId = current.ticketId,
                                onBack = { screen = Screen.MyTickets }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SplashGate(
    onEmployee: () -> Unit,
    onDenied: () -> Unit,
    onUnauthenticated: () -> Unit
) {
    LaunchedEffect(Unit) {
        if (supabase.auth.currentSessionOrNull() == null) {
            onUnauthenticated()
            return@LaunchedEffect
        }
        val role = runCatching { ProfileService.currentRole() }.getOrNull()
        if (role == ProfileService.ROLE_EMPLOYEE) {
            onEmployee()
        } else {
            runCatching { supabase.auth.signOut() }
            onDenied()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PageBackground),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = CorpIndigo)
    }
}

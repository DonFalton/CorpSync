package com.example.corpsyncmobile.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.LocalOffer
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.corpsyncmobile.data.Ticket
import com.example.corpsyncmobile.data.TicketService
import com.example.corpsyncmobile.supabase
import com.example.corpsyncmobile.ui.theme.CorpIndigo
import com.example.corpsyncmobile.ui.theme.FooterGray
import com.example.corpsyncmobile.ui.theme.LabelGray
import com.example.corpsyncmobile.ui.theme.PageBackground
import com.example.corpsyncmobile.ui.theme.SubtleDivider
import com.example.corpsyncmobile.ui.theme.SurfaceWhite
import com.example.corpsyncmobile.ui.theme.TextPrimary
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive
import java.time.Duration
import java.time.Instant
import java.time.OffsetDateTime

private val estadoLabels = mapOf(
    "pendiente" to "Pendiente",
    "en_proceso" to "En proceso",
    "resuelto" to "Resuelto"
)

private val categoriaLabels = mapOf(
    "sin_categorizar" to "Sin categorizar",
    "hardware" to "Hardware",
    "software" to "Software",
    "red" to "Red",
    "otro" to "Otros"
)

private data class StatusPalette(val bg: Color, val fg: Color, val border: Color)

private val statusOpenPalette = StatusPalette(
    bg = Color(0xFFFFF7ED),
    fg = Color(0xFFC2410C),
    border = Color(0xFFFED7AA)
)
private val statusProgressPalette = StatusPalette(
    bg = Color(0xFFEFF6FF),
    fg = Color(0xFF1D4ED8),
    border = Color(0xFFBFDBFE)
)
private val statusResolvedPalette = StatusPalette(
    bg = Color(0xFFECFDF5),
    fg = Color(0xFF047857),
    border = Color(0xFFA7F3D0)
)

@Composable
fun MyTicketsScreen(
    onCreateClick: () -> Unit,
    onLogout: () -> Unit,
    onTicketClick: (Long) -> Unit
) {
    val scope = rememberCoroutineScope()
    var tickets by remember { mutableStateOf<List<Ticket>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val user = remember { supabase.auth.currentUserOrNull() }
    val email = user?.email.orEmpty()
    val rawName = remember(user) { extractName(user?.userMetadata, email) }
    val displayName = remember(rawName) { prettifyName(rawName) }
    val initials = remember(rawName) { initialsFor(rawName) }

    fun load() {
        scope.launch {
            isLoading = true
            errorMessage = null
            try {
                tickets = TicketService.listMine()
            } catch (e: Exception) {
                errorMessage = e.message ?: "No se pudieron cargar los tickets"
            } finally {
                isLoading = false
            }
        }
    }

    fun signOut() {
        scope.launch {
            try {
                supabase.auth.signOut()
            } catch (_: Exception) {
                // ignore — proceed with local logout regardless
            }
            onLogout()
        }
    }

    LaunchedEffect(Unit) { load() }

    val openCount = tickets.count { it.estado != "resuelto" }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PageBackground)
    ) {
        ListHeader(
            displayName = displayName,
            initials = initials,
            openCount = openCount,
            totalCount = tickets.size,
            onLogout = ::signOut
        )

        Box(modifier = Modifier.weight(1f)) {
            when {
                isLoading && tickets.isEmpty() -> LoadingState()
                errorMessage != null -> ErrorState(message = errorMessage!!, onRetry = ::load)
                tickets.isEmpty() -> EmptyState()
                else -> LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        start = 20.dp,
                        end = 20.dp,
                        top = 24.dp,
                        bottom = 140.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        SectionTitle()
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    items(tickets, key = { it.id }) { ticket ->
                        TicketCard(ticket, onClick = { onTicketClick(ticket.id) })
                    }
                }
            }

            BottomActionBar(onCreateClick = onCreateClick)
        }
    }
}

@Composable
private fun ListHeader(
    displayName: String,
    initials: String,
    openCount: Int,
    totalCount: Int,
    onLogout: () -> Unit
) {
    var menuOpen by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(CorpIndigo)
            .padding(horizontal = 24.dp, vertical = 24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = initials,
                    color = CorpIndigo,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.size(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Hola,",
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = displayName,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Box {
                IconButton(
                    onClick = { menuOpen = true },
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.White.copy(alpha = 0.15f))
                ) {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = "Menú",
                        tint = Color.White
                    )
                }
                DropdownMenu(
                    expanded = menuOpen,
                    onDismissRequest = { menuOpen = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Cerrar sesión") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Logout,
                                contentDescription = null
                            )
                        },
                        onClick = {
                            menuOpen = false
                            onLogout()
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = openCount.toString(),
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.size(4.dp))
            Text(
                text = if (openCount == 1) "ticket abierto" else "tickets abiertos",
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.size(8.dp))
            Box(
                modifier = Modifier
                    .size(4.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.6f))
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = "$totalCount en total",
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 14.sp
            )
        }
    }
}

@Composable
private fun SectionTitle() {
    Column {
        Text(
            text = "MIS TICKETS",
            color = LabelGray,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(12.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(SubtleDivider)
        )
    }
}

@Composable
private fun TicketCard(ticket: Ticket, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(SurfaceWhite)
            .border(
                width = 1.5.dp,
                color = SubtleDivider,
                shape = RoundedCornerShape(14.dp)
            )
            .clickable(onClick = onClick)
            .padding(20.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                EstadoBadge(ticket.estado)
                Text(
                    text = "#${ticket.id}",
                    color = FooterGray,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = ticket.titulo,
                color = TextPrimary,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 23.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                MetaItem(
                    icon = Icons.Outlined.LocalOffer,
                    label = categoriaLabels[ticket.categoria] ?: ticket.categoria ?: "—"
                )
                MetaItem(
                    icon = Icons.Outlined.Schedule,
                    label = relativeTime(ticket.creadoEn)
                )
            }
        }
    }
}

@Composable
private fun MetaItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = FooterGray,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.size(6.dp))
        Text(
            text = label,
            color = LabelGray,
            fontSize = 13.sp
        )
    }
}

@Composable
private fun EstadoBadge(estado: String) {
    val palette = when (estado) {
        "resuelto" -> statusResolvedPalette
        "en_proceso" -> statusProgressPalette
        else -> statusOpenPalette
    }
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(palette.bg)
            .border(
                width = 1.5.dp,
                color = palette.border,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(7.dp)
                .clip(CircleShape)
                .background(palette.fg)
        )
        Spacer(modifier = Modifier.size(6.dp))
        Text(
            text = (estadoLabels[estado] ?: estado).uppercase(),
            color = palette.fg,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
private fun BoxScope.BottomActionBar(onCreateClick: () -> Unit) {
    Column(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        PageBackground.copy(alpha = 0f),
                        PageBackground.copy(alpha = 0.9f),
                        PageBackground
                    )
                )
            )
            .padding(start = 24.dp, end = 24.dp, top = 16.dp, bottom = 16.dp)
    ) {
        Button(
            onClick = onCreateClick,
            shape = RoundedCornerShape(32.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = CorpIndigo,
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = "Nuevo ticket",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "CorpSync ITSM · v1.0 · DAM 2025–2026",
            color = FooterGray,
            fontSize = 12.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

@Composable
private fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = CorpIndigo)
    }
}

@Composable
private fun EmptyState() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Aún no has creado tickets",
                color = TextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Toca \"Nuevo ticket\" para crear el primero",
                color = LabelGray,
                fontSize = 13.sp
            )
        }
    }
}

@Composable
private fun ErrorState(message: String, onRetry: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = message,
                color = MaterialTheme.colorScheme.error,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButton(
                onClick = onRetry,
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = CorpIndigo),
                modifier = Modifier.height(44.dp)
            ) {
                Text(
                    text = "Reintentar",
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

private fun extractName(metadata: JsonObject?, email: String): String {
    val fromMetadata = listOf("full_name", "name", "display_name")
        .firstNotNullOfOrNull { key -> metadata?.get(key)?.jsonPrimitive?.contentOrNull }
        ?.takeIf { it.isNotBlank() }
    return fromMetadata ?: email.substringBefore("@").ifBlank { "Usuario" }
}

private fun prettifyName(raw: String): String {
    val parts = raw.split(' ', '.', '_', '-').filter { it.isNotBlank() }
    if (parts.isEmpty()) return raw
    return parts.joinToString(" ") { token ->
        token.replaceFirstChar { c -> if (c.isLowerCase()) c.titlecase() else c.toString() }
    }
}

private fun initialsFor(raw: String): String {
    val parts = raw.split(' ', '.', '_', '-').filter { it.isNotBlank() }
    return when {
        parts.size >= 2 -> "${parts[0].first()}${parts[1].first()}".uppercase()
        parts.size == 1 -> parts[0].take(2).uppercase()
        else -> "?"
    }
}

private fun relativeTime(iso: String): String {
    return try {
        val instant = OffsetDateTime.parse(iso).toInstant()
        val seconds = Duration.between(instant, Instant.now()).seconds.coerceAtLeast(0)
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24
        when {
            seconds < 60 -> "hace unos segundos"
            minutes < 60 -> if (minutes == 1L) "hace 1 minuto" else "hace $minutes minutos"
            hours < 24 -> if (hours == 1L) "hace 1 hora" else "hace $hours horas"
            days < 2 -> "ayer"
            days < 30 -> "hace $days días"
            days < 365 -> {
                val months = days / 30
                if (months == 1L) "hace 1 mes" else "hace $months meses"
            }
            else -> {
                val years = days / 365
                if (years == 1L) "hace 1 año" else "hace $years años"
            }
        }
    } catch (_: Exception) {
        iso.take(10)
    }
}

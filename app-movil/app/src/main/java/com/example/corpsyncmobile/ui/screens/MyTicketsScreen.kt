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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
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
import com.example.corpsyncmobile.domain.model.Ticket
import com.example.corpsyncmobile.data.repository.AuthRepository
import com.example.corpsyncmobile.data.repository.CategoryRepository
import com.example.corpsyncmobile.data.repository.TicketRepository
import com.example.corpsyncmobile.ui.components.AppFooter
import com.example.corpsyncmobile.ui.components.BrandHeader
import com.example.corpsyncmobile.ui.components.BrandIconButton
import com.example.corpsyncmobile.ui.components.DotBadge
import com.example.corpsyncmobile.ui.components.ErrorState
import com.example.corpsyncmobile.ui.components.LoadingState
import com.example.corpsyncmobile.ui.components.SectionHeader
import com.example.corpsyncmobile.ui.components.StatusPalettes
import com.example.corpsyncmobile.ui.theme.BrandBorderWidth
import com.example.corpsyncmobile.ui.theme.BrandShapes
import com.example.corpsyncmobile.ui.theme.CorpIndigo
import com.example.corpsyncmobile.ui.theme.FooterGray
import com.example.corpsyncmobile.ui.theme.LabelGray
import com.example.corpsyncmobile.ui.theme.PageBackground
import com.example.corpsyncmobile.ui.theme.SubtleDivider
import com.example.corpsyncmobile.ui.theme.SurfaceWhite
import com.example.corpsyncmobile.ui.theme.TextPrimary
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive
import java.time.Duration
import java.time.Instant
import java.time.OffsetDateTime

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

    val email = remember { AuthRepository.currentUserEmail().orEmpty() }
    val metadata = remember { AuthRepository.currentUserMetadata() }
    val rawName = remember(metadata, email) { extractName(metadata, email) }
    val displayName = remember(rawName) { prettifyName(rawName) }
    val initials = remember(rawName) { initialsFor(rawName) }

    fun load() {
        scope.launch {
            isLoading = true
            errorMessage = null
            try {
                tickets = TicketRepository.listMine()
            } catch (e: Exception) {
                errorMessage = e.message ?: "No se pudieron cargar los tickets"
            } finally {
                isLoading = false
            }
        }
    }

    fun signOut() {
        scope.launch {
            runCatching { AuthRepository.signOut() }
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

    BrandHeader {
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
                BrandIconButton(
                    icon = Icons.Filled.Menu,
                    onClick = { menuOpen = true },
                    contentDescription = "Menú"
                )
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
        SectionHeader("MIS TICKETS")
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
            .clip(BrandShapes.card)
            .background(SurfaceWhite)
            .border(
                width = BrandBorderWidth,
                color = SubtleDivider,
                shape = BrandShapes.card
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
                DotBadge(
                    label = (CategoryRepository.estado[ticket.estado] ?: ticket.estado).uppercase(),
                    palette = StatusPalettes.forEstado(ticket.estado)
                )
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
                    label = CategoryRepository.categoria[ticket.categoria] ?: ticket.categoria ?: "—"
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
        AppFooter(modifier = Modifier.padding(horizontal = 4.dp))
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

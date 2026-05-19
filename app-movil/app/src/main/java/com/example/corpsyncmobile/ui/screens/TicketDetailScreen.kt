package com.example.corpsyncmobile.ui.screens

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAddAlt1
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import com.example.corpsyncmobile.data.ImageService
import com.example.corpsyncmobile.data.TicketDetail
import com.example.corpsyncmobile.data.TicketService
import com.example.corpsyncmobile.ui.theme.CorpIndigo
import com.example.corpsyncmobile.ui.theme.FooterGray
import com.example.corpsyncmobile.ui.theme.LabelGray
import com.example.corpsyncmobile.ui.theme.PageBackground
import com.example.corpsyncmobile.ui.theme.SubtleDivider
import com.example.corpsyncmobile.ui.theme.SurfaceWhite
import com.example.corpsyncmobile.ui.theme.TextPrimary
import kotlinx.coroutines.launch
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

private val InfoCardBg = Color(0xFFF8FAFC)
private val InfoIconBg = Color(0xFFEEF2FF)
private val InfoIconBorder = Color(0xFFC7D2FE)

private val estadoLabels = mapOf(
    "pendiente" to "Pendiente",
    "en_proceso" to "En proceso",
    "resuelto" to "Resuelto"
)

private val prioridadLabels = mapOf(
    "baja" to "Prioridad baja",
    "media" to "Prioridad media",
    "alta" to "Prioridad alta",
    "critica" to "Prioridad crítica"
)

private val categoriaLabels = mapOf(
    "hardware" to "Hardware",
    "software" to "Software",
    "red" to "Red",
    "otro" to "Otros"
)

private data class BadgePalette(val bg: Color, val fg: Color, val border: Color)

private val statusOpenPalette = BadgePalette(Color(0xFFFFF7ED), Color(0xFFC2410C), Color(0xFFFED7AA))
private val statusProgressPalette = BadgePalette(Color(0xFFEFF6FF), Color(0xFF1D4ED8), Color(0xFFBFDBFE))
private val statusResolvedPalette = BadgePalette(Color(0xFFECFDF5), Color(0xFF047857), Color(0xFFA7F3D0))

private val priorityCriticaPalette = BadgePalette(Color(0xFFFEF2F2), Color(0xFFB91C1C), Color(0xFFFECACA))
private val priorityAltaPalette = BadgePalette(Color(0xFFFFF7ED), Color(0xFFC2410C), Color(0xFFFED7AA))
private val priorityMediaPalette = BadgePalette(Color(0xFFFEFCE8), Color(0xFF92400E), Color(0xFFFDE68A))
private val priorityBajaPalette = BadgePalette(Color(0xFFECFDF5), Color(0xFF047857), Color(0xFFA7F3D0))

@Composable
fun TicketDetailScreen(
    ticketId: Long,
    onBack: () -> Unit
) {
    val scope = rememberCoroutineScope()
    var ticket by remember { mutableStateOf<TicketDetail?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    fun load() {
        scope.launch {
            isLoading = true
            errorMessage = null
            try {
                ticket = TicketService.getById(ticketId)
            } catch (e: Exception) {
                errorMessage = e.message ?: "No se pudo cargar el ticket"
            } finally {
                isLoading = false
            }
        }
    }

    LaunchedEffect(ticketId) { load() }

    val scroll = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PageBackground)
    ) {
        when {
            isLoading && ticket == null -> CenteredLoading(onBack)
            errorMessage != null && ticket == null -> CenteredError(
                message = errorMessage!!,
                onRetry = ::load,
                onBack = onBack
            )
            ticket != null -> DetailContent(
                ticket = ticket!!,
                scroll = scroll,
                onBack = onBack
            )
        }
    }
}

@Composable
private fun DetailContent(
    ticket: TicketDetail,
    scroll: ScrollState,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scroll)
    ) {
        DetailHeader(ticket = ticket, onBack = onBack)

        Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 24.dp)) {
            InfoCard(ticket = ticket)

            Spacer(modifier = Modifier.height(24.dp))

            SectionHeader("DESCRIPCIÓN")
            Spacer(modifier = Modifier.height(12.dp))
            DescriptionBox(text = ticket.descripcion)

            val attachmentUrl = remember(ticket.imagenUrl) {
                ImageService.resolveTicketImage(ticket.imagenUrl)
            }
            if (attachmentUrl != null) {
                Spacer(modifier = Modifier.height(24.dp))
                SectionHeader("ADJUNTOS")
                Spacer(modifier = Modifier.height(12.dp))
                AttachmentThumb(url = attachmentUrl)
            }

            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "CorpSync ITSM · v1.0 · DAM 2025–2026",
                color = FooterGray,
                fontSize = 12.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun DetailHeader(ticket: TicketDetail, onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(CorpIndigo)
            .padding(horizontal = 24.dp, vertical = 24.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White.copy(alpha = 0.15f))
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = Color.White
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Text(
                text = "Ticket #${ticket.id}",
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = ticket.titulo,
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 30.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StatusBadge(ticket.estado)
            val prioridad = ticket.prioridad
            if (prioridad != null && prioridad != "por_asignar") {
                PriorityBadge(prioridad)
            }
            val categoria = ticket.categoria
            if (categoria != null && categoria != "sin_categorizar") {
                CategoryBadge(categoria)
            }
        }
    }
}

@Composable
private fun StatusBadge(estado: String) {
    val palette = when (estado) {
        "resuelto" -> statusResolvedPalette
        "en_proceso" -> statusProgressPalette
        else -> statusOpenPalette
    }
    DotBadge(
        label = (estadoLabels[estado] ?: estado).uppercase(),
        palette = palette
    )
}

@Composable
private fun PriorityBadge(prioridad: String) {
    val palette = when (prioridad) {
        "critica" -> priorityCriticaPalette
        "alta" -> priorityAltaPalette
        "media" -> priorityMediaPalette
        "baja" -> priorityBajaPalette
        else -> statusOpenPalette
    }
    DotBadge(
        label = (prioridadLabels[prioridad] ?: prioridad).uppercase(),
        palette = palette
    )
}

@Composable
private fun CategoryBadge(categoria: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White.copy(alpha = 0.12f))
            .border(
                width = 1.5.dp,
                color = Color.White.copy(alpha = 0.25f),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = (categoriaLabels[categoria] ?: categoria).uppercase(),
            color = Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
private fun DotBadge(label: String, palette: BadgePalette) {
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
                .size(6.dp)
                .clip(CircleShape)
                .background(palette.fg)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = label,
            color = palette.fg,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
private fun InfoCard(ticket: TicketDetail) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(InfoCardBg)
            .border(
                width = 1.5.dp,
                color = SubtleDivider,
                shape = RoundedCornerShape(14.dp)
            )
            .padding(horizontal = 20.dp)
    ) {
        InfoRow(
            icon = Icons.Filled.Person,
            label = "SOLICITANTE",
            value = ticket.empleado?.nombre ?: ticket.empleado?.email ?: "—",
            isMuted = ticket.empleado == null
        )
        InfoDivider()
        InfoRow(
            icon = Icons.Filled.PersonAddAlt1,
            label = "ASIGNADO A",
            value = ticket.tecnico?.nombre ?: "Sin asignar",
            isMuted = ticket.tecnico == null
        )
        InfoDivider()
        InfoRow(
            icon = Icons.Filled.CalendarToday,
            label = "CREADO",
            value = formatCreated(ticket.creadoEn),
            isMuted = false
        )
    }
}

@Composable
private fun InfoRow(icon: ImageVector, label: String, value: String, isMuted: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(InfoIconBg)
                .border(
                    width = 1.5.dp,
                    color = InfoIconBorder,
                    shape = RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = CorpIndigo,
                modifier = Modifier.size(22.dp)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                color = LabelGray,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                color = if (isMuted) FooterGray else TextPrimary,
                fontSize = 16.sp,
                fontWeight = if (isMuted) FontWeight.Medium else FontWeight.SemiBold,
                fontStyle = if (isMuted) FontStyle.Italic else FontStyle.Normal
            )
        }
    }
}

@Composable
private fun InfoDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(SubtleDivider)
    )
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        color = LabelGray,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp
    )
}

@Composable
private fun DescriptionBox(text: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(InfoCardBg)
            .border(
                width = 1.5.dp,
                color = SubtleDivider,
                shape = RoundedCornerShape(14.dp)
            )
            .padding(horizontal = 20.dp, vertical = 18.dp)
    ) {
        Text(
            text = text,
            color = TextPrimary,
            fontSize = 15.sp,
            lineHeight = 24.sp
        )
    }
}

@Composable
private fun AttachmentThumb(url: String) {
    Box(
        modifier = Modifier
            .size(120.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF3F4F6))
            .border(
                width = 1.5.dp,
                color = SubtleDivider,
                shape = RoundedCornerShape(12.dp)
            )
    ) {
        SubcomposeAsyncImage(
            model = url,
            contentDescription = "Adjunto",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .aspectRatio(1f),
            loading = {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = CorpIndigo,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(24.dp)
                    )
                }
            },
            error = {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.BrokenImage,
                        contentDescription = null,
                        tint = FooterGray,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        )
    }
}

@Composable
private fun CenteredLoading(onBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        SimpleTopBar(onBack = onBack)
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = CorpIndigo)
        }
    }
}

@Composable
private fun CenteredError(message: String, onRetry: () -> Unit, onBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        SimpleTopBar(onBack = onBack)
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
                    Text(text = "Reintentar", fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
private fun SimpleTopBar(onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(CorpIndigo)
            .padding(horizontal = 24.dp, vertical = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.White.copy(alpha = 0.15f))
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Volver",
                tint = Color.White
            )
        }
        Spacer(modifier = Modifier.width(14.dp))
        Text(
            text = "Detalle del ticket",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

private val esMonths = listOf(
    "ene", "feb", "mar", "abr", "may", "jun",
    "jul", "ago", "sep", "oct", "nov", "dic"
)

private fun formatCreated(iso: String): String {
    return try {
        val zoned = OffsetDateTime.parse(iso).atZoneSameInstant(ZoneId.systemDefault())
        val day = zoned.dayOfMonth
        val month = esMonths[zoned.monthValue - 1]
        val year = zoned.year
        val time = zoned.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm", Locale.forLanguageTag("es")))
        "$day $month $year, $time"
    } catch (_: Exception) {
        iso
    }
}

package com.example.corpsyncmobile.ui.screens

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAddAlt1
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import com.example.corpsyncmobile.domain.model.TicketDetail
import com.example.corpsyncmobile.data.repository.AttachmentRepository
import com.example.corpsyncmobile.data.repository.CategoryRepository
import com.example.corpsyncmobile.data.repository.TicketRepository
import com.example.corpsyncmobile.ui.components.AppFooter
import com.example.corpsyncmobile.ui.components.BackIconButton
import com.example.corpsyncmobile.ui.components.BrandHeader
import com.example.corpsyncmobile.ui.components.DotBadge
import com.example.corpsyncmobile.ui.components.ErrorState
import com.example.corpsyncmobile.ui.components.ImageViewerDialog
import com.example.corpsyncmobile.ui.components.IconTile
import com.example.corpsyncmobile.ui.components.InfoSurface
import com.example.corpsyncmobile.ui.components.LoadingState
import com.example.corpsyncmobile.ui.components.PriorityPalettes
import com.example.corpsyncmobile.ui.components.SectionHeader
import com.example.corpsyncmobile.ui.components.StatusPalettes
import com.example.corpsyncmobile.ui.theme.BadgeTextStyle
import com.example.corpsyncmobile.ui.theme.BrandBorderWidth
import com.example.corpsyncmobile.ui.theme.BrandShapes
import com.example.corpsyncmobile.ui.theme.CorpIndigo
import com.example.corpsyncmobile.ui.theme.FooterGray
import com.example.corpsyncmobile.ui.theme.LabelGray
import com.example.corpsyncmobile.ui.theme.PageBackground
import com.example.corpsyncmobile.ui.theme.PlaceholderGray
import com.example.corpsyncmobile.ui.theme.SubtleDivider
import com.example.corpsyncmobile.ui.theme.TextPrimary
import kotlinx.coroutines.launch
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

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
                ticket = TicketRepository.getById(ticketId)
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
            isLoading && ticket == null -> Column(modifier = Modifier.fillMaxSize()) {
                SimpleTopBar(onBack = onBack)
                LoadingState()
            }
            errorMessage != null && ticket == null -> Column(modifier = Modifier.fillMaxSize()) {
                SimpleTopBar(onBack = onBack)
                ErrorState(message = errorMessage!!, onRetry = ::load)
            }
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
            InfoSurface { DescriptionText(text = ticket.descripcion) }

            val attachmentUrl = remember(ticket.imagenUrl) {
                AttachmentRepository.resolveTicketImage(ticket.imagenUrl)
            }
            if (attachmentUrl != null) {
                Spacer(modifier = Modifier.height(24.dp))
                SectionHeader("ADJUNTOS")
                Spacer(modifier = Modifier.height(12.dp))
                AttachmentThumb(url = attachmentUrl)
            }

            Spacer(modifier = Modifier.height(32.dp))
            AppFooter()
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun DetailHeader(ticket: TicketDetail, onBack: () -> Unit) {
    BrandHeader {
        Row(verticalAlignment = Alignment.CenterVertically) {
            BackIconButton(onClick = onBack)
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
            DotBadge(
                label = (CategoryRepository.estado[ticket.estado] ?: ticket.estado).uppercase(),
                palette = StatusPalettes.forEstado(ticket.estado)
            )
            val prioridad = ticket.prioridad
            if (prioridad != null && prioridad != "por_asignar") {
                val short = CategoryRepository.prioridad[prioridad] ?: prioridad
                DotBadge(
                    label = "PRIORIDAD ${short.uppercase()}",
                    palette = PriorityPalettes.forPrioridad(prioridad)
                )
            }
            val categoria = ticket.categoria
            if (categoria != null && categoria != "sin_categorizar") {
                CategoryBadge(categoria)
            }
        }
    }
}

@Composable
private fun CategoryBadge(categoria: String) {
    Box(
        modifier = Modifier
            .clip(BrandShapes.badge)
            .background(Color.White.copy(alpha = 0.12f))
            .border(
                width = BrandBorderWidth,
                color = Color.White.copy(alpha = 0.25f),
                shape = BrandShapes.badge
            )
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = (CategoryRepository.categoria[categoria] ?: categoria).uppercase(),
            color = Color.White,
            style = BadgeTextStyle
        )
    }
}

@Composable
private fun InfoCard(ticket: TicketDetail) {
    InfoSurface(contentPadding = PaddingValues(horizontal = 20.dp)) {
        Column {
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
}

@Composable
private fun InfoRow(icon: ImageVector, label: String, value: String, isMuted: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconTile(icon = icon)
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
private fun DescriptionText(text: String) {
    Text(
        text = text,
        color = TextPrimary,
        fontSize = 15.sp,
        lineHeight = 24.sp
    )
}

@Composable
private fun AttachmentThumb(url: String) {
    var viewerOpen by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .size(120.dp)
            .clip(BrandShapes.iconTile)
            .background(PlaceholderGray)
            .border(
                width = BrandBorderWidth,
                color = SubtleDivider,
                shape = BrandShapes.iconTile
            )
            .clickable { viewerOpen = true }
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

    if (viewerOpen) {
        ImageViewerDialog(
            url = url,
            onDismiss = { viewerOpen = false },
            contentDescription = "Adjunto del ticket"
        )
    }
}

@Composable
private fun SimpleTopBar(onBack: () -> Unit) {
    BrandHeader {
        Row(verticalAlignment = Alignment.CenterVertically) {
            BackIconButton(onClick = onBack)
            Spacer(modifier = Modifier.width(14.dp))
            Text(
                text = "Detalle del ticket",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
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

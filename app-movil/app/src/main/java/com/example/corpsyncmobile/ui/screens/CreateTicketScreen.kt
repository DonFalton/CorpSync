package com.example.corpsyncmobile.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.corpsyncmobile.data.TicketService
import com.example.corpsyncmobile.ui.theme.CorpIndigo
import com.example.corpsyncmobile.ui.theme.CorpIndigoPressed
import com.example.corpsyncmobile.ui.theme.FooterGray
import com.example.corpsyncmobile.ui.theme.InputBorder
import com.example.corpsyncmobile.ui.theme.LabelGray
import com.example.corpsyncmobile.ui.theme.PageBackground
import com.example.corpsyncmobile.ui.theme.SubtleDivider
import com.example.corpsyncmobile.ui.theme.TextPrimary
import kotlinx.coroutines.launch
import java.io.File

private val categorias = linkedMapOf(
    "sin_categorizar" to "Sin categorizar",
    "hardware" to "Hardware",
    "software" to "Software",
    "red" to "Red",
    "otro" to "Otros"
)

private val prioridades = linkedMapOf(
    "por_asignar" to "Por asignar",
    "baja" to "Baja",
    "media" to "Media",
    "alta" to "Alta",
    "critica" to "Crítica"
)

@Composable
fun CreateTicketScreen(
    onBack: () -> Unit = {},
    onCreated: () -> Unit = {}
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var titulo by rememberSaveable { mutableStateOf("") }
    var descripcion by rememberSaveable { mutableStateOf("") }
    var categoria by rememberSaveable { mutableStateOf("sin_categorizar") }
    var prioridad by rememberSaveable { mutableStateOf("por_asignar") }
    var photoUri by remember { mutableStateOf<Uri?>(null) }
    var pendingUri by remember { mutableStateOf<Uri?>(null) }
    var isLoading by rememberSaveable { mutableStateOf(false) }
    var errorMessage by rememberSaveable { mutableStateOf<String?>(null) }
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> hasCameraPermission = granted }

    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) photoUri = pendingUri
        pendingUri = null
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri -> if (uri != null) photoUri = uri }

    fun launchCamera() {
        val tempFile = File.createTempFile("ticket_", ".jpg", context.cacheDir)
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", tempFile)
        pendingUri = uri
        cameraLauncher.launch(uri)
    }

    fun submitTicket() {
        scope.launch {
            isLoading = true
            errorMessage = null
            try {
                val imageBytes = photoUri?.let { uri ->
                    context.contentResolver.openInputStream(uri)?.readBytes()
                        ?: throw Exception("No se pudo leer la imagen")
                }

                TicketService.create(
                    titulo = titulo,
                    descripcion = descripcion,
                    categoria = categoria,
                    prioridad = prioridad,
                    imageBytes = imageBytes
                )

                titulo = ""
                descripcion = ""
                categoria = "sin_categorizar"
                prioridad = "por_asignar"
                photoUri = null
                onCreated()
            } catch (e: Exception) {
                errorMessage = e.message ?: "No se pudo crear el ticket"
            } finally {
                isLoading = false
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PageBackground)
            .verticalScroll(rememberScrollState())
    ) {
        FormHeader()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 28.dp)
        ) {
            SectionLabel("Información")

            Spacer(modifier = Modifier.height(16.dp))

            FieldGroup(label = "Título") {
                StyledTextField(
                    value = titulo,
                    onValueChange = { titulo = it; errorMessage = null },
                    placeholder = "Introduce un título",
                    enabled = !isLoading,
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            FieldGroup(label = "Descripción") {
                StyledTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it; errorMessage = null },
                    placeholder = "Describe el problema",
                    enabled = !isLoading,
                    singleLine = false,
                    minHeight = 100.dp
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            FieldGroup(label = "Categoría") {
                DropdownField(
                    selected = categoria,
                    options = categorias,
                    onSelect = { categoria = it },
                    enabled = !isLoading
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            FieldGroup(label = "Prioridad") {
                DropdownField(
                    selected = prioridad,
                    options = prioridades,
                    onSelect = { prioridad = it },
                    enabled = !isLoading
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            FieldGroup(label = "Foto (opcional)") {
                PhotoSection(
                    context = context,
                    photoUri = photoUri,
                    isLoading = isLoading,
                    hasCameraPermission = hasCameraPermission,
                    onRequestCamera = {
                        if (hasCameraPermission) launchCamera()
                        else permissionLauncher.launch(Manifest.permission.CAMERA)
                    },
                    onPickGallery = { galleryLauncher.launch("image/*") },
                    onClear = { photoUri = null }
                )
            }

            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            SubmitButton(
                enabled = !isLoading && titulo.isNotBlank() && descripcion.isNotBlank(),
                isLoading = isLoading,
                onClick = ::submitTicket
            )

            Spacer(modifier = Modifier.height(20.dp))

            CancelLink(enabled = !isLoading, onClick = onBack)

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "CorpSync ITSM · v1.0 · DAM 2025–2026",
                color = FooterGray,
                fontSize = 12.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun FormHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(CorpIndigo)
            .padding(horizontal = 28.dp, vertical = 32.dp)
    ) {
        Text(
            text = "Nuevo Ticket",
            color = Color.White,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Completa el formulario para reportar un problema",
            color = Color.White.copy(alpha = 0.85f),
            fontSize = 14.sp
        )
    }
}

@Composable
private fun SectionLabel(text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(CorpIndigo)
        )
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = text.uppercase(),
            color = CorpIndigo,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
private fun FieldGroup(label: String, content: @Composable () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label.uppercase(),
            color = LabelGray,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.5.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        content()
    }
}

@Composable
private fun StyledTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    enabled: Boolean,
    singleLine: Boolean,
    minHeight: androidx.compose.ui.unit.Dp? = null
) {
    val base = Modifier.fillMaxWidth()
    val modifier = if (minHeight != null) base.heightIn(min = minHeight) else base
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder, color = FooterGray, fontSize = 15.sp) },
        singleLine = singleLine,
        shape = RoundedCornerShape(10.dp),
        colors = styledInputColors(),
        modifier = modifier,
        enabled = enabled
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DropdownField(
    selected: String,
    options: Map<String, String>,
    onSelect: (String) -> Unit,
    enabled: Boolean
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { if (enabled) expanded = it }
    ) {
        OutlinedTextField(
            value = options[selected] ?: selected,
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            shape = RoundedCornerShape(10.dp),
            colors = styledInputColors(),
            modifier = Modifier
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, enabled)
                .fillMaxWidth(),
            enabled = enabled
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { (value, displayLabel) ->
                DropdownMenuItem(
                    text = { Text(displayLabel) },
                    onClick = { onSelect(value); expanded = false }
                )
            }
        }
    }
}

@Composable
private fun PhotoSection(
    context: android.content.Context,
    photoUri: Uri?,
    isLoading: Boolean,
    hasCameraPermission: Boolean,
    onRequestCamera: () -> Unit,
    onPickGallery: () -> Unit,
    onClear: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        val bitmap = remember(photoUri) {
            photoUri?.let {
                context.contentResolver.openInputStream(it)?.use { stream ->
                    BitmapFactory.decodeStream(stream)?.asImageBitmap()
                }
            }
        }
        if (bitmap != null) {
            Image(
                bitmap = bitmap,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(4f / 3f)
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.5.dp, SubtleDivider, RoundedCornerShape(12.dp))
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(
                onClick = onClear,
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = CorpIndigo),
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            ) { Text("Eliminar foto", fontWeight = FontWeight.SemiBold) }
            Spacer(modifier = Modifier.height(12.dp))
        }

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            PhotoPillButton(
                label = if (hasCameraPermission) "Cámara" else "Permitir cámara",
                enabled = !isLoading,
                onClick = onRequestCamera,
                modifier = Modifier.weight(1f)
            )
            PhotoPillButton(
                label = "Galería",
                enabled = !isLoading,
                onClick = onPickGallery,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun PhotoPillButton(
    label: String,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        shape = RoundedCornerShape(24.dp),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = LabelGray),
        modifier = modifier.height(48.dp),
        enabled = enabled
    ) {
        Text(label, fontSize = 14.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun SubmitButton(enabled: Boolean, isLoading: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(28.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = CorpIndigo,
            contentColor = Color.White,
            disabledContainerColor = CorpIndigoPressed.copy(alpha = 0.5f),
            disabledContentColor = Color.White.copy(alpha = 0.8f)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        enabled = enabled
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                strokeWidth = 2.dp,
                color = Color.White
            )
        } else {
            Text(
                text = "Enviar ticket",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.size(8.dp))
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
private fun CancelLink(enabled: Boolean, onClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(SubtleDivider)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Cancelar",
            color = LabelGray,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = enabled, onClick = onClick),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun styledInputColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = CorpIndigo,
    unfocusedBorderColor = InputBorder,
    disabledBorderColor = InputBorder,
    focusedTextColor = TextPrimary,
    unfocusedTextColor = TextPrimary,
    cursorColor = CorpIndigo,
    focusedContainerColor = Color.White,
    unfocusedContainerColor = Color.White,
    disabledContainerColor = Color.White
)

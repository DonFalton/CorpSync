package com.example.corpsyncmobile

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.launch
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.io.File

@Serializable
private data class NewTicket(
    val titulo: String,
    val descripcion: String,
    val categoria: String,
    val prioridad: String,
    @SerialName("empleado_id") val empleadoId: String,
    @SerialName("imagen_url") val imagenUrl: String? = null
)

private val categorias = listOf("sin_categorizar", "hardware", "software", "red", "otro")
private val prioridades = listOf("por_asignar", "baja", "media", "alta", "critica")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTicketScreen() {
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
    var successMessage by rememberSaveable { mutableStateOf<String?>(null) }
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
            successMessage = null
            try {
                val userId = supabase.auth.currentUserOrNull()?.id
                    ?: throw Exception("Not authenticated")

                var imageUrl: String? = null
                photoUri?.let { uri ->
                    val bytes = context.contentResolver.openInputStream(uri)?.readBytes()
                        ?: throw Exception("Could not read image")
                    val fileName = "ticket_${System.currentTimeMillis()}.jpg"
                    supabase.storage.from("ticket-images").upload(fileName, bytes)
                    imageUrl = supabase.storage.from("ticket-images").publicUrl(fileName)
                }

                supabase.from("tickets").insert(
                    NewTicket(
                        titulo = titulo.trim(),
                        descripcion = descripcion.trim(),
                        categoria = categoria,
                        prioridad = prioridad,
                        empleadoId = userId,
                        imagenUrl = imageUrl
                    )
                )

                successMessage = "Ticket created successfully"
                titulo = ""
                descripcion = ""
                categoria = "sin_categorizar"
                prioridad = "por_asignar"
                photoUri = null
            } catch (e: Exception) {
                errorMessage = e.message ?: "Failed to create ticket"
            } finally {
                isLoading = false
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {

        // Colored header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .padding(horizontal = 24.dp, vertical = 20.dp)
        ) {
            Column {
                Text(
                    text = "Nuevo Ticket",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Completa el formulario para reportar un problema",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                )
            }
        }

        // Scrollable form
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = "Información",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )

            OutlinedTextField(
                value = titulo,
                onValueChange = { titulo = it; errorMessage = null; successMessage = null },
                label = { Text("Título") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            )

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it; errorMessage = null; successMessage = null },
                label = { Text("Descripción") },
                minLines = 3,
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            )

            DropdownField(
                label = "Categoría",
                selected = categoria,
                options = categorias,
                onSelect = { categoria = it },
                enabled = !isLoading
            )

            DropdownField(
                label = "Prioridad",
                selected = prioridad,
                options = prioridades,
                onSelect = { prioridad = it },
                enabled = !isLoading
            )

            Text(
                text = "Foto (opcional)",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )

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
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp))
                )
                OutlinedButton(
                    onClick = { photoUri = null },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading
                ) { Text("Eliminar foto") }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(
                    onClick = {
                        if (hasCameraPermission) launchCamera()
                        else permissionLauncher.launch(Manifest.permission.CAMERA)
                    },
                    modifier = Modifier.weight(1f),
                    enabled = !isLoading
                ) { Text(if (hasCameraPermission) "Cámara" else "Permitir cámara") }

                OutlinedButton(
                    onClick = { galleryLauncher.launch("image/*") },
                    modifier = Modifier.weight(1f),
                    enabled = !isLoading
                ) { Text("Galería") }
            }

            if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            if (successMessage != null) {
                Text(
                    text = successMessage!!,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Button(
                onClick = { submitTicket() },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading && titulo.isNotBlank() && descripcion.isNotBlank()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Enviar ticket")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DropdownField(
    label: String,
    selected: String,
    options: List<String>,
    onSelect: (String) -> Unit,
    enabled: Boolean
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { if (enabled) expanded = it }
    ) {
        OutlinedTextField(
            value = selected,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            enabled = enabled
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = { onSelect(option); expanded = false }
                )
            }
        }
    }
}

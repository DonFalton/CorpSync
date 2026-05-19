package com.example.corpsyncmobile.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.corpsyncmobile.data.repository.AuthRepository
import com.example.corpsyncmobile.data.repository.ProfileRepository
import com.example.corpsyncmobile.ui.components.AppFooter
import com.example.corpsyncmobile.ui.components.BrandHeader
import com.example.corpsyncmobile.ui.components.BrandOutlinedTextField
import com.example.corpsyncmobile.ui.components.FieldLabel
import com.example.corpsyncmobile.ui.components.PrimaryActionButton
import com.example.corpsyncmobile.ui.theme.BrandShapes
import com.example.corpsyncmobile.ui.theme.LabelGray
import com.example.corpsyncmobile.ui.theme.PageBackground
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onAccessDenied: () -> Unit
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var isLoading by rememberSaveable { mutableStateOf(false) }
    var errorMessage by rememberSaveable { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PageBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 480.dp),
                shape = BrandShapes.cardLarge,
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                BrandHeader(
                    contentPadding = PaddingValues(horizontal = 28.dp, vertical = 36.dp)
                ) {
                    LoginKicker()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Acceso a CorpSync",
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Introduce tus credenciales corporativas para continuar",
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 14.sp
                    )
                }

                Column(modifier = Modifier.padding(horizontal = 28.dp, vertical = 32.dp)) {
                    FieldLabel("Correo corporativo")
                    Spacer(modifier = Modifier.height(8.dp))
                    BrandOutlinedTextField(
                        value = email,
                        onValueChange = { email = it; errorMessage = null },
                        placeholder = "nombre@empresa.com",
                        enabled = !isLoading,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        leadingIcon = {
                            Icon(Icons.Filled.Email, contentDescription = null, tint = LabelGray)
                        }
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    FieldLabel("Contraseña")
                    Spacer(modifier = Modifier.height(8.dp))
                    BrandOutlinedTextField(
                        value = password,
                        onValueChange = { password = it; errorMessage = null },
                        placeholder = "••••••••",
                        enabled = !isLoading,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        leadingIcon = {
                            Icon(Icons.Filled.Lock, contentDescription = null, tint = LabelGray)
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = { passwordVisible = !passwordVisible },
                                enabled = !isLoading
                            ) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                    contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                                    tint = LabelGray
                                )
                            }
                        }
                    )

                    if (errorMessage != null) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = errorMessage!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    PrimaryActionButton(
                        text = "Iniciar sesión",
                        isLoading = isLoading,
                        enabled = email.isNotBlank() && password.isNotBlank(),
                        onClick = {
                            scope.launch {
                                isLoading = true
                                errorMessage = null
                                try {
                                    AuthRepository.signIn(email, password)
                                    val role = ProfileRepository.currentRole()
                                    if (role == ProfileRepository.ROLE_EMPLOYEE) {
                                        onLoginSuccess()
                                    } else {
                                        runCatching { AuthRepository.signOut() }
                                        onAccessDenied()
                                    }
                                } catch (e: Exception) {
                                    errorMessage = e.message ?: "Error al iniciar sesión. Intenta de nuevo."
                                } finally {
                                    isLoading = false
                                }
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            AppFooter()
        }
    }
}

@Composable
private fun LoginKicker() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(Color.White)
        )
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = "APP PARA EMPLEADOS",
            color = Color.White.copy(alpha = 0.9f),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.8.sp
        )
    }
}

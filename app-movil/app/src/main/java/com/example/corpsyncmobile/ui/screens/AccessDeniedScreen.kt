package com.example.corpsyncmobile.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.corpsyncmobile.ui.theme.CorpIndigo
import com.example.corpsyncmobile.ui.theme.FooterGray
import com.example.corpsyncmobile.ui.theme.InputBorder
import com.example.corpsyncmobile.ui.theme.LabelGray
import com.example.corpsyncmobile.ui.theme.PageBackground
import com.example.corpsyncmobile.ui.theme.SubtleDivider
import com.example.corpsyncmobile.ui.theme.TextPrimary

private val LockBg = Color(0xFFFEF2F2)
private val LockBorder = Color(0xFFFECACA)
private val LockFg = Color(0xFFDC2626)
private val ErrorBadgeFg = Color(0xFFB91C1C)

private val InfoCardBg = Color(0xFFF8FAFC)
private val ContactIconBg = Color(0xFFEEF2FF)
private val ContactIconBorder = Color(0xFFC7D2FE)

private const val SUPPORT_EMAIL = "soporte@corpsync.com"

@Composable
fun AccessDeniedScreen(onBackToLogin: () -> Unit) {
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
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 28.dp, vertical = 40.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AppIcon()
                    Spacer(modifier = Modifier.height(32.dp))

                    LockIcon()
                    Spacer(modifier = Modifier.height(24.dp))

                    ErrorBadge()
                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "Sin permisos de acceso",
                        color = TextPrimary,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        lineHeight = 30.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    DescriptionText()

                    Spacer(modifier = Modifier.height(28.dp))

                    ContactCard()

                    Spacer(modifier = Modifier.height(20.dp))

                    BackToLoginButton(onClick = onBackToLogin)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "CorpSync ITSM · v1.0 · DAM 2025–2026",
                color = FooterGray,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun AppIcon() {
    Box(
        modifier = Modifier
            .size(72.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(CorpIndigo),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "CS",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
private fun LockIcon() {
    Box(
        modifier = Modifier
            .size(110.dp)
            .clip(CircleShape)
            .background(LockBg)
            .border(width = 2.dp, color = LockBorder, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Filled.Lock,
            contentDescription = null,
            tint = LockFg,
            modifier = Modifier.size(48.dp)
        )
    }
}

@Composable
private fun ErrorBadge() {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(24.dp))
            .background(LockBg)
            .border(width = 1.5.dp, color = LockBorder, shape = RoundedCornerShape(24.dp))
            .padding(horizontal = 18.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(7.dp)
                .clip(CircleShape)
                .background(LockFg)
        )
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = "ERROR 403 · ACCESO DENEGADO",
            color = ErrorBadgeFg,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.8.sp
        )
    }
}

@Composable
private fun DescriptionText() {
    val highlighted = buildAnnotatedString {
        append("Tu cuenta no tiene permisos para usar la app móvil de CorpSync. Esta app está reservada para ")
        withStyle(SpanStyle(color = CorpIndigo, fontWeight = FontWeight.SemiBold)) {
            append("empleados")
        }
        append(". Contacta con tu administrador para solicitar acceso.")
    }
    Text(
        text = highlighted,
        color = LabelGray,
        fontSize = 15.sp,
        lineHeight = 24.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(horizontal = 8.dp)
    )
}

@Composable
private fun ContactCard() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(InfoCardBg)
            .border(width = 1.5.dp, color = SubtleDivider, shape = RoundedCornerShape(14.dp))
            .padding(horizontal = 18.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(ContactIconBg)
                .border(width = 1.5.dp, color = ContactIconBorder, shape = RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Email,
                contentDescription = null,
                tint = CorpIndigo,
                modifier = Modifier.size(22.dp)
            )
        }
        Spacer(modifier = Modifier.size(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "CONTACTA CON EL ADMINISTRADOR",
                color = LabelGray,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = SUPPORT_EMAIL,
                color = TextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun BackToLoginButton(onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        shape = RoundedCornerShape(28.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.White,
            contentColor = CorpIndigo
        ),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, InputBorder),
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = null,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = "Volver al inicio de sesión",
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

package com.example.corpsyncmobile.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.corpsyncmobile.ui.theme.BrandIconButtonSize
import com.example.corpsyncmobile.ui.theme.BrandShapes

@Composable
fun BrandIconButton(
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String? = null
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .size(BrandIconButtonSize)
            .clip(BrandShapes.field)
            .background(Color.White.copy(alpha = 0.15f))
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = Color.White
        )
    }
}

@Composable
fun BackIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String = "Volver"
) = BrandIconButton(
    icon = Icons.AutoMirrored.Filled.ArrowBack,
    onClick = onClick,
    modifier = modifier,
    contentDescription = contentDescription
)

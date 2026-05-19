package com.example.corpsyncmobile.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.corpsyncmobile.ui.theme.BrandBorderWidth
import com.example.corpsyncmobile.ui.theme.BrandShapes
import com.example.corpsyncmobile.ui.theme.CorpIndigo
import com.example.corpsyncmobile.ui.theme.InfoIconBg
import com.example.corpsyncmobile.ui.theme.InfoIconBorder

@Composable
fun IconTile(
    icon: ImageVector,
    modifier: Modifier = Modifier,
    size: Dp = 44.dp,
    iconSize: Dp = 22.dp,
    contentDescription: String? = null
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(BrandShapes.iconTile)
            .background(InfoIconBg)
            .border(
                width = BrandBorderWidth,
                color = InfoIconBorder,
                shape = BrandShapes.iconTile
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = CorpIndigo,
            modifier = Modifier.size(iconSize)
        )
    }
}

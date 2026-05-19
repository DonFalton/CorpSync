package com.example.corpsyncmobile.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.corpsyncmobile.ui.theme.BrandBorderWidth
import com.example.corpsyncmobile.ui.theme.BrandShapes
import com.example.corpsyncmobile.ui.theme.InfoCardBg
import com.example.corpsyncmobile.ui.theme.SubtleDivider

@Composable
fun InfoSurface(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(horizontal = 20.dp, vertical = 18.dp),
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(BrandShapes.card)
            .background(InfoCardBg)
            .border(
                width = BrandBorderWidth,
                color = SubtleDivider,
                shape = BrandShapes.card
            )
            .padding(contentPadding)
    ) {
        content()
    }
}

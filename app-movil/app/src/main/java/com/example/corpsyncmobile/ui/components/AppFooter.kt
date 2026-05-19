package com.example.corpsyncmobile.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.corpsyncmobile.ui.theme.FooterGray

@Composable
fun AppFooter(modifier: Modifier = Modifier) {
    Text(
        text = "CorpSync ITSM · v1.0 · DAM 2025–2026",
        color = FooterGray,
        fontSize = 12.sp,
        modifier = modifier.fillMaxWidth(),
        textAlign = TextAlign.Center
    )
}

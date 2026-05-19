package com.example.corpsyncmobile.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.corpsyncmobile.ui.theme.LabelGray
import com.example.corpsyncmobile.ui.theme.SectionHeaderTextStyle

@Composable
fun SectionHeader(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        color = LabelGray,
        style = SectionHeaderTextStyle,
        modifier = modifier
    )
}

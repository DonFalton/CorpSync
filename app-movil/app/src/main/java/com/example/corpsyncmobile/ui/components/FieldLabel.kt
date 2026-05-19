package com.example.corpsyncmobile.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.corpsyncmobile.ui.theme.FieldLabelTextStyle
import com.example.corpsyncmobile.ui.theme.LabelGray

@Composable
fun FieldLabel(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text.uppercase(),
        color = LabelGray,
        style = FieldLabelTextStyle,
        modifier = modifier
    )
}

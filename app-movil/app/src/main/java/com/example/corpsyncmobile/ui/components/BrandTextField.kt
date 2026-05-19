package com.example.corpsyncmobile.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import com.example.corpsyncmobile.ui.theme.BrandShapes
import com.example.corpsyncmobile.ui.theme.CorpIndigo
import com.example.corpsyncmobile.ui.theme.FooterGray
import com.example.corpsyncmobile.ui.theme.InputBorder
import com.example.corpsyncmobile.ui.theme.TextPrimary

@Composable
fun brandTextFieldColors() = OutlinedTextFieldDefaults.colors(
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

@Composable
fun BrandOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    singleLine: Boolean = true,
    minHeight: Dp? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    val sizedModifier = if (minHeight != null) {
        modifier.fillMaxWidth().heightIn(min = minHeight)
    } else {
        modifier.fillMaxWidth()
    }
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder, color = FooterGray, fontSize = 15.sp) },
        singleLine = singleLine,
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformation,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        shape = BrandShapes.field,
        colors = brandTextFieldColors(),
        modifier = sizedModifier,
        enabled = enabled
    )
}

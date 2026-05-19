package com.example.corpsyncmobile.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.corpsyncmobile.ui.theme.BrandPrimaryButtonHeight
import com.example.corpsyncmobile.ui.theme.BrandShapes
import com.example.corpsyncmobile.ui.theme.CorpIndigo
import com.example.corpsyncmobile.ui.theme.CorpIndigoPressed

@Composable
fun PrimaryActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    trailingIcon: ImageVector? = Icons.AutoMirrored.Filled.ArrowForward
) {
    Button(
        onClick = onClick,
        shape = BrandShapes.button,
        colors = ButtonDefaults.buttonColors(
            containerColor = CorpIndigo,
            contentColor = Color.White,
            disabledContainerColor = CorpIndigoPressed.copy(alpha = 0.5f),
            disabledContentColor = Color.White.copy(alpha = 0.8f)
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(BrandPrimaryButtonHeight),
        enabled = enabled && !isLoading
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                strokeWidth = 2.dp,
                color = Color.White
            )
        } else {
            Text(
                text = text,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
            if (trailingIcon != null) {
                Spacer(modifier = Modifier.size(8.dp))
                Icon(
                    imageVector = trailingIcon,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

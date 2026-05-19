package com.example.corpsyncmobile.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.corpsyncmobile.ui.theme.BrandShapes
import com.example.corpsyncmobile.ui.theme.CorpIndigo

@Composable
fun ErrorState(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    retryLabel: String = "Reintentar"
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = message,
                color = MaterialTheme.colorScheme.error,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButton(
                onClick = onRetry,
                shape = BrandShapes.button,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = CorpIndigo),
                modifier = Modifier.height(44.dp)
            ) {
                Text(text = retryLabel, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

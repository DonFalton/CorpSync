package com.example.corpsyncmobile.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.corpsyncmobile.ui.theme.BadgeTextStyle
import com.example.corpsyncmobile.ui.theme.BrandBorderWidth
import com.example.corpsyncmobile.ui.theme.BrandShapes
import com.example.corpsyncmobile.ui.theme.SoftBlueBg
import com.example.corpsyncmobile.ui.theme.SoftBlueBorder
import com.example.corpsyncmobile.ui.theme.SoftBlueFg
import com.example.corpsyncmobile.ui.theme.SoftGreenBg
import com.example.corpsyncmobile.ui.theme.SoftGreenBorder
import com.example.corpsyncmobile.ui.theme.SoftGreenFg
import com.example.corpsyncmobile.ui.theme.SoftOrangeBg
import com.example.corpsyncmobile.ui.theme.SoftOrangeBorder
import com.example.corpsyncmobile.ui.theme.SoftOrangeFg
import com.example.corpsyncmobile.ui.theme.SoftRedBg
import com.example.corpsyncmobile.ui.theme.SoftRedBorder
import com.example.corpsyncmobile.ui.theme.SoftRedFg
import com.example.corpsyncmobile.ui.theme.SoftYellowBg
import com.example.corpsyncmobile.ui.theme.SoftYellowBorder
import com.example.corpsyncmobile.ui.theme.SoftYellowFg

data class BadgePalette(val bg: Color, val fg: Color, val border: Color)

object StatusPalettes {
    val Open = BadgePalette(SoftOrangeBg, SoftOrangeFg, SoftOrangeBorder)
    val Progress = BadgePalette(SoftBlueBg, SoftBlueFg, SoftBlueBorder)
    val Resolved = BadgePalette(SoftGreenBg, SoftGreenFg, SoftGreenBorder)

    fun forEstado(estado: String): BadgePalette = when (estado) {
        "resuelto" -> Resolved
        "en_proceso" -> Progress
        else -> Open
    }
}

object PriorityPalettes {
    val Critica = BadgePalette(SoftRedBg, SoftRedFg, SoftRedBorder)
    val Alta = BadgePalette(SoftOrangeBg, SoftOrangeFg, SoftOrangeBorder)
    val Media = BadgePalette(SoftYellowBg, SoftYellowFg, SoftYellowBorder)
    val Baja = BadgePalette(SoftGreenBg, SoftGreenFg, SoftGreenBorder)

    fun forPrioridad(prioridad: String): BadgePalette = when (prioridad) {
        "critica" -> Critica
        "alta" -> Alta
        "media" -> Media
        "baja" -> Baja
        else -> StatusPalettes.Open
    }
}

@Composable
fun DotBadge(label: String, palette: BadgePalette, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .clip(BrandShapes.badge)
            .background(palette.bg)
            .border(
                width = BrandBorderWidth,
                color = palette.border,
                shape = BrandShapes.badge
            )
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(palette.fg)
        )
        Spacer(modifier = Modifier.size(6.dp))
        Text(
            text = label,
            color = palette.fg,
            style = BadgeTextStyle
        )
    }
}

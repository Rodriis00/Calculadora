package com.rodrigodev.calculadora.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rodrigodev.calculadora.ui.theme.ButtonGray
import com.rodrigodev.calculadora.ui.theme.TextPrimary

@Composable
fun CalculatorButton(
    symbol: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = ButtonGray,
    textColor: Color = TextPrimary,
    onClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .padding(8.dp)
            .aspectRatio(1f)
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable { onClick() }
    ) {
        Text(
            text = symbol,
            fontSize = 28.sp,
            fontWeight = FontWeight.Medium,
            color = textColor
        )
    }
}
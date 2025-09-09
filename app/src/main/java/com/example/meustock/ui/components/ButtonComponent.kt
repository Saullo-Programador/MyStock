package com.example.meustock.ui.components


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun ButtonComponent(
    modifier: Modifier = Modifier,
    text: String,
    fontSize: Int = 16,
    onClick: () -> Unit,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    iconVector: ImageVector? = null,
    iconPainter: Painter? = null,
    iconPosition: ButtonIconPosition = ButtonIconPosition.START,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    fontColor: Color = Color.White,
    cornerRadius: Int = 15
) {

    Button(
        onClick = { if (!isLoading) onClick() },
        modifier = modifier
            .height(50.dp)
            .fillMaxWidth(),
        enabled = enabled,
        colors = colors,
        shape = RoundedCornerShape(cornerRadius.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = Color.White,
                modifier = Modifier.size(24.dp),
                strokeWidth = 2.dp
            )
        } else {
            if (iconVector != null && iconPosition == ButtonIconPosition.START) {
                Icon(
                    imageVector = iconVector,
                    contentDescription = null,
                    tint = fontColor,
                    modifier = Modifier.padding(end = 8.dp)
                )
            } else if (iconPainter != null && iconPosition == ButtonIconPosition.START) {
                Icon(
                    painter = iconPainter,
                    contentDescription = null,
                    tint = fontColor,
                    modifier = Modifier.padding(end = 8.dp)
                )
            }


            Text(text, fontSize = fontSize.sp, color = fontColor)

            if (iconVector != null && iconPosition == ButtonIconPosition.END) {
                Icon(
                    imageVector = iconVector,
                    contentDescription = null,
                    modifier = Modifier.padding(start = 8.dp)
                )
            } else if(iconPainter != null && iconPosition == ButtonIconPosition.END) {
                Icon(
                    painter = iconPainter,
                    contentDescription = null,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}

enum class ButtonIconPosition { START, END }


@Composable
fun ButtonGradient(
    gradientBrush: Brush = Brush.linearGradient(
        colors = listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.secondary),
        start = Offset.Zero,
        end = Offset.Infinite
    ),
    onClick: () -> Unit,
    text: String,
    fontSize: Int = 16,
    cornerRadius: Int = 15,
    fontColor: Color = Color.White
){
    Box(
        modifier = Modifier
            .background(brush = gradientBrush, shape = RoundedCornerShape(cornerRadius.dp))
            .clickable { onClick() }
            .height(60.dp)
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = text, fontSize = fontSize.sp, color = fontColor)
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = fontColor,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CustomButtonPreview() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ButtonComponent(
            text = "Confirmar",
            onClick = { /* TODO */ },
            iconVector = Icons.Default.Check,
            cornerRadius = 8,
        )
        ButtonComponent(
            text = "Carregando...",
            onClick = { /* TODO */ },
            isLoading = true,
            cornerRadius = 20
        )

        ButtonComponent(
            text = "Excluir",
            onClick = { /* TODO */ },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
            iconVector = Icons.Default.Delete,
            iconPosition = ButtonIconPosition.END,
            cornerRadius = 0
        )
    }
}


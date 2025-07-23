package com.example.meustock.ui.components


import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun FloatingButton(
    onClick: () -> Unit,
    icon: ImageVector,
    contentDescription: String?,
    iconTint: Color = Color.White,
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape = RoundedCornerShape(50.dp)
){
    FloatingActionButton(
        onClick = onClick,
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        modifier = modifier,
        shape = shape
    ){
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = iconTint
        )

    }

}
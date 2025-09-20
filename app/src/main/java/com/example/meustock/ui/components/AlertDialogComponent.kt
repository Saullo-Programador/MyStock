package com.example.meustock.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp

@Composable
fun AlertDialogComponent(
    onDismissRequest: () -> Unit,
    textDismiss: String,
    onConfirmation: () -> Unit,
    containerColor: Color = MaterialTheme.colorScheme.background,
    textConfirmation: String,
    dialogTitle: String,
    dialogText: @Composable () -> Unit,
    icon: Painter,
    tint: Color = MaterialTheme.colorScheme.primary,
    colorButtonConfirmation: Color = MaterialTheme.colorScheme.primary,
){
    AlertDialog(
        containerColor = containerColor,
        icon = {
            Icon(
                icon,
                contentDescription = "Example Icon",
                tint = tint,
                modifier = Modifier.size(50.dp)
            )
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            dialogText()
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorButtonConfirmation,
                    contentColor = Color.White
                ),
                shape = MaterialTheme.shapes.medium,
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 4.dp,
                    pressedElevation = 8.dp
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = colorButtonConfirmation
                ),
                onClick = {
                    onConfirmation()
                }
            ) {
                Text(text = textConfirmation)
            }
        },
        dismissButton = {
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onBackground
                ),
                shape = MaterialTheme.shapes.medium,
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 4.dp,
                    pressedElevation = 8.dp
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onBackground
                ),
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text(text = textDismiss)
            }
        }
    )
}
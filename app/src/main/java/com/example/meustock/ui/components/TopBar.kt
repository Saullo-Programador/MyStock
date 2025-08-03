package com.example.meustock.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: String,
    onNavigationIconClick: () -> Unit= {},
    trailingIconVector: ImageVector? = null,
    trailingIconPainter: Painter? = null,
    onTrailingIconClick: () -> Unit = {},
    colorBackground: Color = MaterialTheme.colorScheme.primary
) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = colorBackground,
            titleContentColor = MaterialTheme.colorScheme.onBackground,
        ),
        title = {
            Text(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            IconButton(onClick = { onNavigationIconClick() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    tint = MaterialTheme.colorScheme.onBackground,
                    contentDescription = "onNavigationIconClick TopBar"
                )
            }
        },
        actions = {
            IconButton(onClick = { onTrailingIconClick() }) {
                if (trailingIconVector != null) {
                    Icon(
                        imageVector = trailingIconVector,
                        tint = MaterialTheme.colorScheme.onBackground,
                        contentDescription = "onTrailingIconClick TopBar"
                    )
                } else if (trailingIconPainter != null) {
                    Icon(
                        painter = trailingIconPainter,
                        tint = MaterialTheme.colorScheme.onBackground,
                        contentDescription = "onTrailingIconClick TopBar"
                    )
                }
            }
        },
    )
}
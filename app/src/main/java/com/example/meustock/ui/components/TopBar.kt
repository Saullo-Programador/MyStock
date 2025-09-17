package com.example.meustock.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: String,
    colorTitle: Color = MaterialTheme.colorScheme.onBackground,
    onNavigationIconClick: () -> Unit= {},
    trailingIconVector: ImageVector? = null,
    trailingIconPainter: Painter? = null,
    colorTrailingIcon: Color = MaterialTheme.colorScheme.onBackground,
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
                overflow = TextOverflow.Ellipsis,
                style = TextStyle(
                    color = colorTitle,
                    fontSize = 21.sp,
                )
            )
        },
        navigationIcon = {
            IconButton(onClick = { onNavigationIconClick() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowLeft,
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(35.dp),
                    contentDescription = "onNavigationIconClick TopBar"
                )
            }
        },
        actions = {
            IconButton(onClick = { onTrailingIconClick() }) {
                if (trailingIconVector != null) {
                    Icon(
                        imageVector = trailingIconVector,
                        tint = colorTrailingIcon,
                        contentDescription = "onTrailingIconClick TopBar"
                    )
                } else if (trailingIconPainter != null) {
                    Icon(
                        painter = trailingIconPainter,
                        tint = colorTrailingIcon,
                        contentDescription = "onTrailingIconClick TopBar"
                    )
                }
            }
        },
    )
}
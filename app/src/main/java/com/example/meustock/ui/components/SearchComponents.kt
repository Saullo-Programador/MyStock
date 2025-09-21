package com.example.meustock.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchComponents(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    searchResults: List<String>,
    onResultClick: (String) -> Unit,
    placeholder: String = "Search",
    leadingIcon: Painter,
    trailingIcon: @Composable (() -> Unit)? = null,
    supportingContent: (@Composable (String) -> Unit)? = null,
    leadingContent: (@Composable () -> Unit)? = null,
    modifier: Modifier = Modifier,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    cornerRadius: Int = 15
){
    SearchBar(
        inputField = { 
            SearchBarDefaults.InputField(
                query = query,
                onQueryChange = onQueryChange,
                onSearch = {
                    onSearch(query)
                    expanded
                },
                expanded = expanded,
                onExpandedChange = onExpandedChange,
                placeholder = { Text( text = placeholder) },
                leadingIcon = {
                    Icon(
                        painter = leadingIcon,
                        contentDescription = null
                    )
                },
                trailingIcon = trailingIcon,
                colors = SearchBarDefaults.inputFieldColors(
                    cursorColor = MaterialTheme.colorScheme.primary,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                )
            ) 
        },
        modifier = modifier,
        colors = SearchBarDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        expanded = expanded,
        onExpandedChange = onExpandedChange,
        shape = RoundedCornerShape(cornerRadius.dp),
    ) {
        LazyColumn {
            items(count = searchResults.size) { index ->
                val resultText = searchResults[index]
                ListItem(
                    headlineContent = { Text(resultText) },
                    supportingContent = supportingContent?.let { { it(resultText) } },
                    leadingContent = leadingContent,
                    colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                    modifier = modifier
                        .clickable {
                            onResultClick(resultText)
                            expanded
                        }
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }
        }

    }
}
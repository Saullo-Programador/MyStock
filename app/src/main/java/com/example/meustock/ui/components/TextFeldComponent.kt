package com.example.meustock.ui.components


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.meustock.R

@Composable
fun TextFeldComponent(
    value: String?,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "",
    placeholder: String = "",
    isError: Boolean = false,
    errorMessage: String? = null,
    leadingIcon: ImageVector? = null,
    tintLeadingIcon: Color = MaterialTheme.colorScheme.onBackground,
    trailingIcon: ImageVector? = null,
    onTrailingIconClick: (() -> Unit)? = null,
    tintTrailingIcon: Color = MaterialTheme.colorScheme.onBackground,
    isPasswordField: Boolean = false,
    shape: RoundedCornerShape = RoundedCornerShape(8.dp),
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    colors: TextFieldColors = TextFieldDefaults.colors(
        cursorColor = MaterialTheme.colorScheme.primary,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent,
        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
        errorContainerColor = MaterialTheme.colorScheme.error,
        errorIndicatorColor = MaterialTheme.colorScheme.error
    )
) {
    var passwordVisibility by remember { mutableStateOf(!isPasswordField) }

    TextField(
        value = value ?: "",
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        label = { if (label.isNotEmpty()) Text(label) },
        placeholder = { if (placeholder.isNotEmpty()) Text(placeholder) },
        isError = isError,
        visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = keyboardOptions.copy(
            keyboardType = if (isPasswordField) KeyboardType.Password else keyboardOptions.keyboardType
        ),
        keyboardActions = keyboardActions,
        leadingIcon = leadingIcon?.let {
            { Icon(imageVector = it, contentDescription = null, tint = tintLeadingIcon) }
        },
        trailingIcon = {
            if (isPasswordField) {
                IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                    Icon(
                        painter = if (passwordVisibility) painterResource(R.drawable.icon_visibility) else painterResource(R.drawable.icon_visibility_off),
                        contentDescription = if (passwordVisibility) "Ocultar senha" else "Mostrar senha",
                        tint = tintTrailingIcon
                    )
                }
            } else if (trailingIcon != null && onTrailingIconClick != null) {
                if (value?.isNotEmpty() ?: true){
                    IconButton(onClick = onTrailingIconClick) {
                        Icon(
                            imageVector = trailingIcon,
                            contentDescription = null, tint = tintTrailingIcon)
                    }
                }
            }
        },
        singleLine = true,
        shape = shape,
        colors = colors
    )

    if (isError && !errorMessage.isNullOrEmpty()) {
        Text(
            text = errorMessage,
            color = MaterialTheme.colorScheme.error,
            fontSize = 12.sp,
            modifier = Modifier.padding(start = 16.dp, top = 4.dp)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun CustomTextFieldPreview() {
    var text by remember { mutableStateOf("") }

    MaterialTheme {
        Column(modifier = Modifier.padding(16.dp).fillMaxSize()) {
            TextFeldComponent(
                value = text,
                onValueChange = { text = it },
                label = "Nome",
                placeholder = "Digite seu nome",
                leadingIcon = Icons.Default.Person
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextFeldComponent(
                value = text,
                onValueChange = { text = it },
                label = "Senha",
                placeholder = "Digite sua senha",
                isPasswordField = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextFeldComponent(
                value = text,
                onValueChange = { text = it },
                label = "E-mail",
                placeholder = "Digite seu e-mail",
                leadingIcon = Icons.Default.Email,
                trailingIcon = Icons.Default.Clear,
                onTrailingIconClick = { text = "" },
                shape = RoundedCornerShape(20.dp)
            )
        }
    }
}
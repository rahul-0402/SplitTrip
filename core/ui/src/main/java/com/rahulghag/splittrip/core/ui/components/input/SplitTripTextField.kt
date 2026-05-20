package com.rahulghag.splittrip.core.ui.components.input

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rahulghag.splittrip.core.ui.theme.Dimens
import com.rahulghag.splittrip.core.ui.theme.ErrorRed
import com.rahulghag.splittrip.core.ui.theme.SplitTripTheme

@Composable
fun SplitTripTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    helperText: String? = null,
    errorText: String? = null,
    isError: Boolean = errorText != null,
    enabled: Boolean = true,
    singleLine: Boolean = true,
    maxLines: Int = 1,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyMedium,
                )
            },
            placeholder = if (placeholder.isNotEmpty()) {
                {
                    Text(
                        text = placeholder,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                            .copy(alpha = 0.6f),
                    )
                }
            } else null,
            isError = isError,
            enabled = enabled,
            singleLine = singleLine,
            maxLines = maxLines,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            visualTransformation = visualTransformation,
            textStyle = MaterialTheme.typography.bodyLarge,
            shape = MaterialTheme.shapes.small,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                errorBorderColor = ErrorRed,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                errorLabelColor = ErrorRed,
            ),
            modifier = Modifier.fillMaxWidth(),
        )

        // Helper or error text below field
        val supportText = errorText ?: helperText
        if (supportText != null) {
            Spacer(Modifier.height(Dimens.spaceXS))
            Text(
                text = supportText,
                style = MaterialTheme.typography.labelSmall,
                color = if (isError) ErrorRed
                        else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = Dimens.spaceS),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TextFieldPreview() {
    SplitTripTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement =
                androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp),
        ) {
            SplitTripTextField(
                value = "rahul@gmail.com",
                onValueChange = {},
                label = "Email",
            )
            SplitTripTextField(
                value = "",
                onValueChange = {},
                label = "Password",
                placeholder = "Enter your password",
                helperText = "Must be at least 8 characters",
            )
            SplitTripTextField(
                value = "invalid-email",
                onValueChange = {},
                label = "Email",
                errorText = "Please enter a valid email",
            )
        }
    }
}

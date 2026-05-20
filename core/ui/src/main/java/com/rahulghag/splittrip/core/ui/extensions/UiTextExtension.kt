package com.rahulghag.splittrip.core.ui.extensions

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.rahulghag.splittrip.core.common.model.UiText

/**
 * Resolves UiText to a String using Android Context.
 * Only available in Android/Compose layer — not in core:common.
 */
fun UiText.asString(context: Context): String = when (this) {
    is UiText.DynamicString  -> value
    is UiText.StringResource -> context.getString(id, *args.toTypedArray())
}

@Composable
fun UiText.asString(): String {
    val context = LocalContext.current
    return asString(context)
}

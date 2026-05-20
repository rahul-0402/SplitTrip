package com.rahulghag.splittrip.core.ui.extensions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.rahulghag.splittrip.core.common.mvi.UiEvent
import kotlinx.coroutines.flow.Flow

/**
 * Collects UiEvents safely — only when the screen is STARTED.
 * Prevents events from being processed when app is in background.
 *
 * Usage in any screen composable:
 *   CollectEvents(viewModel.uiEvent) { event ->
 *     when (event) {
 *       is MyEvent.NavigateToHome -> navController.navigate(...)
 *       is MyEvent.ShowSnackbar   -> snackbarHost.show(...)
 *     }
 *   }
 */
@Composable
fun <Event : UiEvent> CollectEvents(
    flow: Flow<Event>,
    lifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
    collector: suspend (Event) -> Unit,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(flow, lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(lifecycleState) {
            flow.collect { collector(it) }
        }
    }
}

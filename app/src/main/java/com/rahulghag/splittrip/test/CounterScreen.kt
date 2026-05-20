package com.rahulghag.splittrip.test

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rahulghag.splittrip.core.ui.extensions.CollectEvents
import com.rahulghag.splittrip.core.ui.theme.AmountTextStyle
import com.rahulghag.splittrip.core.ui.theme.Dimens
import com.rahulghag.splittrip.core.ui.theme.SplitTripTheme

@Composable
fun CounterScreen(
    viewModel: CounterViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    // Collect one-time events
    CollectEvents(viewModel.uiEvent) { event ->
        when (event) {
            CounterEvent.ShowResetMessage ->
                snackbarHostState.showSnackbar("Counter reset!")
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background,
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(Dimens.spaceL),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "MVI Pattern Test",
                style = MaterialTheme.typography.titleLarge,
            )

            Text(
                text = "${state.count}",
                style = AmountTextStyle.copy(
                    fontSize = MaterialTheme.typography.displayLarge.fontSize,
                ),
                color = if (state.count >= 0)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(vertical = 32.dp),
            )

            Row(horizontalArrangement = Arrangement.spacedBy(Dimens.spaceM)) {
                OutlinedButton(
                    onClick = { viewModel.onIntent(CounterIntent.Decrement) }
                ) { Text("−") }

                Button(
                    onClick = { viewModel.onIntent(CounterIntent.Increment) }
                ) { Text("+") }
            }

            OutlinedButton(
                onClick = { viewModel.onIntent(CounterIntent.Reset) },
                modifier = Modifier.padding(top = Dimens.spaceM),
            ) { Text("Reset") }

            Text(
                text = "Tap Reset to see a one-time Event",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = Dimens.space2XL),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CounterScreenPreview() {
    SplitTripTheme {
        // Preview uses a stub — hiltViewModel() doesn't work in Preview
    }
}

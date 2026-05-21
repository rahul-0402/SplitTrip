package com.rahulghag.splittrip.feature.auth.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rahulghag.splittrip.core.ui.extensions.CollectEvents
import com.rahulghag.splittrip.core.ui.theme.Brand400
import com.rahulghag.splittrip.core.ui.theme.Dimens
import com.rahulghag.splittrip.core.ui.theme.SplitTripTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToTripList: () -> Unit,
    viewModel: SplashViewModel = hiltViewModel(),
) {
    CollectEvents(viewModel.uiEvent) { event ->
        when (event) {
            SplashEvent.NavigateToLogin    -> onNavigateToLogin()
            SplashEvent.NavigateToTripList -> onNavigateToTripList()
        }
    }

    SplashContent(
        onAnimationsComplete = {
            viewModel.onIntent(SplashIntent.AnimationsComplete)
        }
    )
}

@Composable
private fun SplashContent(
    onAnimationsComplete: () -> Unit = {},
) {
    val iconScale = remember { Animatable(0.3f) }
    val iconAlpha = remember { Animatable(0f) }
    val textAlpha = remember { Animatable(0f) }
    val tagAlpha  = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        // Step 1 — icon fades + scales in simultaneously
        launch {
            iconAlpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = 500,
                    easing = FastOutSlowInEasing,
                )
            )
        }
        iconScale.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 600,
                easing = FastOutSlowInEasing,
            )
        )
        // Step 2 — app name fades in
        delay(100)
        textAlpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 400),
        )
        // Step 3 — tagline fades in
        delay(150)
        tagAlpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 400),
        )
        // Step 4 — hold briefly then navigate
        delay(800)
        onAnimationsComplete()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Box(
                modifier = Modifier
                    .scale(iconScale.value)
                    .alpha(iconAlpha.value)
                    .size(88.dp)
                    .background(
                        color = Brand400,
                        shape = MaterialTheme.shapes.extraLarge,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Outlined.AccountBalanceWallet,
                    contentDescription = "SplitTrip",
                    modifier = Modifier.size(44.dp),
                    tint = Color.White,
                )
            }

            Spacer(Modifier.height(Dimens.space2XL))

            Text(
                text = "SplitTrip",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.alpha(textAlpha.value),
            )

            Spacer(Modifier.height(Dimens.spaceS))

            Text(
                text = "Fair splits, happy trips.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.alpha(tagAlpha.value),
            )
        }

        Text(
            text = "v1.0.0",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = Dimens.space3XL)
                .alpha(tagAlpha.value),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SplashScreenPreview() {
    SplitTripTheme {
        SplashContent()
    }
}

package com.rahulghag.splittrip.core.ui.components.loading

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.rahulghag.splittrip.core.ui.theme.Dimens
import com.rahulghag.splittrip.core.ui.theme.SplitTripTheme

@Composable
fun shimmerBrush(): Brush {
    val shimmerColors = listOf(
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f),
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
    )
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000),
            repeatMode = RepeatMode.Restart,
        ),
        label = "shimmer_translate",
    )
    return Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translateAnim - 200f, 0f),
        end = Offset(translateAnim, 0f),
    )
}

@Composable
fun SkeletonBox(
    modifier: Modifier = Modifier,
    height: Dp = 16.dp,
    width: Dp? = null,
) {
    val widthModifier = if (width != null) modifier.width(width) else modifier
    Box(
        modifier = widthModifier
            .height(height)
            .clip(MaterialTheme.shapes.extraSmall)
            .background(shimmerBrush()),
    )
}

// Skeleton for a trip list row
@Composable
fun SkeletonTripRow(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.spaceL, vertical = Dimens.spaceM),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens.spaceM),
    ) {
        // Trip icon placeholder
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(MaterialTheme.shapes.small)
                .background(shimmerBrush()),
        )
        // Text lines
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(Dimens.spaceXS),
        ) {
            SkeletonBox(
                modifier = Modifier.fillMaxWidth(0.6f),
                height = 14.dp,
            )
            SkeletonBox(
                modifier = Modifier.fillMaxWidth(0.4f),
                height = 11.dp,
            )
        }
        // Amount placeholder
        Column(horizontalAlignment = Alignment.End) {
            SkeletonBox(width = 56.dp, height = 14.dp)
            Spacer(Modifier.height(4.dp))
            SkeletonBox(width = 36.dp, height = 10.dp)
        }
    }
}

// Skeleton for an expense row
@Composable
fun SkeletonExpenseRow(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.spaceL, vertical = Dimens.spaceM),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens.spaceM),
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(MaterialTheme.shapes.small)
                .background(shimmerBrush()),
        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(Dimens.spaceXS),
        ) {
            SkeletonBox(
                modifier = Modifier.fillMaxWidth(0.55f),
                height = 13.dp,
            )
            SkeletonBox(
                modifier = Modifier.fillMaxWidth(0.35f),
                height = 10.dp,
            )
        }
        Column(horizontalAlignment = Alignment.End) {
            SkeletonBox(width = 52.dp, height = 13.dp)
            Spacer(Modifier.height(4.dp))
            SkeletonBox(width = 40.dp, height = 10.dp)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SkeletonPreview() {
    SplitTripTheme {
        Column {
            repeat(3) { SkeletonTripRow() }
            repeat(3) { SkeletonExpenseRow() }
        }
    }
}

package com.rahulghag.splittrip.core.ui.components.avatar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rahulghag.splittrip.core.ui.theme.MemberColors
import com.rahulghag.splittrip.core.ui.theme.SplitTripTheme

// Avatar sizes
enum class AvatarSize(
    val dp: Dp,
    val fontSize: TextUnit,
) {
    XS(22.dp, 8.sp),
    SM(28.dp, 10.sp),
    MD(36.dp, 13.sp),
    LG(48.dp, 18.sp),
    XL(64.dp, 24.sp),
}

/**
 * Single member avatar showing initials.
 *
 * @param name  member name — initials extracted automatically
 * @param index member index in trip — determines color
 *              color = MemberColors[index % MemberColors.size]
 */
@Composable
fun MemberAvatar(
    name: String,
    index: Int,
    modifier: Modifier = Modifier,
    size: AvatarSize = AvatarSize.MD,
) {
    val color = MemberColors[index % MemberColors.size]
    val initials = name.toInitials()

    Box(
        modifier = modifier
            .size(size.dp)
            .clip(CircleShape)
            .background(color),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = initials,
            fontSize = size.fontSize,
            fontWeight = FontWeight.Bold,
            color = Color.White,
        )
    }
}

/**
 * Stack of overlapping avatars.
 * Shows up to [maxVisible] avatars then "+N" overflow.
 *
 * Usage: AvatarStack(members = trip.members)
 */
@Composable
fun AvatarStack(
    names: List<String>,
    modifier: Modifier = Modifier,
    maxVisible: Int = 4,
    size: AvatarSize = AvatarSize.SM,
) {
    val visible = names.take(maxVisible)
    val overflow = names.size - maxVisible

    Box(modifier = modifier) {
        visible.forEachIndexed { index, name ->
            Box(
                modifier = Modifier
                    .offset(x = (index * (size.dp.value * 0.7)).dp)
                    .size(size.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center,
            ) {
                MemberAvatar(
                    name = name,
                    index = index,
                    size = size,
                    modifier = Modifier
                        .size(size.dp - 2.dp)
                        .clip(CircleShape),
                )
            }
        }

        if (overflow > 0) {
            val overflowIndex = visible.size
            Box(
                modifier = Modifier
                    .offset(x = (overflowIndex * (size.dp.value * 0.7)).dp)
                    .size(size.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center,
            ) {
                Box(
                    modifier = Modifier
                        .size(size.dp - 2.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "+$overflow",
                        fontSize = size.fontSize,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

private fun String.toInitials(): String =
    trim()
        .split(" ")
        .filter { it.isNotEmpty() }
        .take(2)
        .joinToString("") { it.first().uppercase() }

@Preview(showBackground = true)
@Composable
private fun AvatarPreview() {
    SplitTripTheme {
        androidx.compose.foundation.layout.Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // All sizes
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AvatarSize.entries.forEachIndexed { i, size ->
                    MemberAvatar(name = "Rahul", index = i, size = size)
                }
            }
            // Stack
            AvatarStack(
                names = listOf("Rahul", "Komal", "Arun", "Sara", "Meera"),
                maxVisible = 4,
            )
        }
    }
}

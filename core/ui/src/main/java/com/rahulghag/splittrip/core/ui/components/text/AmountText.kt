package com.rahulghag.splittrip.core.ui.components.text

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rahulghag.splittrip.core.ui.theme.AmountTextStyle
import com.rahulghag.splittrip.core.ui.theme.SplitTripTheme
import com.rahulghag.splittrip.core.ui.theme.Success
import com.rahulghag.splittrip.core.ui.theme.ErrorRed

enum class AmountType { POSITIVE, NEGATIVE, NEUTRAL }

/**
 * Displays a currency amount always in JetBrains Mono.
 * Positive amounts show in green with "+" prefix.
 * Negative amounts show in red with "-" prefix.
 * Neutral amounts show in onSurface color.
 */
@Composable
fun AmountText(
    amount: Double,
    modifier: Modifier = Modifier,
    currencySymbol: String = "₹",
    style: TextStyle = AmountTextStyle,
    showSign: Boolean = true,
    type: AmountType = when {
        amount > 0 -> AmountType.POSITIVE
        amount < 0 -> AmountType.NEGATIVE
        else       -> AmountType.NEUTRAL
    },
) {
    val color = when (type) {
        AmountType.POSITIVE -> Success
        AmountType.NEGATIVE -> ErrorRed
        AmountType.NEUTRAL  -> MaterialTheme.colorScheme.onSurface
    }

    val prefix = when {
        showSign && amount > 0 -> "+"
        else -> ""
    }

    val formattedAmount = formatAmount(Math.abs(amount))
    val sign = if (amount < 0) "-" else ""

    Text(
        text = "$prefix$sign$currencySymbol$formattedAmount",
        style = style,
        color = color,
        modifier = modifier,
    )
}

private fun formatAmount(amount: Double): String {
    // Indian number formatting: 1,00,000
    val intPart = amount.toLong()
    val decPart = ((amount - intPart) * 100).toLong()

    val intStr = formatIndianNumber(intPart)

    return if (decPart == 0L) intStr
    else "$intStr.${decPart.toString().padStart(2, '0')}"
}

private fun formatIndianNumber(number: Long): String {
    if (number == 0L) return "0"
    val str = number.toString()
    if (str.length <= 3) return str

    // Last 3 digits, then groups of 2
    val result = StringBuilder()
    val lastThree = str.takeLast(3)
    val remaining = str.dropLast(3)

    remaining.reversed().chunked(2).forEachIndexed { i, chunk ->
        if (i > 0) result.append(",")
        result.append(chunk.reversed())
    }
    result.reverse()
    return if (result.isNotEmpty()) "$result,$lastThree" else lastThree
}

@Preview(showBackground = true)
@Composable
private fun AmountTextPreview() {
    SplitTripTheme {
        androidx.compose.foundation.layout.Column(
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement =
                androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp),
        ) {
            AmountText(amount = 18420.0,
                style = AmountTextStyle.copy(fontSize = 28.sp))
            AmountText(amount = -1200.0,
                style = AmountTextStyle.copy(fontSize = 28.sp))
            AmountText(amount = 0.0,
                style = AmountTextStyle.copy(fontSize = 28.sp))
            AmountText(amount = 2800.50,
                style = AmountTextStyle.copy(fontSize = 20.sp))
        }
    }
}

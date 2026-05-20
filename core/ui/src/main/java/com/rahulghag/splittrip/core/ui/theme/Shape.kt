package com.rahulghag.splittrip.core.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val SplitTripShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),   // tags, chips
    small      = RoundedCornerShape(8.dp),   // inputs, small buttons
    medium     = RoundedCornerShape(12.dp),  // cards, buttons
    large      = RoundedCornerShape(16.dp),  // bottom sheets
    extraLarge = RoundedCornerShape(24.dp),  // dialogs
)

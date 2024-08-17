package com.smh.movie.android.ui.components

import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Dp

fun Modifier.expandMaxWidth(
    horizontal: Dp
): Modifier {
    return layout { measurable, constraints ->
        val placeable = measurable.measure(
            constraints.copy(
                maxWidth = constraints.maxWidth + horizontal.roundToPx()
            )
        )

        layout(placeable.width, placeable.height) {
            placeable.place(0, 0)
        }
    }
}
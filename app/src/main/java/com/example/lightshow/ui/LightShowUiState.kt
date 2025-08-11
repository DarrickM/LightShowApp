package com.example.lightshow.ui

import androidx.compose.ui.graphics.Color
import com.example.lightshow.R

data class LightShowUiState(
    val isLightShowActive: Boolean = false,
    val numberOfCircleRows: Int = 5,
    val numberOfCircleColumns: Int = 3,
    val colorNames: Map<String, Int> = mapOf(
        "Red" to R.string.red,
        "Green" to R.string.green,
        "Blue" to R.string.blue,
        "Yellow" to R.string.yellow,
        "Pink" to R.string.pink,
        "White" to R.string.white,
        "Random" to R.string.random
    ),
    val colorValues: Map<String, Color?> = mapOf(
        "Red" to Color.Red,
        "Green" to Color.Green,
        "Blue" to Color.Blue,
        "Yellow" to Color.Yellow,
        "Pink" to Color.Magenta,
        "White" to Color.White,
        "Random" to null,
    ),
    val currentCircleColor: MutableMap<String, Color?> = mutableMapOf()
)

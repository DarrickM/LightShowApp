package com.example.lightshow.ui

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lightshow.R
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random
import kotlin.random.nextInt

class LightShowViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(createInitialState())
    val uiState: StateFlow<LightShowUiState> = _uiState.asStateFlow()

    private fun createInitialState(): LightShowUiState {
        val initialLightShowState = false
        val initialNumRows = 5
        val initialNumColumns = 3
        val initialColorNames: Map<String, Int> = mapOf(
            "Red" to R.string.red,
            "Green" to R.string.green,
            "Blue" to R.string.blue,
            "Yellow" to R.string.yellow,
            "Pink" to R.string.pink,
            "White" to R.string.white,
            "Random" to R.string.random
        )
        val initialColorValues: Map<String, Color?> = mapOf(
            "Red" to Color.Red,
            "Green" to Color.Green,
            "Blue" to Color.Blue,
            "Yellow" to Color.Yellow,
            "Pink" to Color.Magenta,
            "White" to Color.White,
            "Random" to null,
        )
        val initialDefaultColor = initialColorValues["Blue"]
        val initialCircleColors = mutableMapOf<String, Color?>()
        for (r in 0 until initialNumRows) {
            for (c in 0 until initialNumColumns) {
                initialCircleColors["Circle_r${r}_c${c}"] = initialDefaultColor
            }
        }
        return LightShowUiState(
            isLightShowActive = initialLightShowState,
            numberOfCircleColumns = initialNumColumns,
            numberOfCircleRows = initialNumRows,
            colorNames = initialColorNames,
            colorValues = initialColorValues,
            currentCircleColor = initialCircleColors
        )
    }


    fun toggleLightShow() {
        viewModelScope.launch {
            executeLightShow()
        }
    }

    private var colorsBeforeShow: MutableMap<String, Color?> = mutableMapOf()
    private var lightShowJob: Job? = null

    private suspend fun executeLightShow() {
        val currentlyActive = _uiState.value.isLightShowActive


        if (!currentlyActive) {
            Log.d("ViewModel", "Light Show is turning on")
            colorsBeforeShow = _uiState.value.currentCircleColor
            _uiState.update { currentState ->
                val clearedCircleColors: MutableMap<String, Color?> =
                    currentState.currentCircleColor
                        .mapValues { null }
                        .toMutableMap()
                currentState.copy(
                    currentCircleColor = clearedCircleColors,
                    isLightShowActive = true
                )
            }
            delay(500L)

            lightShowJob = viewModelScope.launch {
                Log.d("ViewModel", "Light show is active")
                while (_uiState.value.isLightShowActive) {
                    val randomKey = _uiState.value.currentCircleColor.keys.random()
                    _uiState.update { currentState ->
                        val colorUpdateForLightShow = currentState.currentCircleColor.toMutableMap()
                        val circleColorUpdate = colorsBeforeShow[randomKey]
                        colorUpdateForLightShow[randomKey] = circleColorUpdate
                        currentState.copy(
                            currentCircleColor = colorUpdateForLightShow
                        )
                    }
                    delay(100L)
                    _uiState.update { currentState ->
                        val colorUpdateForLightShow = currentState.currentCircleColor.toMutableMap()
                        colorUpdateForLightShow[randomKey] = null
                        currentState.copy(
                            currentCircleColor = colorUpdateForLightShow
                        )
                    }
                    delay(100L)
                }
            }


        } else {
            Log.d("ViewModel", "Light show turning off")
            lightShowJob?.cancelAndJoin()
            _uiState.update { currentState ->
                currentState.copy(
                    currentCircleColor = colorsBeforeShow,
                    isLightShowActive = false
                )
            }
            Log.d("ViewModel", "Circle colors restored, light show is off")
        }
    }

    fun colorSelect(circleId: String, selectedColor: String) {
        Log.d("ViewModel", "Color selected for $circleId: $selectedColor")
        val wantedColor: Color? = when (selectedColor) {
            "Red" -> _uiState.value.colorValues["Red"]
            "Green" -> _uiState.value.colorValues["Green"]
            "Blue" -> _uiState.value.colorValues["Blue"]
            "Yellow" -> _uiState.value.colorValues["Yellow"]
            "Pink" -> _uiState.value.colorValues["Pink"]
            "White" -> _uiState.value.colorValues["White"]
            "Random" -> randomColor()
            else -> {
                Log.w("ViewModel", "Unlisted Color selected: $selectedColor")
                Color.Black
            }
        }
        _uiState.update { currentState ->
            Log.d("ViewModel", "Updating uiState for color selection")
            val updatedCircleColors = currentState.currentCircleColor.toMutableMap()
            updatedCircleColors[circleId] = wantedColor
            currentState.copy(currentCircleColor = updatedCircleColors)
        }
    }

    fun colorSelectAll(selectedColor: String) {
        Log.d("ViewModel", "Color selected for for : $selectedColor")
        val wantedColor: Color? = when (selectedColor) {
            "Red" -> _uiState.value.colorValues["Red"]
            "Green" -> _uiState.value.colorValues["Green"]
            "Blue" -> _uiState.value.colorValues["Blue"]
            "Yellow" -> _uiState.value.colorValues["Yellow"]
            "Pink" -> _uiState.value.colorValues["Pink"]
            "White" -> _uiState.value.colorValues["White"]
            "Random" -> _uiState.value.colorValues["Random"]
            else -> {
                Log.w("ViewModel", "Unlisted Color selected: $selectedColor")
                Color.Black
            }
        }
        _uiState.update { currentState ->
            Log.d("ViewModel", "Updating uiState for color selection")
            val updatedCircleColors = currentState.currentCircleColor.toMutableMap()
            for ((key) in updatedCircleColors) {
                if (wantedColor == _uiState.value.colorValues["Random"])
                    updatedCircleColors[key] = randomColor()
                else updatedCircleColors[key] = wantedColor
            }
            currentState.copy(currentCircleColor = updatedCircleColors)
        }

    }

    fun randomColor(): Color {
        val rColor = Random.nextInt(1..255)
        val gColor = Random.nextInt(1..255)
        val bColor = Random.nextInt(1..255)
        return Color(red = rColor, green = gColor, blue = bColor)
    }
}
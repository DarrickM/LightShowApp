package com.example.lightshow.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lightshow.R

@Composable
fun LightShowScreen(
    lightShowViewModel: LightShowViewModel = viewModel()
) {
    val lightShowUiState by lightShowViewModel.uiState.collectAsState()
    val smallPadding = dimensionResource(R.dimen.small_padding)
    val mediumPadding = dimensionResource(R.dimen.medium_padding)
    val topPadding = dimensionResource(R.dimen.top_padding)

    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(top = topPadding)
            .background(Color.Black)
    ) {
        for (rowIndex in 0 until lightShowUiState.numberOfCircleRows) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center

            ) {
                for (colIndex in 0 until lightShowUiState.numberOfCircleColumns) {
                    val circleId = "Circle_r${rowIndex}_c${colIndex}"
                    Box(
                        modifier = Modifier
                            .padding(smallPadding)
                            .background(Color.Transparent)
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(
                                    lightShowUiState.currentCircleColor[circleId] ?: Color.Black
                                )
                                .size(110.dp)
                        )
                        var expanded by remember { mutableStateOf(false) }
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .wrapContentSize()
                                .offset(x = 20.dp, y = 12.dp)
                        ) {
                            IconButton(onClick = { expanded = !expanded }) {
                                Icon(
                                    Icons.Default.MoreVert,
                                    contentDescription = "Color Options"
                                )
                            }
                            DropdownMenu(
                                offset = DpOffset(x = 20.dp, y = 12.dp),
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                lightShowUiState.colorNames.forEach { (colorNameString, colorResourceId) ->
                                    DropdownMenuItem(
                                        text = { Text(stringResource(colorResourceId)) },
                                        onClick = {
                                            expanded = !expanded
                                            lightShowViewModel.colorSelect(
                                            circleId = circleId, selectedColor = colorNameString
                                        )
                                        }
                                    )
                                }
                            }
                        }

                    }
                }
            }
        }
        Spacer(modifier = Modifier.size(30.dp))
        Row(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        ) {
            Button(
                onClick = {
                    lightShowViewModel.toggleLightShow()},
                shape = (RoundedCornerShape(50)),
                modifier = Modifier
                    .padding(mediumPadding)
                    .wrapContentSize(Alignment.Center)
            ) {
                Text(
                    text = stringResource(R.string.start_stop_light_show),
                    fontSize = 20.sp
                )
                Box {
                    var expanded by remember { mutableStateOf(false)}
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(
                            Icons.Default.MoreVert,
                            contentDescription = "Color Options",
                        )
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        lightShowUiState.colorNames.forEach { (colorNameString, colorResourceId) ->
                            DropdownMenuItem(
                                text = { Text(stringResource(colorResourceId)) },
                                onClick = {
                                    expanded = !expanded
                                    lightShowViewModel.colorSelectAll(colorNameString)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
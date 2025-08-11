package com.example.lightshow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.lightshow.ui.LightShowScreen
import com.example.lightshow.ui.theme.LightShowTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LightShowTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    LightShowScreen()
                }
            }
        }
    }
}
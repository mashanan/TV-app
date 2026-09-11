package com.example.tv_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.tv_app.ui.navigation.TvAppNavHost
import com.example.tv_app.ui.theme.TVappTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TVappTheme {
                TvAppNavHost()
            }
        }
    }
}

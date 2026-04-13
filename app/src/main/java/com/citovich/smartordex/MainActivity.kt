package com.citovich.smartordex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.citovich.smartordex.ui.navigation.AppNavGraph
import com.citovich.smartordex.ui.theme.SmartOrdexTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SmartOrdexTheme {
                AppNavGraph()
            }
        }
    }
}
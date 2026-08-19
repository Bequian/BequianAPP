package com.example.bequianapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.bequianapp.navigation.NavigationWrapper
import com.example.bequianapp.ui.theme.BequianAPPTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BequianAPPTheme {
                NavigationWrapper()
            }
        }
    }
}

package com.example.morseovka

import android.hardware.camera2.CameraManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.morseovka.ui.theme.MorseovkaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Získání CameraManager
        val cameraManager = getSystemService(CAMERA_SERVICE) as CameraManager

        // Vytvoření MorseViewModel s CameraManagerem
        val viewModelFactory = MorseViewModelFactory(cameraManager)
        val morseViewModel = ViewModelProvider(this, viewModelFactory)[MorseViewModel::class.java]

        setContent {
            MorseScreen(viewModel = morseViewModel)
        }
    }
}

class MorseViewModelFactory(
    private val cameraManager: CameraManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MorseViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MorseViewModel(cameraManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
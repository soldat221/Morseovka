package com.example.morseovka

import android.hardware.camera2.CameraManager
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MorseViewModel(
    private val cameraManager: CameraManager
) : ViewModel() {

    var inputText = mutableStateOf("")
    var morseCodeText = mutableStateOf("")
    var isBlinking = mutableStateOf(false)
    var repeatBlinking = mutableStateOf(false)
    val isDarkMode = mutableStateOf(false)

    private val cameraId: String = cameraManager.cameraIdList[0] // Použijeme hlavní kameru

    // Funkce pro převod textu do Morseova kódu
    private fun convertTextToMorse(text: String): String {
        val morseMap = mapOf(
            'A' to ".-", 'B' to "-...", 'C' to "-.-.", 'D' to "-..", 'E' to ".",
            'F' to "..-.", 'G' to "--.", 'H' to "....", 'I' to "..", 'J' to ".---",
            'K' to "-.-", 'L' to ".-..", 'M' to "--", 'N' to "-.", 'O' to "---",
            'P' to ".--.", 'Q' to "--.-", 'R' to ".-.", 'S' to "...", 'T' to "-",
            'U' to "..-", 'V' to "...-", 'W' to ".--", 'X' to "-..-", 'Y' to "-.--",
            'Z' to "--..", '1' to ".----", '2' to "..---", '3' to "...--", '4' to "....-",
            '5' to ".....", '6' to "-....", '7' to "--...", '8' to "---..", '9' to "----.",
            '0' to "-----", ' ' to "/"
        )

        return text.uppercase().map { morseMap[it] ?: "" }.joinToString(" ")
    }

    // Aktualizace vstupního textu a morseCodeText
    fun onTextChanged(newText: String) {
        inputText.value = newText
        morseCodeText.value = convertTextToMorse(newText)
    }

    // Funkce pro spuštění blikání Morseovy abecedy
    fun startBlinking() {
        if (isBlinking.value) return
        isBlinking.value = true
        viewModelScope.launch {
            do {
                for (char in morseCodeText.value) {
                    // Pokud blikání bylo zastaveno, ukonči smyčku
                    if (!isBlinking.value) return@launch

                    when (char) {
                        '.' -> blinkLight(200)
                        '-' -> blinkLight(600)
                        ' ' -> delay(600)
                        '/' -> delay(1400)
                    }
                    delay(200) // Pauza mezi znaky

                    // Kontrola stavu isBlinking po každém symbolu
                    if (!isBlinking.value) return@launch
                }
            } while (repeatBlinking.value && isBlinking.value)
            isBlinking.value = false
        }
    }

    // Funkce pro zapnutí a vypnutí blesku
    private suspend fun blinkLight(duration: Long) {
        try {
            cameraManager.setTorchMode(cameraId, true)  // Zapni blesk
            delay(duration)
            cameraManager.setTorchMode(cameraId, false) // Vypni blesk
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Funkce pro zastavení blikání
    fun stopBlinking() {
        isBlinking.value = false
    }
}
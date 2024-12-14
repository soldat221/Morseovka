package com.example.morseovka

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
//import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun MorseScreen(viewModel: MorseViewModel = viewModel()) {
    val inputText by viewModel.inputText
    val morseCodeText by viewModel.morseCodeText
    val isBlinking by viewModel.isBlinking
    val repeatBlinking by viewModel.repeatBlinking

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        BasicTextField(
            value = inputText,
            onValueChange = { newText ->
                // Filtrovat pouze platné znaky (A-Z, 0-9 a mezery)
                val filteredText = newText.uppercase().filter { it in "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789 " }
                viewModel.onTextChanged(filteredText)
            },
            modifier = Modifier.fillMaxWidth(),
            decorationBox = { innerTextField ->
                Box(modifier = Modifier.padding(16.dp)) {
                    if (inputText.isEmpty()) {
                        Text("Zadejte text")
                    }
                    innerTextField()
                }
            }
        )

        Text(text = "Morseovka: $morseCodeText")

        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
            Checkbox(
                checked = repeatBlinking,
                onCheckedChange = { viewModel.repeatBlinking.value = it }
            )
            Text("Opakovat")
        }

        Button(
            onClick = { viewModel.startBlinking() },
            enabled = !isBlinking
        ) {
            Text("Blikat")
        }

        Button(
            onClick = { viewModel.stopBlinking() },
            enabled = isBlinking
        ) {
            Text("Zastavit blikání")
        }
    }
}
package com.example.morseovka

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextAlign

@Composable
fun MorseScreen(viewModel: MorseViewModel) {
    val inputText by viewModel.inputText
    val morseCode by viewModel.morseCodeText
    val isBlinking by viewModel.isBlinking
    val repeatBlinking by viewModel.repeatBlinking
    val isDarkMode by viewModel.isDarkMode
    val messageHistory = viewModel.messageHistory

    val backgroundColor = if (isDarkMode) Color.Black else Color.White
    val textColor = if (isDarkMode) Color.White else Color.Black
    val placeholderColor = if (isDarkMode) Color.Gray else Color.DarkGray

    var showHistory by remember { mutableStateOf(false) }

    MaterialTheme(
        colorScheme = if (viewModel.isDarkMode.value) darkColorScheme() else lightColorScheme()
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor),
            color = backgroundColor
        ) {
            if (showHistory) {
                // Zobrazení historie
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Text(
                        "Historie zpráv",
                        color = textColor,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        state = rememberLazyListState(),
                        reverseLayout = false,
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        items(viewModel.displayedMessageHistory) { message ->
                            Text(
                                text = message.text,
                                color = textColor,
                                modifier = Modifier
                                    .padding(vertical = 4.dp)
                                    .fillMaxWidth()
                                    .background(
                                        if (isDarkMode) Color.DarkGray else Color.LightGray
                                    )
                                    .padding(8.dp)
                                    .clickable {
                                        viewModel.onTextChanged(message.text)
                                    }
                            )
                        }

                        item {
                            LaunchedEffect(viewModel.displayedMessageHistory.size) {
                                if (viewModel.displayedMessageHistory.isNotEmpty()) {
                                    viewModel.loadMoreMessages()
                                }
                            }
                        }
                    }

                    Button(
                        onClick = { showHistory = false },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Zpět")
                    }
                }
            } else {
                // Hlavní obrazovka
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(onClick = { showHistory = true }) {
                            Text("Historie")
                        }

                        Row {
                            Text("Dark Mode", color = textColor, modifier = Modifier.padding(end = 8.dp))
                            Switch(
                                checked = isDarkMode,
                                onCheckedChange = { viewModel.isDarkMode.value = it }
                            )
                        }
                    }

                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text("Zadejte text:", color = textColor)
                        Spacer(modifier = Modifier.height(8.dp))
                        BasicTextField(
                            value = inputText,
                            onValueChange = { newText ->
                                val filteredText = newText.uppercase().filter { it in "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789 " }
                                viewModel.onTextChanged(filteredText)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(placeholderColor.copy(alpha = 0.1f))
                                .padding(8.dp),
                            textStyle = LocalTextStyle.current.copy(color = textColor),
                            decorationBox = { innerTextField ->
                                Box {
                                    if (inputText.isEmpty()) {
                                        Text("Zadejte text", color = placeholderColor)
                                    }
                                    innerTextField()
                                }
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        "Morseovka: $morseCode",
                        color = textColor,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(onClick = {
                            viewModel.startBlinking()
                            viewModel.addMessage(inputText) // Přidání zprávy do historie
                        }) {
                            Text("Spustit blikání")
                        }
                        Button(onClick = { viewModel.stopBlinking() }) {
                            Text("Zastavit blikání")
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = repeatBlinking,
                            onCheckedChange = { viewModel.repeatBlinking.value = it }
                        )
                        Text("Opakovat zprávu", color = textColor)
                    }
                }
            }
        }
    }
}
package com.example.morseovka

import android.content.Context
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import java.io.File
import com.google.gson.Gson

data class Message(val text: String)

object MessageHistory {
    private const val HISTORY_FILE_NAME = "history.json"

    fun loadHistory(context: Context): List<Message> {
        val file = File(context.filesDir, HISTORY_FILE_NAME)
        if (!file.exists()) return emptyList()
        val json = file.readText()
        return Gson().fromJson(json, object : TypeToken<List<Message>>() {}.type)
    }

    fun saveHistory(context: Context, messages: List<Message>) {
        val file = File(context.filesDir, HISTORY_FILE_NAME)
        val json = Gson().toJson(messages)
        file.writeText(json)
    }
}
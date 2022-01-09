package ru.dmitriyt.dcs.clientdesktop.data.repository

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.dmitriyt.dcs.clientdesktop.data.model.Settings
import java.io.File

class SettingsRepository {
    companion object {
        private const val FILE_NAME = "settings.json"
    }

    fun getSettings(): Settings {
        return getSettingsFile()
            .let {
                try {
                    Json.decodeFromString<Settings>(it.readText())
                } catch (e: Exception) {
                    null
                }
            }
            ?: Settings.DEFAULT
    }

    fun saveSettings(settings: Settings) {
        getSettingsFile().writeText(Json.encodeToString(settings))
    }

    private fun getSettingsFile(): File {
        return File(FILE_NAME).apply {
            if (!exists()) {
                createNewFile()
            }
        }
    }
}
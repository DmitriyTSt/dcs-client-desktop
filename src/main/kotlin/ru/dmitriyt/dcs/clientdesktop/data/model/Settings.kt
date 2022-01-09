package ru.dmitriyt.dcs.clientdesktop.data.model

import kotlinx.serialization.Serializable
import ru.dmitriyt.dcs.core.data.DefaultConfig

@Serializable
data class Settings(
    val server: String,
    val port: String,
    val isMulti: Boolean,
) {
    companion object {
        val DEFAULT = Settings(DefaultConfig.DEFAULT_SERVER, DefaultConfig.DEFAULT_PORT.toString(), false)
    }
}
package ru.dmitriyt.dcs.clientdesktop.presentation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.dmitriyt.dcs.clientdesktop.data.model.Settings
import ru.dmitriyt.dcs.clientdesktop.data.repository.SettingsRepository
import ru.dmitriyt.dcs.clientdesktop.domain.DCSClientWrapper

class MainViewModel(
    private val settingsRepository: SettingsRepository,
    private val dcsClientWrapper: DCSClientWrapper,
) {
    private val _settings = MutableStateFlow(Settings.DEFAULT)
    val settings = _settings.asStateFlow()

    val computingState = dcsClientWrapper.state.asStateFlow()
    val computingError = dcsClientWrapper.error.asStateFlow()

    fun initSettings() {
        val settings = settingsRepository.getSettings()
        _settings.value = settings
    }

    fun saveServer(server: String) {
        val newSettings = _settings.value.copy(server = server)
        applySetting(newSettings)
    }

    fun savePort(port: String) {
        val newSettings = _settings.value.copy(port = port)
        applySetting(newSettings)
    }

    fun saveIsMulti(isMulti: Boolean) {
        val newSettings = _settings.value.copy(isMulti = isMulti)
        applySetting(newSettings)
    }

    private fun applySetting(newSettings: Settings) {
        _settings.value = newSettings
        settingsRepository.saveSettings(newSettings)
    }

    fun start(settings: Settings) {
        dcsClientWrapper.run(settings)
    }

    fun stop() {
        dcsClientWrapper.stop()
    }
}
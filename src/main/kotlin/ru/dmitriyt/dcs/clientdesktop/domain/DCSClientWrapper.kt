package ru.dmitriyt.dcs.clientdesktop.domain

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ru.dmitriyt.dcs.client.ArgsManager
import ru.dmitriyt.dcs.client.presentation.ClientApp
import ru.dmitriyt.dcs.clientdesktop.data.model.ComputingState
import ru.dmitriyt.dcs.clientdesktop.data.model.Settings

class DCSClientWrapper {
    val error = MutableStateFlow("")
    val state = MutableStateFlow(ComputingState.IDLE)
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        state.value = ComputingState.IDLE
        error.value = throwable.message ?: "Неизвестная ошибка"
        System.err.println(throwable.cause)
        System.err.println(throwable.message)
    }
    private val coroutineScope = CoroutineScope(Dispatchers.Default + exceptionHandler)
    private var clientApp: ClientApp? = null

    fun run(settings: Settings) {
        clientApp ?: run {
            val app = ClientApp(
                ArgsManager(
                    listOfNotNull(
                        "--server",
                        settings.server,
                        "--port",
                        settings.port,
                        if (settings.isMulti) "-m" else null
                    ).toTypedArray()
                )
            )
            clientApp = app
        }
        coroutineScope.launch {
            clientApp?.start()
            state.value = ComputingState.COMPUTING
        }
    }

    fun stop() {
        coroutineScope.launch {
            clientApp?.softStop()
            clientApp = null
            state.value = ComputingState.IDLE
        }
    }
}
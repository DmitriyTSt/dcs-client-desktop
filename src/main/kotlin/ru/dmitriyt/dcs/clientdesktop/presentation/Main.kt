package ru.dmitriyt.dcs.clientdesktop.presentation

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Checkbox
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import ru.dmitriyt.dcs.clientdesktop.data.model.ComputingState
import ru.dmitriyt.dcs.clientdesktop.data.repository.SettingsRepository
import ru.dmitriyt.dcs.clientdesktop.domain.DCSClientWrapper

@Composable
@Preview
fun App(mainViewModel: MainViewModel) {
    LaunchedEffect(true) {
        mainViewModel.initSettings()
    }

    val state = mainViewModel.computingState.collectAsState()
    val error = mainViewModel.computingError.collectAsState()
    val settings = mainViewModel.settings.collectAsState()

    MaterialTheme {
        Box(modifier = Modifier.fillMaxWidth().fillMaxHeight()) {
            Column {
                Row(modifier = Modifier.padding(16.dp)) {
                    TextField(
                        value = settings.value.server,
                        onValueChange = { mainViewModel.saveServer(it) },
                        modifier = Modifier.weight(1f),
                        label = {
                            Text("Адрес сервера")
                        },
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    TextField(
                        value = settings.value.port,
                        onValueChange = { mainViewModel.savePort(it) },
                        modifier = Modifier.weight(1f),
                        label = {
                            Text("Порт")
                        },
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Row(modifier = Modifier.weight(1f)) {
                        Checkbox(
                            checked = settings.value.isMulti,
                            onCheckedChange = { mainViewModel.saveIsMulti(it) },
                        )
                        Text(
                            modifier = Modifier.weight(1f).align(Alignment.CenterVertically),
                            text = "Многопоточность",
                        )
                    }
                }
                Button(
                    onClick = {
                        if (state.value == ComputingState.COMPUTING) {
                            mainViewModel.stop()
                        } else {
                            mainViewModel.start(settings.value)
                        }
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = when (state.value) {
                            ComputingState.COMPUTING -> Color.Red
                            ComputingState.IDLE -> Color.Green
                        }
                    )
                ) {
                    Text(state.value.buttonText, fontSize = 20.sp)
                }
                if (error.value.isNotEmpty()) {
                    Text(
                        modifier = Modifier.align(Alignment.CenterHorizontally).padding(16.dp),
                        text = error.value,
                        color = Color.Red
                    )
                }

            }
        }
    }
}

fun main() = application {
    val mainViewModel = MainViewModel(SettingsRepository(), DCSClientWrapper())

    fun closeApp() {
        mainViewModel.stop()
        exitApplication()
    }

    Window(title = "DCS Client Desktop", onCloseRequest = ::closeApp) {
        App(mainViewModel)
    }
}

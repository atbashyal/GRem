package com.lazyracoon.grem.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogWindow
import androidx.compose.ui.window.rememberDialogState
import com.lazyracoon.grem.viewmodel.MainViewModel
import kotlin.time.Duration.Companion.seconds

@Composable
fun SettingsWindow(
    viewModel: MainViewModel,
    onQuit: (() -> Unit)? = null
) {
    if (viewModel.settingsVisible) {
        DialogWindow(
            onCloseRequest = { viewModel.settingsVisible = false },
            title = "GRem Settings",
            state = rememberDialogState(width = 440.dp, height = 360.dp),
            resizable = false
        ) {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val totalSecs = viewModel.breakInterval.inWholeSeconds
                    var hStr by remember { mutableStateOf((totalSecs / 3600).toString()) }
                    var mStr by remember { mutableStateOf(((totalSecs % 3600) / 60).toString()) }
                    var sStr by remember { mutableStateOf((totalSecs % 60).toString()) }

                    Column(
                        modifier = Modifier.fillMaxSize().padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Break Schedule",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Text(
                            text = "Set how often the reminder should appear:",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            OutlinedTextField(
                                value = hStr,
                                onValueChange = { hStr = it.filter { char -> char.isDigit() } },
                                label = { Text("Hours") },
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                            OutlinedTextField(
                                value = mStr,
                                onValueChange = { mStr = it.filter { char -> char.isDigit() } },
                                label = { Text("Mins") },
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                            OutlinedTextField(
                                value = sStr,
                                onValueChange = { sStr = it.filter { char -> char.isDigit() } },
                                label = { Text("Secs") },
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (viewModel.running) "Status: Running" else "Status: Paused",
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (viewModel.running) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                            )

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedButton(
                                    onClick = { viewModel.toggleRunning() }
                                ) {
                                    Text(if (viewModel.running) "Pause" else "Resume")
                                }

                                OutlinedButton(
                                    onClick = { viewModel.showBreakNow() }
                                ) {
                                    Text("Test Break")
                                }
                            }
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (onQuit != null) {
                                TextButton(
                                    onClick = {
                                        viewModel.settingsVisible = false
                                        onQuit()
                                    },
                                    colors = ButtonDefaults.textButtonColors(
                                        contentColor = MaterialTheme.colorScheme.error
                                    )
                                ) {
                                    Text("Quit GRem")
                                }
                            } else {
                                Spacer(modifier = Modifier.width(1.dp))
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                TextButton(
                                    onClick = { viewModel.settingsVisible = false }
                                ) {
                                    Text("Close")
                                }

                                Button(
                                    onClick = {
                                        val h = hStr.toLongOrNull() ?: 0L
                                        val m = mStr.toLongOrNull() ?: 0L
                                        val s = sStr.toLongOrNull() ?: 0L
                                        val totalSeconds = (h * 3600) + (m * 60) + s

                                        if (totalSeconds > 0) {
                                            viewModel.updateInterval(totalSeconds.seconds)
                                            viewModel.settingsVisible = false
                                        }
                                    },
                                    shape = MaterialTheme.shapes.medium
                                ) {
                                    Text("Update Preference")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

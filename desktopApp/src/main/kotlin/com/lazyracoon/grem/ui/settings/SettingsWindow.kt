package com.lazyracoon.grem.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogWindow
import androidx.compose.ui.window.rememberDialogState
import com.lazyracoon.grem.viewmodel.MainViewModel
import kotlin.time.Duration.Companion.seconds

@Composable
fun SettingsWindow(viewModel: MainViewModel) {
    if (viewModel.settingsVisible) {
        DialogWindow(
            onCloseRequest = { viewModel.settingsVisible = false },
            title = "GRem Settings",
            state = rememberDialogState(width = 400.dp, height = 300.dp),
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

                        Spacer(modifier = Modifier.weight(1f))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(
                                onClick = { viewModel.settingsVisible = false }
                            ) {
                                Text("Cancel")
                            }

                            Spacer(modifier = Modifier.width(8.dp))

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

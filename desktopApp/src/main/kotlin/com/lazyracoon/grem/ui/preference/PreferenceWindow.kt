package com.lazyracoon.grem.ui.preference

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogWindow
import androidx.compose.ui.window.rememberDialogState
import com.lazyracoon.grem.viewmodel.MainViewModel

@Composable
fun PreferenceWindow(viewModel: MainViewModel) {
    if (viewModel.preferenceVisible) {
        DialogWindow(
            onCloseRequest = { viewModel.preferenceVisible = false },
            title = "GRem Preferences",
            state = rememberDialogState(width = 420.dp, height = 240.dp),
            resizable = false
        ) {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Preferences",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.setAutoStart(!viewModel.autoStartEnabled)
                                },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Checkbox(
                                checked = viewModel.autoStartEnabled,
                                onCheckedChange = { checked ->
                                    viewModel.setAutoStart(checked)
                                }
                            )

                            Column {
                                Text(
                                    text = "Start app when windows starts",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Text(
                                    text = "Automatically launch GRem on startup",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Button(
                                onClick = { viewModel.preferenceVisible = false }
                            ) {
                                Text("Close")
                            }
                        }
                    }
                }
            }
        }
    }
}

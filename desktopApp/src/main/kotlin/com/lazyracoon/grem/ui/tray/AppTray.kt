package com.lazyracoon.grem.ui.tray

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Tray
import com.lazyracoon.grem.util.WindowWorkspaceUtils
import com.lazyracoon.grem.viewmodel.MainViewModel
import grem.shared.generated.resources.Res
import grem.shared.generated.resources.img
import org.jetbrains.compose.resources.painterResource
import java.awt.event.ActionListener

@Composable
fun ApplicationScope.AppTray(viewModel: MainViewModel) {
    if (WindowWorkspaceUtils.isLinux()) {
        DisposableEffect(Unit) {
            try {
                dorkbox.systemTray.SystemTray.DEBUG = false
                val tray: dorkbox.systemTray.SystemTray? = dorkbox.systemTray.SystemTray.get()
                if (tray != null) {
                    val stream = MainViewModel::class.java.getResourceAsStream("/drawable/img.png")
                        ?: Thread.currentThread().contextClassLoader.getResourceAsStream("drawable/img.png")
                    if (stream != null) {
                        tray.setImage(stream)
                    }
                    tray.setStatus("GRem")
                    tray.setTooltip("GRem")

                    val menu: dorkbox.systemTray.Menu = tray.getMenu()
                    
                    val settingsItem: dorkbox.systemTray.Entry = dorkbox.systemTray.MenuItem("Settings", ActionListener {
                        viewModel.settingsVisible = true
                    })
                    val preferenceItem: dorkbox.systemTray.Entry = dorkbox.systemTray.MenuItem("Preference", ActionListener {
                        viewModel.preferenceVisible = true
                    })
                    val pauseItem: dorkbox.systemTray.Entry = dorkbox.systemTray.MenuItem("Pause / Resume", ActionListener {
                        viewModel.toggleRunning()
                    })
                    val showBreakItem: dorkbox.systemTray.Entry = dorkbox.systemTray.MenuItem("Show break now", ActionListener {
                        viewModel.showBreakNow()
                    })
                    val quitItem: dorkbox.systemTray.Entry = dorkbox.systemTray.MenuItem("Quit", ActionListener {
                        tray.shutdown()
                        exitApplication()
                    })

                    menu.add(settingsItem)
                    menu.add(preferenceItem)
                    menu.add(pauseItem)
                    menu.add(showBreakItem)
                    menu.add(quitItem)
                }
            } catch (t: Throwable) {
                t.printStackTrace()
            }

            onDispose {
                try {
                    dorkbox.systemTray.SystemTray.get()?.shutdown()
                } catch (_: Throwable) {}
            }
        }
    } else if (java.awt.SystemTray.isSupported()) {
        Tray(
            icon = painterResource(Res.drawable.img),
            tooltip = "GRem",
            onAction = {
                viewModel.settingsVisible = true
            },
        ) {
            Item(
                text = "Settings",
                onClick = {
                    viewModel.settingsVisible = true
                },
            )

            Item(
                text = "Preference",
                onClick = {
                    viewModel.preferenceVisible = true
                },
            )

            Item(
                text = if (viewModel.running) "Pause" else "Resume",
                onClick = {
                    viewModel.toggleRunning()
                }
            )

            Item(
                text = "Show break now",
                onClick = {
                    viewModel.showBreakNow()
                }
            )

            Separator()

            Item(
                text = "Quit",
                onClick = {
                    exitApplication()
                }
            )
        }
    }
}

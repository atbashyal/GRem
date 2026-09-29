package com.lazyracoon.grem

import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.window.application
import com.lazyracoon.grem.ui.overlay.BreakOverlayWindow
import com.lazyracoon.grem.ui.preference.PreferenceWindow
import com.lazyracoon.grem.ui.settings.SettingsWindow
import com.lazyracoon.grem.ui.tray.AppTray
import com.lazyracoon.grem.viewmodel.MainViewModel

fun main() = application {
    val scope = rememberCoroutineScope()
    val viewModel = remember { MainViewModel(scope) }

    // Tray icon / Top bar status indicator
    AppTray(viewModel)

    // Overlay Window (The pop-up)
    BreakOverlayWindow(viewModel)

    // Settings Window
    SettingsWindow(
        viewModel = viewModel,
        onQuit = { exitApplication() }
    )

    // Preference Window
    PreferenceWindow(viewModel)
}

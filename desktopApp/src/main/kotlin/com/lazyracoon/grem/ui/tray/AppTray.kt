package com.lazyracoon.grem.ui.tray

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Tray
import com.lazyracoon.grem.viewmodel.MainViewModel
import grem.shared.generated.resources.Res
import grem.shared.generated.resources.img
import org.jetbrains.compose.resources.painterResource

@Composable
fun ApplicationScope.AppTray(viewModel: MainViewModel) {
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

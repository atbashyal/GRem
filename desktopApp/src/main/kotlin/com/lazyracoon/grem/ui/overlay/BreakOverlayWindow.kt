package com.lazyracoon.grem.ui.overlay

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogWindow
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.rememberDialogState
import com.lazyracoon.grem.viewmodel.MainViewModel
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun BreakOverlayWindow(viewModel: MainViewModel) {
    val dialogState = rememberDialogState(
        width = 200.dp,
        height = 100.dp,
        position = WindowPosition.Aligned(Alignment.BottomEnd),
    )

    var isWindowVisible by remember { mutableStateOf(false) }
    var isContentVisible by remember { mutableStateOf(false) }
    var totalDurationMs by remember { mutableStateOf(0L) }

    LaunchedEffect(viewModel.breakVisible) {
        if (viewModel.breakVisible) {
            totalDurationMs = 0L
            isWindowVisible = true
            isContentVisible = true
        } else {
            isContentVisible = false
            delay(600.milliseconds) // Wait for exit slideOut animation to complete
            isWindowVisible = false
            totalDurationMs = 0L
        }
    }

    LaunchedEffect(viewModel.breakVisible, totalDurationMs) {
        if (viewModel.breakVisible) {
            val timeout = if (totalDurationMs > 0) totalDurationMs else 10_000L
            delay(timeout.milliseconds)
            viewModel.dismissBreak()
        }
    }

    if (isWindowVisible) {
        DialogWindow(
            onCloseRequest = { viewModel.dismissBreak() },
            title = "GRem",
            state = dialogState,
            undecorated = true,
            transparent = true,
            resizable = false,
            alwaysOnTop = true,
            focusable = false,
            visible = isWindowVisible,
        ) {
            AnimatedVisibility(
                visible = isContentVisible,
                enter = slideInVertically(
                    animationSpec = tween(600),
                    initialOffsetY = { fullHeight -> fullHeight },
                ),
                exit = slideOutVertically(
                    animationSpec = tween(600),
                    targetOffsetY = { fullHeight -> fullHeight },
                ),
                modifier = Modifier.fillMaxSize()
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    AnimatedWebp(
                        resourcePath = "drawable/output.webp",
                        modifier = Modifier.fillMaxSize(),
                        onDurationLoaded = { duration ->
                            if (duration > 0) {
                                totalDurationMs = duration
                            }
                        }
                    )
                }
            }
        }
    }
}

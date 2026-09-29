package com.lazyracoon.grem.viewmodel

import androidx.compose.runtime.*
import com.lazyracoon.grem.scheduler.BreakScheduler
import com.lazyracoon.grem.util.AutoStartUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

class MainViewModel(private val scope: CoroutineScope) {
    
    var breakVisible by mutableStateOf(value = false)
        private set

    var running by mutableStateOf(value = true)
        private set
    
    var settingsVisible by mutableStateOf(false)

    var preferenceVisible by mutableStateOf(false)

    var autoStartEnabled by mutableStateOf(AutoStartUtils.isAutoStartEnabled())
        private set
    
    var breakInterval by mutableStateOf(15.minutes)
        private set

    private var schedulerJob: Job? = null

    init {
        startScheduler()
    }

    private fun startScheduler() {
        schedulerJob?.cancel()
        schedulerJob = scope.launch {
            val scheduler = BreakScheduler(
                breakInterval = breakInterval,
                onBreak = {
                    if (running) {
                        breakVisible = true
                    }
                },
            )
            scheduler.start()
        }
    }

    fun toggleRunning() {
        running = !running
    }

    fun showBreakNow() {
        breakVisible = true
    }

    fun dismissBreak() {
        breakVisible = false
    }

    fun updateInterval(newInterval: Duration) {
        breakInterval = newInterval
        startScheduler()
    }

    fun setAutoStart(enabled: Boolean) {
        val success = AutoStartUtils.setAutoStartEnabled(enabled)
        if (success) {
            autoStartEnabled = enabled
        } else {
            autoStartEnabled = AutoStartUtils.isAutoStartEnabled()
        }
    }
}

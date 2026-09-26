package com.lazyracoon.grem.scheduler

import kotlinx.coroutines.delay
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class BreakScheduler(
    private val breakInterval: Duration = 30.seconds,
    private val onBreak: () -> Unit
) {
    suspend fun start() {
        while (true) {
            delay(breakInterval)
            onBreak()
        }
    }
}

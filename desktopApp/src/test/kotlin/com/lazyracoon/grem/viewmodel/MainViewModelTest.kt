package com.lazyracoon.grem.viewmodel

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.minutes

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    @Test
    fun testToggleRunning() {
        val testScope = TestScope(UnconfinedTestDispatcher())
        val viewModel = MainViewModel(testScope)

        assertTrue(viewModel.running)
        viewModel.toggleRunning()
        assertFalse(viewModel.running)
        viewModel.toggleRunning()
        assertTrue(viewModel.running)
    }

    @Test
    fun testShowAndDismissBreak() {
        val testScope = TestScope(UnconfinedTestDispatcher())
        val viewModel = MainViewModel(testScope)

        assertFalse(viewModel.breakVisible)
        viewModel.showBreakNow()
        assertTrue(viewModel.breakVisible)
        viewModel.dismissBreak()
        assertFalse(viewModel.breakVisible)
    }

    @Test
    fun testUpdateInterval() {
        val testScope = TestScope(UnconfinedTestDispatcher())
        val viewModel = MainViewModel(testScope)

        assertEquals(15.minutes, viewModel.breakInterval)
        viewModel.updateInterval(30.minutes)
        assertEquals(30.minutes, viewModel.breakInterval)
    }
}

package com.lazyracoon.grem.util

import com.lazyracoon.grem.viewmodel.MainViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class AutoStartUtilsTest {

    @Test
    fun testOsDetection() {
        val isWin = AutoStartUtils.isWindows()
        val isLin = AutoStartUtils.isLinux()
        assertNotNull(isWin)
        assertNotNull(isLin)
    }

    @Test
    fun testIsAutoStartEnabledDoesNotCrash() {
        val enabled = AutoStartUtils.isAutoStartEnabled()
        assertNotNull(enabled)
    }

    @Test
    fun testViewModelPreferenceState() {
        val testScope = TestScope(UnconfinedTestDispatcher())
        val viewModel = MainViewModel(testScope)

        assertFalse(viewModel.preferenceVisible)
        viewModel.preferenceVisible = true
        assertTrue(viewModel.preferenceVisible)

        val initialState = viewModel.autoStartEnabled
        viewModel.setAutoStart(!initialState)
        viewModel.setAutoStart(initialState)
    }
}

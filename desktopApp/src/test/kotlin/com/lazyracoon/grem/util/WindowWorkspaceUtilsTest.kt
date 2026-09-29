package com.lazyracoon.grem.util

import org.junit.Test
import javax.swing.JDialog
import kotlin.test.assertNotNull

class WindowWorkspaceUtilsTest {

    @Test
    fun testOsDetection() {
        val isLinux = WindowWorkspaceUtils.isLinux()
        assertNotNull(isLinux)
    }

    @Test
    fun testMakeStickyDoesNotCrash() {
        val dialog = JDialog()
        WindowWorkspaceUtils.makeStickyOnAllWorkspaces(dialog)
        dialog.dispose()
    }
}

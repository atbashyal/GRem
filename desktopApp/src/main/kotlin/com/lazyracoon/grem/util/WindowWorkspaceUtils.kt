package com.lazyracoon.grem.util

import com.sun.jna.Memory
import com.sun.jna.Native
import com.sun.jna.NativeLong
import com.sun.jna.platform.unix.X11
import java.awt.Window

object WindowWorkspaceUtils {

    fun isLinux(): Boolean {
        val os = System.getProperty("os.name")?.lowercase() ?: ""
        return os.contains("linux") || os.contains("unix")
    }

    fun makeStickyOnAllWorkspaces(window: Window) {
        if (!isLinux()) return

        var xid: Long = 0L
        try {
            xid = Native.getWindowID(window)
        } catch (t: Throwable) {
            t.printStackTrace()
        }

        if (xid > 0) {
            try {
                makeStickyX11Jna(xid)
            } catch (t: Throwable) {
                t.printStackTrace()
            }

            try {
                makeStickyCommandLine(xid)
            } catch (t: Throwable) {
                t.printStackTrace()
            }
        }
    }

    private fun makeStickyX11Jna(xid: Long) {
        val x11 = X11.INSTANCE
        val display = x11.XOpenDisplay(null) ?: return
        try {
            val rootWindow = x11.XDefaultRootWindow(display)

            val netWmDesktop = x11.XInternAtom(display, "_NET_WM_DESKTOP", false)
            val netWmState = x11.XInternAtom(display, "_NET_WM_STATE", false)
            val netWmStateSticky = x11.XInternAtom(display, "_NET_WM_STATE_STICKY", false)
            val netWmStateAbove = x11.XInternAtom(display, "_NET_WM_STATE_ABOVE", false)

            val mem = Memory(Native.LONG_SIZE.toLong())
            mem.setNativeLong(0, NativeLong(-1L))

            x11.XChangeProperty(
                display,
                X11.Window(xid),
                netWmDesktop,
                X11.XA_CARDINAL,
                32,
                X11.PropModeReplace,
                mem,
                1
            )

            // Send ClientMessage for _NET_WM_DESKTOP = 0xFFFFFFFF
            val eventDesktop = X11.XEvent()
            eventDesktop.type = X11.ClientMessage
            eventDesktop.xclient.type = X11.ClientMessage
            eventDesktop.xclient.window = X11.Window(xid)
            eventDesktop.xclient.message_type = netWmDesktop
            eventDesktop.xclient.format = 32
            eventDesktop.xclient.data.l[0] = NativeLong(-1L)
            eventDesktop.xclient.data.l[1] = NativeLong(1L)

            val mask = NativeLong(X11.SubstructureRedirectMask.toLong() or X11.SubstructureNotifyMask.toLong())
            x11.XSendEvent(display, rootWindow, 0, mask, eventDesktop)

            // Send ClientMessage for _NET_WM_STATE_STICKY and _NET_WM_STATE_ABOVE
            val eventState = X11.XEvent()
            eventState.type = X11.ClientMessage
            eventState.xclient.type = X11.ClientMessage
            eventState.xclient.window = X11.Window(xid)
            eventState.xclient.message_type = netWmState
            eventState.xclient.format = 32
            eventState.xclient.data.l[0] = NativeLong(1L) // _NET_WM_STATE_ADD = 1
            eventState.xclient.data.l[1] = netWmStateSticky
            eventState.xclient.data.l[2] = netWmStateAbove
            eventState.xclient.data.l[3] = NativeLong(1L)

            x11.XSendEvent(display, rootWindow, 0, mask, eventState)
            x11.XFlush(display)
        } finally {
            x11.XCloseDisplay(display)
        }
    }

    private fun makeStickyCommandLine(xid: Long) {
        val hexId = "0x" + java.lang.Long.toHexString(xid)

        try {
            ProcessBuilder("xprop", "-id", hexId, "-f", "_NET_WM_DESKTOP", "32c", "-set", "_NET_WM_DESKTOP", "4294967295")
                .start()
        } catch (_: Exception) {}

        try {
            ProcessBuilder("xprop", "-id", hexId, "-f", "_NET_WM_STATE", "32a", "-set", "_NET_WM_STATE", "_NET_WM_STATE_STICKY,_NET_WM_STATE_ABOVE")
                .start()
        } catch (_: Exception) {}

        try {
            ProcessBuilder("wmctrl", "-i", "-r", hexId, "-b", "add,sticky,above")
                .start()
        } catch (_: Exception) {}
    }
}

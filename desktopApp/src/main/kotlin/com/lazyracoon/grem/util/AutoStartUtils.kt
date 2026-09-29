package com.lazyracoon.grem.util

import com.sun.jna.platform.win32.Advapi32Util
import com.sun.jna.platform.win32.WinReg
import java.io.File

object AutoStartUtils {

    private const val APP_NAME = "GRem"
    private const val REG_RUN_KEY = "Software\\Microsoft\\Windows\\CurrentVersion\\Run"

    fun isWindows(): Boolean {
        return System.getProperty("os.name")?.lowercase()?.contains("win") == true
    }

    fun isLinux(): Boolean {
        val os = System.getProperty("os.name")?.lowercase() ?: ""
        return os.contains("linux") || os.contains("unix")
    }

    fun isAutoStartEnabled(): Boolean {
        return try {
            if (isWindows()) {
                Advapi32Util.registryValueExists(
                    WinReg.HKEY_CURRENT_USER,
                    REG_RUN_KEY,
                    APP_NAME
                )
            } else if (isLinux()) {
                val autostartFile = getLinuxAutostartFile()
                autostartFile.exists()
            } else {
                false
            }
        } catch (t: Throwable) {
            t.printStackTrace()
            false
        }
    }

    fun setAutoStartEnabled(enabled: Boolean): Boolean {
        return try {
            if (isWindows()) {
                if (enabled) {
                    val appPath = getExecutablePath()
                    Advapi32Util.registrySetStringValue(
                        WinReg.HKEY_CURRENT_USER,
                        REG_RUN_KEY,
                        APP_NAME,
                        "\"$appPath\""
                    )
                } else {
                    if (Advapi32Util.registryValueExists(
                            WinReg.HKEY_CURRENT_USER,
                            REG_RUN_KEY,
                            APP_NAME
                        )
                    ) {
                        Advapi32Util.registryDeleteValue(
                            WinReg.HKEY_CURRENT_USER,
                            REG_RUN_KEY,
                            APP_NAME
                        )
                    }
                }
                true
            } else if (isLinux()) {
                val autostartFile = getLinuxAutostartFile()
                if (enabled) {
                    autostartFile.parentFile?.mkdirs()
                    val execCmd = getExecutablePath()
                    val content = """
                        [Desktop Entry]
                        Type=Application
                        Name=GRem
                        Exec="$execCmd"
                        Terminal=false
                        X-GNOME-Autostart-enabled=true
                    """.trimIndent()
                    autostartFile.writeText(content)
                } else {
                    if (autostartFile.exists()) {
                        autostartFile.delete()
                    }
                }
                true
            } else {
                false
            }
        } catch (t: Throwable) {
            t.printStackTrace()
            false
        }
    }

    private fun getLinuxAutostartFile(): File {
        val userHome = System.getProperty("user.home") ?: "."
        return File(userHome, ".config/autostart/grem.desktop")
    }

    private fun getExecutablePath(): String {
        val jpackagePath = System.getProperty("jpackage.app-path")
        if (!jpackagePath.isNullOrEmpty()) {
            return jpackagePath
        }

        try {
            val processCommand = ProcessHandle.current().info().command().orElse(null)
            if (!processCommand.isNullOrEmpty()) {
                return processCommand
            }
        } catch (_: Throwable) {}

        return try {
            val codeSourceLocation = AutoStartUtils::class.java.protectionDomain?.codeSource?.location
            if (codeSourceLocation != null) {
                File(codeSourceLocation.toURI()).absolutePath
            } else {
                "grem"
            }
        } catch (_: Throwable) {
            "grem"
        }
    }
}

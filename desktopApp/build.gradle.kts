import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

dependencies {
    implementation(project(":shared"))

    implementation(compose.desktop.currentOs)
    implementation(libs.kotlinx.coroutinesSwing)

    implementation(libs.compose.uiToolingPreview)
    implementation(libs.compose.components.resources)
    implementation(libs.compose.material3)
    implementation(libs.compose.foundation)
}

compose.desktop {
    application {
        mainClass = "com.lazyracoon.grem.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb, TargetFormat.Exe)
            packageName = "Grem"
            packageVersion = "1.0.0"

            windows {
                iconFile.set(project.file("../shared/src/commonMain/composeResources/drawable/grem.ico"))
            }

            linux {
                iconFile.set(project.file("../shared/src/commonMain/composeResources/drawable/img.png"))
            }
        }
    }
}
import org.jetbrains.kotlin.gradle.plugin.mpp.BitcodeEmbeddingMode
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.cocoapods)
    alias(libs.plugins.compose)
}
compose {
    kotlinCompilerPlugin.set(libs.versions.composeCompiler.get())
}
kotlin {
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        name = "iosComposePod"
        summary = "Common UI-kit for the NoteDelight app"
        homepage = "https://github.com/softartdev/NoteDelight"
        version = "1.0"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../iosApp/Podfile")
        pod("SQLCipher", libs.versions.iosSqlCipher.get())
        framework {
            baseName = "iosComposeKit"
            embedBitcodeMode.set(BitcodeEmbeddingMode.DISABLE)
            freeCompilerArgs += listOf(
                "-linker-option", "-framework", "-linker-option", "Metal",
                "-linker-option", "-framework", "-linker-option", "CoreText",
                "-linker-option", "-framework", "-linker-option", "CoreGraphics",
            )
            export(project(":shared"))
//            export(project(":shared-compose-ui"))
            export(libs.mokoResources)
            export(libs.koin.core)
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":shared"))
                api(project(":shared-compose-ui"))
                implementation(compose.ui)
                implementation(compose.foundation)
                implementation(compose.material)
                implementation(compose.runtime)
                api(libs.mokoResources)
                api(libs.koin.core)
            }
        }
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
    }
    targets.withType<KotlinNativeTarget> {
        binaries.all {
            // TODO: the current compose binary surprises LLVM, so disable checks for now.
            freeCompilerArgs += "-Xdisable-phases=VerifyBitcode"
        }
    }
}

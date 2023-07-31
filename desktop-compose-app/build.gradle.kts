import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}
group = "com.softartdev"
version = "1.0"

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
    }
    sourceSets {
        all {
            languageSettings.optIn("kotlin.RequiresOptIn")
        }
        val jvmMain by getting {
            dependencies {
                implementation(project(":shared"))
                implementation(project(":shared-compose-ui"))
                implementation(libs.decompose)
                implementation(libs.decompose.extComposeJb)
                implementation(libs.coroutines.swing)
                implementation(compose.desktop.currentOs)
                implementation(libs.koin.core.jvm)
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}

compose {
    kotlinCompilerPlugin.set("1.4.8")
    desktop {
        application {
            mainClass = "com.softartdev.notedelight.MainKt"
            nativeDistributions {
                targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
                packageName = "com.softartdev.notedelight"
                packageVersion = "1.0.0"
            }
        }
    }
}
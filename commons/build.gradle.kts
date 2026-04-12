import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.jetbrainsComposeCompiler)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.serialization)
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }

    jvm {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }

    iosArm64()
    iosSimulatorArm64()

    android {
        namespace = "com.vitorpamplona.amethyst.commons"
        compileSdk =
            libs.versions.android.compileSdk
                .get()
                .toInt()
        minSdk =
            libs.versions.android.minSdk
                .get()
                .toInt()

        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }

        androidResources.enable = true

        withHostTest {}

        withDeviceTest {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "CommonsIos"
            isStatic = true
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":quartz"))
                implementation(libs.kotlinx.serialization.json)

                // Serialization
                implementation(libs.kotlinx.serialization.json)

                // Compose Multiplatform
                implementation(libs.jetbrains.compose.ui)
                implementation(libs.jetbrains.compose.foundation)
                implementation(libs.jetbrains.compose.runtime)
                implementation(libs.jetbrains.compose.material3)
                implementation(libs.jetbrains.compose.material.icons.extended)
                implementation(libs.jetbrains.compose.ui.tooling.preview)

                // Lifecycle ViewModel (KMP since 2.8.0) — base only, no compose
                implementation(libs.androidx.lifecycle.viewmodel)
                implementation(libs.androidx.lifecycle.runtime.compose)

                // Image loading (Coil 3 - KMP) — compose only, network backend per platform
                implementation(libs.coil.compose)

                // Coroutines
                implementation(libs.kotlinx.coroutines.core)

                // LruCache (KMP-ready)
                implementation(libs.androidx.collection)

                // Immutable collections
                api(libs.kotlinx.collections.immutable)

                // Kotlin serialization
                implementation(libs.kotlinx.serialization.json)

                // Compose Multiplatform Resources
                implementation(libs.jetbrains.compose.components.resources)
            }
        }

        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
                implementation(libs.kotlinx.coroutines.test)
            }
        }

        // Shared JVM code for both Android and Desktop
        val jvmAndroid =
            create("jvmAndroid") {
                dependsOn(commonMain.get())
                dependencies {
                    // Coil OkHttp network backend (JVM/Android only)
                    implementation(libs.coil.okhttp)

                    // Lifecycle ViewModel Compose (JVM/Android only)
                    implementation(libs.androidx.lifecycle.viewmodel.compose)

                    // Markdown rendering (richtext-commonmark) — JVM/Android only
                    implementation(libs.markdown.commonmark)
                    implementation(libs.markdown.ui)
                    implementation(libs.markdown.ui.material3)
                }
            }

        val iosMain =
            create("iosMain") {
                dependsOn(commonMain.get())
                dependencies {
                    // iOS: Coil network via Ktor (Darwin engine)
                    implementation(libs.coil.network.ktor3)
                    implementation(libs.ktor.client.darwin)
                }
            }

        val iosArm64Main by getting {
            dependsOn(iosMain)
        }

        val iosSimulatorArm64Main by getting {
            dependsOn(iosMain)
        }

        jvmMain {
            dependsOn(jvmAndroid)
            dependencies {
                // Desktop-specific Compose
                implementation(compose.desktop.currentOs)
                implementation(libs.jetbrains.compose.ui.tooling)

                // Secure key storage via OS keychain (macOS/Windows/Linux)
                implementation(libs.java.keyring)
            }
        }

        androidMain {
            dependsOn(jvmAndroid)
            dependencies {
                // Android-specific Compose tooling
                implementation(libs.androidx.ui.tooling.preview)

                // Secure key storage via Android Keystore
                implementation(libs.androidx.security.crypto.ktx)
                implementation(libs.androidx.datastore.preferences)
            }
        }

        val iosMain =
            create("iosMain") {
                dependsOn(commonMain.get())
                dependencies {
                    // Coil Ktor network backend for iOS
                    implementation(libs.coil.ktor)
                }
            }

        getByName("iosArm64Main") {
            dependsOn(iosMain)
        }

        getByName("iosSimulatorArm64Main") {
            dependsOn(iosMain)
        }

        getByName("androidHostTest") {
            dependencies {
                implementation(libs.junit)

                // Bitcoin secp256k1 bindings
                implementation(libs.secp256k1.kmp.jni.jvm)
            }
        }

        getByName("androidDeviceTest") {
            dependencies {
                implementation(libs.androidx.junit)
                implementation(libs.androidx.espresso.core)
            }
        }

        // iOS source sets
        val iosMain = create("iosMain") {
            dependsOn(commonMain.get())
            dependencies {
                // Image loading via Ktor for iOS
                implementation("io.coil-kt.coil3:coil-network-ktor3:${libs.versions.coil.get()}")
                implementation("io.ktor:ktor-client-darwin:3.1.3")
            }
        }

        val iosArm64Main by getting { dependsOn(iosMain) }
        val iosSimulatorArm64Main by getting { dependsOn(iosMain) }
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "com.vitorpamplona.amethyst.commons.resources"
    generateResClass = always
}

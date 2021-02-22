import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("com.android.library")
}

kotlin {
    android()
    ios {
        binaries {
            framework {
                baseName = "GitFoxSDK"
            }
        }
    }

    js {
        browser {
            //workaround https://youtrack.jetbrains.com/issue/KT-36484
            dceTask {
                keep("ktor-ktor-io.\$\$importsForInline\$\$.ktor-ktor-io.io.ktor.utils.io")
            }
            // execute jsBrowserRun to launch dev server
            runTask {
                devServer = KotlinWebpackConfig.DevServer(
                    true,
                    false,
                    true,
                    true,
                    false,
                    8080,
                    null,
                    listOf("${projectDir}/src/jsMain/resources")
                )
                outputFileName = "main.js"
            }
            useCommonJs()
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                //Log
                implementation("com.github.aakira:napier:${properties["version.napier"]}")
                //JSON
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${properties["version.kotlinx.serialization"]}")
                //Preferences
                implementation("com.russhwolf:multiplatform-settings:${properties["version.multiplatformSettings"]}")
                //Network
                implementation("io.ktor:ktor-client-core:${properties["version.ktor"]}")
                implementation("io.ktor:ktor-client-serialization:${properties["version.ktor"]}")
                implementation("io.ktor:ktor-client-auth:${properties["version.ktor"]}")
                implementation("io.ktor:ktor-client-logging:${properties["version.ktor"]}")
                //Coroutines
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${properties["version.kotlinx.coroutines"]}")
                //UUID
                implementation("com.benasher44:uuid:0.1.0")
            }
        }
        val androidMain by getting {
            dependencies {
                //Network
                implementation("io.ktor:ktor-client-okhttp:${properties["version.ktor"]}")
            }
        }
        val iosMain by getting {
            dependencies {
                //Network
                implementation("io.ktor:ktor-client-ios:${properties["version.ktor"]}")
            }
        }
        val jsMain by getting {
            dependencies {
                //Network
                implementation("io.ktor:ktor-client-js:${properties["version.ktor"]}")
            }
        }
        all {
            languageSettings.apply {
                useExperimentalAnnotation("kotlin.ExperimentalStdlibApi")
                useExperimentalAnnotation("kotlinx.coroutines.ExperimentalCoroutinesApi")
                useExperimentalAnnotation("kotlinx.coroutines.FlowPreview")
                useExperimentalAnnotation("kotlinx.coroutines.InternalCoroutinesApi")
            }
        }
    }
}

android {
    compileSdkVersion((properties["android.compileSdk"] as String).toInt())
    defaultConfig {
        minSdkVersion((properties["android.minSdk"] as String).toInt())
        targetSdkVersion((properties["android.targetSdk"] as String).toInt())
    }
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
}

val packForXcode by tasks.creating(Sync::class) {
    group = "build"
    val mode = System.getenv("CONFIGURATION") ?: "DEBUG"
    val sdkName = System.getenv("SDK_NAME") ?: "iphonesimulator"
    val targetName = "ios" + if (sdkName.startsWith("iphoneos")) "Arm64" else "X64"
    val framework = kotlin.targets.getByName<KotlinNativeTarget>(targetName).binaries.getFramework(mode)
    inputs.property("mode", mode)
    dependsOn(framework.linkTask)
    val targetDir = File(buildDir, "xcode-frameworks")
    from({ framework.outputDirectory })
    into(targetDir)
}
tasks.getByName("build").dependsOn(packForXcode)

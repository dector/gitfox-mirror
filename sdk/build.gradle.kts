import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("com.android.library")
}

kotlin {
    android()

    //select iOS target platform depending on the Xcode environment variables
    val iOSTarget: (String, KotlinNativeTarget.() -> Unit) -> KotlinNativeTarget =
        if (System.getenv("SDK_NAME")?.startsWith("iphoneos") == true) ::iosArm64
        else ::iosX64

    iOSTarget("ios") {
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
        val ktorVersion = "1.3.2"
        val coroutinesVersion = "1.3.4"
        val serializationVersion = "0.20.0"
        val napierVersion = "1.2.0"
        val settingsVersion = "0.6"
        commonMain {
            dependencies {
                //Kotlin
                implementation("org.jetbrains.kotlin:kotlin-stdlib-common")
                //Log
                implementation("com.github.aakira:napier:$napierVersion")
                //JSON
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-common:$serializationVersion")
                //Preferences
                implementation("com.russhwolf:multiplatform-settings:$settingsVersion")
                //Network
                implementation("io.ktor:ktor-client-core:$ktorVersion")
                //Coroutines
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-common:$coroutinesVersion")
                //UUID
                implementation("com.benasher44:uuid:0.1.0")
            }
        }
        val androidMain by getting {
            dependencies {
                //Kotlin
                implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
                //Log
                implementation("com.github.aakira:napier-android:$napierVersion")
                //Network
                api("io.ktor:ktor-client-core-jvm:$ktorVersion")
                implementation("io.ktor:ktor-client-serialization-jvm:$ktorVersion")
                implementation("io.ktor:ktor-client-auth-jvm:$ktorVersion")
                implementation("io.ktor:ktor-client-logging-jvm:$ktorVersion")
                implementation("io.ktor:ktor-client-okhttp:$ktorVersion")
                //Coroutines
                api("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")
                //JSON
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:$serializationVersion")
            }
        }
        val iosMain by getting {
            dependencies {
                //Log
                implementation("com.github.aakira:napier-ios:$napierVersion")
                //Network
                implementation("io.ktor:ktor-client-core-native:$ktorVersion")
                implementation("io.ktor:ktor-client-serialization-native:$ktorVersion")
                implementation("io.ktor:ktor-client-auth-native:$ktorVersion")
                implementation("io.ktor:ktor-client-logging-native:$ktorVersion")
                implementation("io.ktor:ktor-client-ios:$ktorVersion")
                //Coroutines
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-native:$coroutinesVersion")
                //JSON
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-native:$serializationVersion")
            }
        }
        val jsMain by getting {
            dependencies {
                //Kotlin
                implementation("org.jetbrains.kotlin:kotlin-stdlib-js")
                //Log
                implementation("com.github.aakira:napier-js:$napierVersion")
                //Network
                implementation("io.ktor:ktor-client-core-js:$ktorVersion")
                implementation("io.ktor:ktor-client-js:$ktorVersion")
                implementation("io.ktor:ktor-client-serialization-js:$ktorVersion")
                implementation("io.ktor:ktor-client-auth-js:$ktorVersion")
                implementation("io.ktor:ktor-client-logging-js:$ktorVersion")
                //Coroutines
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:$coroutinesVersion")
                //JSON
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-js:$serializationVersion")
                //required NPM dependencies
                implementation(npm("text-encoding", "*"))
                implementation(npm("abort-controller", "*"))
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
    compileSdkVersion(29)
    defaultConfig {
        minSdkVersion(19)
        targetSdkVersion(29)
    }
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
}

val packForXcode by tasks.creating(Sync::class) {
    val targetDir = File(buildDir, "xcode-frameworks")

    /// selecting the right configuration for the iOS
    /// framework depending on the environment
    /// variables set by Xcode build
    val mode = System.getenv("CONFIGURATION") ?: "DEBUG"
    val framework = kotlin.targets
        .getByName<KotlinNativeTarget>("ios")
        .binaries.getFramework(mode)
    inputs.property("mode", mode)
    dependsOn(framework.linkTask)

    from({ framework.outputDirectory })
    into(targetDir)

    /// generate a helpful ./gradlew wrapper with embedded Java path
    doLast {
        val gradlew = File(targetDir, "gradlew")
        gradlew.writeText("#!/bin/bash\n"
            + "export 'JAVA_HOME=${System.getProperty("java.home")}'\n"
            + "cd '${rootProject.rootDir}'\n"
            + "./gradlew \$@\n")
        gradlew.setExecutable(true)
    }
}

tasks.getByName("build").dependsOn(packForXcode)

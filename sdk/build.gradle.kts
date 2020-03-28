plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("com.android.library")
}

kotlin {
    android()
    sourceSets {
        val ktorVersion = "1.3.2"
        val coroutinesVersion = "1.3.4"
        commonMain {
            dependencies {
                //Kotlin
                implementation("org.jetbrains.kotlin:kotlin-stdlib-common")
                //Log
                implementation("com.github.aakira:napier:1.2.0")
                //JSON
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-common:0.20.0")
                //Preferences
                implementation("com.russhwolf:multiplatform-settings:0.5.1")
                //Network
                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.ktor:ktor-client-okhttp:$ktorVersion")
                //Coroutines
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-common:$coroutinesVersion")
            }
        }
        val androidMain by getting {
            dependencies {
                //Kotlin
                implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
                //Log
                implementation("com.github.aakira:napier-android:1.2.0")
                //Network
                implementation("io.ktor:ktor-client-serialization-jvm:$ktorVersion")
                implementation("io.ktor:ktor-client-auth-jvm:$ktorVersion")
                implementation("io.ktor:ktor-client-logging-jvm:$ktorVersion")
                implementation("io.ktor:ktor-client-okhttp:$ktorVersion")
                //Coroutines
                api("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")
                //Preferences
                api("com.russhwolf:multiplatform-settings:0.5.1")
            }
        }
        all {
            languageSettings.apply {
                useExperimentalAnnotation("kotlin.ExperimentalStdlibApi")
                useExperimentalAnnotation("kotlinx.coroutines.ExperimentalCoroutinesApi")
                useExperimentalAnnotation("kotlinx.coroutines.FlowPreview")
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

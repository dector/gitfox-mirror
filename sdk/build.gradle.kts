plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("com.android.library")
}

kotlin {
    android()
    js {
        browser {
        }
    }
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
                implementation("com.github.aakira:napier-android:1.2.0")
                //Network
                api("io.ktor:ktor-client-core-jvm:$ktorVersion")
                implementation("io.ktor:ktor-client-serialization-jvm:$ktorVersion")
                implementation("io.ktor:ktor-client-auth-jvm:$ktorVersion")
                implementation("io.ktor:ktor-client-logging-jvm:$ktorVersion")
                implementation("io.ktor:ktor-client-okhttp:$ktorVersion")
                //Coroutines
                api("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")
                //Preferences
                implementation("com.russhwolf:multiplatform-settings:0.5.1")
                //JSON
                api("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.20.0")
            }
        }
        val jsMain by getting {
            dependencies {
                //Kotlin
                implementation("org.jetbrains.kotlin:kotlin-stdlib-js")
                //Log
                implementation("com.github.aakira:napier-js:1.2.0")
                //Network
                implementation("io.ktor:ktor-client-core-js:$ktorVersion")
                implementation("io.ktor:ktor-client-js:$ktorVersion")
                implementation("io.ktor:ktor-client-serialization-js:$ktorVersion")
                implementation("io.ktor:ktor-client-auth-js:$ktorVersion")
                implementation("io.ktor:ktor-client-logging-js:$ktorVersion")
                //Coroutines
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:$coroutinesVersion")
                //Preferences
                implementation("com.russhwolf:multiplatform-settings:0.5.1")
                //JSON
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.20.0")
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

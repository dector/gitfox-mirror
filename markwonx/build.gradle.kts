import com.android.build.gradle.ProguardFiles.getDefaultProguardFile
import org.gradle.internal.impldep.com.amazonaws.PredefinedClientConfigurations.defaultConfig

plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    compileSdkVersion(28)

    defaultConfig {
        minSdkVersion(16)
        targetSdkVersion(28)
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        create("debugPG")

        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                file("config/proguard/proguard-rules.txt")
            )
        }
    }

}

dependencies {
    //Markdown to HTML converter
    compileOnly("ru.noties:markwon:${extra["markwonVersion"] as String}")

    //Kotlin
    compileOnly("org.jetbrains.kotlin:kotlin-stdlib:${extra["kotlinVersion"] as String}")

    //RxJava
    compileOnly("io.reactivex.rxjava2:rxjava:2.2.3")
    compileOnly("io.reactivex.rxjava2:rxandroid:2.1.0")
    compileOnly("com.jakewharton.rxrelay2:rxrelay:2.1.0")

    //JUnit
    testImplementation("junit:junit:4.12")
    //Mockito
    testImplementation("org.mockito:mockito-core:2.8.9")
    //Mockito Kotlin
    testImplementation("com.nhaarman:mockito-kotlin-kt1.1:1.5.0")
}

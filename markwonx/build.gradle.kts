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
    compileOnly("ru.noties:markwon:${Versions.markwon}")
    testImplementation("ru.noties:markwon:${Versions.markwon}")

    //Kotlin
    compileOnly("org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}")
    testImplementation("org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}")

    //JUnit
    testImplementation("junit:junit:4.12")
    //Mockito
    testImplementation("org.mockito:mockito-core:2.27.0")
    //Mockito Kotlin
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.0.0")
    //Roboelectric (to test Spannable)
    testImplementation("org.robolectric:robolectric:4.2.1")
}

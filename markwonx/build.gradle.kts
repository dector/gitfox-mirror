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
    val kotlinVersion = extra["kotlinVersion"] as String
    val markwonVersion = extra["markwonVersion"] as String

    //Markdown to HTML converter
    compileOnly("ru.noties:markwon:$markwonVersion")
    testImplementation("ru.noties:markwon:$markwonVersion")

    //Kotlin
    compileOnly("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")

    //RxJava
    compileOnly("io.reactivex.rxjava2:rxjava:2.2.3")
    compileOnly("io.reactivex.rxjava2:rxandroid:2.1.0")
    compileOnly("com.jakewharton.rxrelay2:rxrelay:2.1.0")

    //JUnit
    testImplementation("junit:junit:4.12")
    //Mockito
    testImplementation("org.mockito:mockito-core:2.27.0")
    //Mockito Kotlin
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.0.0")
    //Roboelectric (to test Spannable)
    testImplementation("org.robolectric:robolectric:4.2.1")
}

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
    val markwonVersion = "2.0.0"
    val coroutinesVersion = "1.3.4"
    //Markdown to HTML converter
    compileOnly("ru.noties:markwon:$markwonVersion")
    testImplementation("ru.noties:markwon:$markwonVersion")

    val kotlinVersion = "1.3.61"
    //Kotlin
    compileOnly("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
    compileOnly("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")

    //JUnit
    testImplementation("junit:junit:4.12")
    //Mockito
    testImplementation("org.mockito:mockito-core:2.27.0")
    //Mockito Kotlin
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.0.0")
    //Roboelectric (to test Spannable)
    testImplementation("org.robolectric:robolectric:4.2.1")
}

buildscript {
    repositories {
        google()
        mavenCentral()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.1.0-alpha05")
        val kotlinVersion = "1.3.70"
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("org.jetbrains.kotlin:kotlin-serialization:$kotlinVersion")
        classpath("org.jetbrains.kotlinx:atomicfu-gradle-plugin:0.14.2")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        jcenter()
    }
}

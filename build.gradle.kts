buildscript {
    repositories {
        google()
        jcenter()
        maven { url = uri("https://maven.fabric.io/public") }
    }
    dependencies {
        classpath("com.android.tools.build:gradle:3.4.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.40")
        classpath("com.google.gms:google-services:4.2.0")
        classpath("io.fabric.tools:gradle:1.29.0")
    }
}

allprojects {
    extra["kotlinVersion"] = "1.3.40"
    repositories {
        google()
        jcenter()
    }
}

val clean by tasks.creating(Delete::class) {
    delete = setOf(rootProject.buildDir)
}

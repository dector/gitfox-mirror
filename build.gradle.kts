buildscript {
    val kotlinVersion = "1.3.0"
    repositories {
        google()
        jcenter()
        maven { url = uri("https://maven.fabric.io/public") }
    }
    dependencies {
        classpath("com.android.tools.build:gradle:3.4.0-alpha04")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("com.google.gms:google-services:3.2.0")
        classpath("io.fabric.tools:gradle:1.26.1")
    }
}

allprojects {
    extra["kotlinVersion"] = "1.3.0"
    repositories {
        google()
        jcenter()
    }
}

val clean by tasks.creating(Delete::class) {
    delete = setOf(rootProject.buildDir)
}

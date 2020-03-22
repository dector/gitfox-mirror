buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.1.0-alpha03")
        val kotlinVersion = "1.3.70"
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("org.jetbrains.kotlin:kotlin-serialization:$kotlinVersion")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}

val clean by tasks.creating(Delete::class) {
    delete = setOf(rootProject.buildDir)
}

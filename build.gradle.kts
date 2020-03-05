buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.1.0-alpha01")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.70")
    }
}

allprojects {
    extra["markwonVersion"] = "2.0.0"
    repositories {
        google()
        jcenter()
    }
}

val clean by tasks.creating(Delete::class) {
    delete = setOf(rootProject.buildDir)
}

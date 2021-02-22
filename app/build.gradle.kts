plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    kotlin("android.extensions")
    id("kotlinx-serialization")
}

apply(from = "${project.rootDir}/codequality/ktlint.gradle.kts")

val buildUid = System.getenv("CI_COMMIT_SHORT_SHA") ?: "local"
val buildName = System.getenv("CI_COMMIT_TAG")?.substring(1) ?: "Dev build"
val buildNumber = System.getenv("CI_PIPELINE_IID")?.toInt() ?: Int.MAX_VALUE
val signKeyAlias = System.getenv("KEY_ALIAS") ?: "alias"
val signKeyPassword = System.getenv("KEY_PASS") ?: "pass"
val oaAppId = System.getenv("OA_APP_ID") ?: "808b7f51c6634294afd879edd75d5eaf55f1a75e7fe5bd91ca8b7140a5af639d"
val oaAppSecret = System.getenv("OA_APP_SECRET") ?: "a9dd39c8d2e781b65814007ca0f8b555d34f79b4d30c9356c38bb7ad9909c6f3"
val oaAppUrl = System.getenv("OA_APP_URL") ?: "app://gitlab.client/"

android {
    compileSdkVersion((properties["android.compileSdk"] as String).toInt())

    defaultConfig {
        applicationId = "com.gitlab.terrakok.gitfox"

        minSdkVersion((properties["android.minSdk"] as String).toInt())
        targetSdkVersion((properties["android.targetSdk"] as String).toInt())
        buildToolsVersion = properties["android.buildToolsVersion"] as String

        versionName = buildName
        versionCode = buildNumber

        lintOptions {
            isWarningsAsErrors = true
            isIgnoreTestSources = true
            lintConfig = file("${project.rootDir}/codequality/lint_rules.xml")
        }

        defaultConfig {
            buildConfigField("String", "VERSION_UID", "\"$buildUid\"")
            buildConfigField(
                "String",
                "APP_DESCRIPTION",
                "\"Gitfox is an Android client for Gitlab.\""
            )
            buildConfigField(
                "String",
                "FEEDBACK_URL",
                "\"https://gitlab.com/terrakok/gitlab-client/issues\""
            )
            buildConfigField(
                "String",
                "APP_HOME_PAGE",
                "\"https://gitlab.com/terrakok/gitlab-client\""
            )

            buildConfigField("String", "WEB_AUTH_USER_AGENT", "\"gitfox_user_agent\"")
            buildConfigField("String", "ORIGIN_GITLAB_ENDPOINT", "\"https://gitlab.com/\"")
            buildConfigField(
                "String",
                "APP_DEVELOPERS_PATH",
                "\"https://gitlab.com/terrakok/gitlab-client/graphs/develop\""
            )

            buildConfigField("String", "OAUTH_APP_ID", "\"$oaAppId\"")
            buildConfigField("String", "OAUTH_SECRET", "\"$oaAppSecret\"")
            buildConfigField("String", "OAUTH_CALLBACK", "\"$oaAppUrl\"")

            multiDexEnabled = true
        }

        signingConfigs {
            create("prod") {
                storeFile = file("../keys/play/key.jks")
                storePassword = signKeyPassword
                keyAlias = signKeyAlias
                keyPassword = signKeyPassword
            }
        }

        buildTypes {
            create("debugPG") {
                initWith(getByName("debug"))
                isMinifyEnabled = true
                versionNameSuffix = " debugPG"
                matchingFallbacks.add("debug")

                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    file("proguard-rules.pro")
                )
            }
            getByName("release") {
                isMinifyEnabled = true
                signingConfig = signingConfigs.getByName("prod")

                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    file("proguard-rules.pro")
                )
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    packagingOptions {
        exclude("META-INF/ktor-client-core.kotlin_module")
        exclude("META-INF/ktor-io.kotlin_module")
        exclude("META-INF/ktor-http.kotlin_module")
        exclude("META-INF/ktor-http-cio.kotlin_module")
        exclude("META-INF/ktor-utils.kotlin_module")
        exclude("META-INF/ktor-client-serialization.kotlin_module")
        exclude("META-INF/ktor-client-json.kotlin_module")
        exclude("META-INF/kotlinx-serialization-runtime.kotlin_module")
        exclude("META-INF/ktor-client-auth.kotlin_module")
        exclude("META-INF/ktor-client-logging.kotlin_module")
    }
}

androidExtensions {
    isExperimental = true
}

dependencies {
    implementation(project(":sdk"))
    //Support
    implementation("androidx.appcompat:appcompat:${properties["version.androidx.appcompat"]}")
    implementation("com.google.android.material:material:${properties["version.androidx.material"]}")
    implementation("androidx.cardview:cardview:${properties["version.androidx.cardview"]}")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:${properties["version.androidx.swiperefreshlayout"]}")
    implementation("androidx.recyclerview:recyclerview:${properties["version.androidx.recyclerview"]}")
    implementation("androidx.constraintlayout:constraintlayout:${properties["version.androidx.constraintlayout"]}")
    //Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${properties["version.kotlinx.coroutines"]}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${properties["version.kotlinx.coroutines"]}")
    //MVP Moxy
    kapt("com.github.moxy-community:moxy-compiler:${properties["version.moxy"]}")
    implementation("com.github.moxy-community:moxy:${properties["version.moxy"]}")
    implementation("com.github.moxy-community:moxy-androidx:${properties["version.moxy"]}")
    //Cicerone Navigation
    implementation("ru.terrakok.cicerone:cicerone:${properties["version.cicerone"]}")
    //DI
    implementation("com.github.stephanenicolas.toothpick:toothpick-runtime:${properties["version.toothpick"]}")
    kapt("com.github.stephanenicolas.toothpick:toothpick-compiler:${properties["version.toothpick"]}")
    //Adapter simplify
    implementation("com.hannesdorfmann:adapterdelegates4:${properties["version.adapterdelegates"]}")
    //Image load and cache
    implementation("com.github.bumptech.glide:glide:${properties["version.glide"]}")
    kapt("com.github.bumptech.glide:compiler:${properties["version.glide"]}")
    implementation("com.github.bumptech.glide:okhttp3-integration:${properties["version.glide"]}")
    //Markdown to HTML converter
    implementation("ru.noties:markwon:${properties["version.markwon"]}")
    implementation("ru.noties:markwon-image-loader:${properties["version.markwon"]}")
    //Bottom navigation bar
    implementation("com.aurelhubert:ahbottomnavigation:${properties["version.ahbottomnavigation"]}")
    //Lottie
    implementation("com.airbnb.android:lottie:${properties["version.lottie"]}")
    //Date
    implementation("com.jakewharton.threetenabp:threetenabp:${properties["version.threetenabp"]}")
    //FlexBox Layout
    implementation("com.google.android:flexbox:${properties["version.flexbox"]}")
    //Log
    implementation("com.github.aakira:napier:${properties["version.napier"]}")
    //JSON
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${properties["version.kotlinx.serialization"]}")
    //Network
    implementation("io.ktor:ktor-client-core:${properties["version.ktor"]}")

    //JUnit
    testImplementation("junit:junit:${properties["version.junit"]}")
    //Mockito
    testImplementation("org.mockito:mockito-core:${properties["version.mockito"]}")
    //Mockito Kotlin
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:${properties["version.mockito.kotlin"]}")
    //Date with timezone
    testImplementation("org.threeten:threetenbp:${properties["version.threetenbp"]}")
}

gradle.buildFinished {
    println("VersionName: ${android.defaultConfig.versionName}")
    println("VersionCode: ${android.defaultConfig.versionCode}")
    println("BuildUid: $buildUid")
}

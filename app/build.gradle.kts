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
    compileSdkVersion(29)

    defaultConfig {
        applicationId = "com.gitlab.terrakok.gitfox"

        minSdkVersion(19)
        targetSdkVersion(29)

        versionName = buildName
        versionCode = buildNumber

        buildToolsVersion = "28.0.3"

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
                matchingFallbacks = mutableListOf("debug")

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
    buildToolsVersion = "29.0.2"

    packagingOptions {
        exclude("META-INF/ktor-client-core.kotlin_module")
        exclude("META-INF/ktor-io.kotlin_module")
        exclude("META-INF/ktor-http.kotlin_module")
        exclude("META-INF/ktor-http-cio.kotlin_module")
        exclude("META-INF/ktor-utils.kotlin_module")
        exclude("META-INF/ktor-client-serialization.kotlin_module")
        exclude("META-INF/ktor-client-json.kotlin_module")
        exclude("META-INF/kotlinx-serialization-runtime.kotlin_module")
    }
}

androidExtensions {
    isExperimental = true
}

dependencies {
    implementation(project(":sdk"))
    //Support
    implementation("androidx.appcompat:appcompat:1.1.0")
    implementation("com.google.android.material:material:1.1.0")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.1.0")
    implementation("androidx.constraintlayout:constraintlayout:1.1.3")
    //MVP Moxy
    val moxyVersion = "2.0.2"
    kapt("com.github.moxy-community:moxy-compiler:$moxyVersion")
    implementation("com.github.moxy-community:moxy:$moxyVersion")
    implementation("com.github.moxy-community:moxy-androidx:$moxyVersion")
    //Cicerone Navigation
    implementation("ru.terrakok.cicerone:cicerone:5.1.0")
    //DI
    val toothpickVersion = "3.1.0"
    implementation("com.github.stephanenicolas.toothpick:toothpick-runtime:$toothpickVersion")
    kapt("com.github.stephanenicolas.toothpick:toothpick-compiler:$toothpickVersion")
    //Adapter simplify
    implementation("com.hannesdorfmann:adapterdelegates4:4.2.0")
    //Image load and cache
    val glideVersion = "4.11.0"
    implementation("com.github.bumptech.glide:glide:$glideVersion")
    kapt("com.github.bumptech.glide:compiler:$glideVersion")
    implementation("com.github.bumptech.glide:okhttp3-integration:$glideVersion")
    //Markdown to HTML converter
    val markwonVersion = "2.0.0"
    implementation("ru.noties:markwon:$markwonVersion")
    implementation("ru.noties:markwon-image-loader:$markwonVersion")
    //Bottom navigation bar
    implementation("com.aurelhubert:ahbottomnavigation:2.3.4")
    //Lottie
    implementation("com.airbnb.android:lottie:3.3.1")
    //Date
    implementation("com.jakewharton.threetenabp:threetenabp:1.2.2")
    //FlexBox Layout
    implementation("com.google.android:flexbox:1.0.0")
    //Log
    implementation("com.github.aakira:napier-android:1.2.0")
    //JSON
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.20.0")

    //JUnit
    testImplementation("junit:junit:4.13")
    //Mockito
    testImplementation("org.mockito:mockito-core:2.27.0")
    //Mockito Kotlin
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0")
    //Date with timezone
    testImplementation("org.threeten:threetenbp:1.4.1")
}

gradle.buildFinished {
    println("VersionName: ${android.defaultConfig.versionName}")
    println("VersionCode: ${android.defaultConfig.versionCode}")
    println("BuildUid: $buildUid")
}

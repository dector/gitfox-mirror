import org.jetbrains.kotlin.kapt3.base.Kapt.kapt

plugins {
    id("com.android.application")
    id("io.fabric")
    kotlin("android")
    kotlin("kapt")
    id("org.jetbrains.kotlin.android.extensions")
}

val buildUid = System.getenv("BUILD_COMMIT_SHA") ?: "local"
android {
    compileSdkVersion(28)

    defaultConfig {
        applicationId = "com.gitlab.terrakok.gitfox"

        minSdkVersion(19)
        targetSdkVersion(28)

        versionName = "1.4.2"
        versionCode = 14

        buildToolsVersion = "28.0.3"

        defaultConfig {
            buildConfigField("String", "VERSION_UID", "\"$buildUid\"")
            buildConfigField("String", "APP_DESCRIPTION", "\"Gitfox is an Android client for Gitlab.\"")
            buildConfigField("String", "FEEDBACK_URL", "\"https://gitlab.com/terrakok/gitlab-client/issues\"")
            buildConfigField("String", "APP_HOME_PAGE", "\"https://gitlab.com/terrakok/gitlab-client\"")

            buildConfigField("String", "WEB_AUTH_USER_AGENT", "\"gitfox_user_agent\"")
            buildConfigField("String", "ORIGIN_GITLAB_ENDPOINT", "\"https://gitlab.com/\"")
            buildConfigField(
                "String",
                "APP_DEVELOPERS_PATH",
                "\"https://gitlab.com/terrakok/gitlab-client/graphs/develop\""
            )

            //todo: put prod value for release
            buildConfigField(
                "String",
                "OAUTH_APP_ID",
                "\"808b7f51c6634294afd879edd75d5eaf55f1a75e7fe5bd91ca8b7140a5af639d\""
            )
            buildConfigField(
                "String",
                "OAUTH_SECRET",
                "\"a9dd39c8d2e781b65814007ca0f8b555d34f79b4d30c9356c38bb7ad9909c6f3\""
            )
            buildConfigField("String", "OAUTH_CALLBACK", "\"app://gitlab.client/\"")

            multiDexEnabled = true
        }

        signingConfigs {
            create("prod") {
                //todo put key params for release
                storeFile = file("../keys/play/key.jks")
                storePassword = "pass"
                keyAlias = "alias"
                keyPassword = "pass"
            }
        }

        buildTypes {
            create("debugPG") {
                initWith(getByName("debug"))
                isMinifyEnabled = true
                versionNameSuffix = " debugPG"

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
}

dependencies {
    val moxyVersion = "1.7.0"
    val toothpickVersion = "2.1.0"
    val retrofitVersion = "2.2.0"
    val markwonVersion = extra["markwonVersion"] as String
    val glideVersion = "4.8.0"

    //Support
    implementation("androidx.appcompat:appcompat:1.0.2")
    implementation("com.google.android.material:material:1.1.0-alpha06")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.constraintlayout:constraintlayout:1.1.3")
    //Kotlin
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${extra["kotlinVersion"] as String}")
    //Log
    implementation("com.jakewharton.timber:timber:4.7.0")
    //MVP Moxy
    kapt("tech.schoolhelper:moxy-x-compiler:$moxyVersion")
    implementation("tech.schoolhelper:moxy-x:$moxyVersion")
    implementation("tech.schoolhelper:moxy-x-androidx:$moxyVersion")
    //Cicerone Navigation
    implementation("ru.terrakok.cicerone:cicerone:5.0.0")
    //DI
    implementation("com.github.stephanenicolas.toothpick:toothpick-runtime:$toothpickVersion")
    kapt("com.github.stephanenicolas.toothpick:toothpick-compiler:$toothpickVersion")
    //Gson
    implementation("com.google.code.gson:gson:2.8.2")
    //Retrofit
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")
    implementation("com.squareup.okhttp3:logging-interceptor:3.11.0")
    implementation("com.squareup.retrofit2:adapter-rxjava2:$retrofitVersion")
    //RxJava
    implementation("io.reactivex.rxjava2:rxandroid:2.1.1")
    implementation("io.reactivex.rxjava2:rxjava:2.2.6")
    implementation("com.jakewharton.rxrelay2:rxrelay:2.1.0")
    //Adapter simplify
    implementation("com.hannesdorfmann:adapterdelegates4:4.0.0")
    //Image load and cache
    implementation("com.github.bumptech.glide:glide:$glideVersion")
    kapt("com.github.bumptech.glide:compiler:$glideVersion")
    implementation("com.github.bumptech.glide:okhttp3-integration:$glideVersion")
    //Markdown to HTML converter
    implementation("ru.noties:markwon:$markwonVersion")
    implementation("ru.noties:markwon-image-loader:$markwonVersion")
    //Bottom navigation bar
    implementation("com.aurelhubert:ahbottomnavigation:2.1.0")
    //Lottie
    implementation("com.airbnb.android:lottie:2.5.1")
    //Date
    implementation("com.jakewharton.threetenabp:threetenabp:1.0.5")
    //FlexBox Layout
    implementation("com.google.android:flexbox:1.0.0")
    //Firebase
    implementation("com.google.firebase:firebase-core:16.0.9")
    //Crashlytics
    implementation("com.crashlytics.sdk.android:crashlytics:2.10.0")

    //Custom GitLab markdown parsing tools
    implementation(project(":markwonx"))

    //JUnit
    testImplementation("junit:junit:4.12")
    //Mockito
    testImplementation("org.mockito:mockito-core:2.8.9")
    //Mockito Kotlin
    testImplementation("com.nhaarman:mockito-kotlin-kt1.1:1.5.0")
}

configurations.all {
    resolutionStrategy {
        force("org.jetbrains.kotlin:kotlin-stdlib:${extra["kotlinVersion"] as String}")
    }
}

gradle.buildFinished {
    println("VersionName: ${android.defaultConfig.versionName}")
    println("VersionCode: ${android.defaultConfig.versionCode}")
    println("BuildUid: $buildUid")
}

apply(plugin = "com.google.gms.google-services")
package com.example.gitfox

import android.os.Bundle
import android.os.PersistableBundle
import androidx.annotation.NonNull;
import gitfox.SDK
import gitfox.create
import gitfox.entity.app.session.OAuthParams
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugins.GeneratedPluginRegistrant

class MainActivity : FlutterActivity() {

    private lateinit var sdk: SDK

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        initSDK()
    }

    private fun MainActivity.initSDK() {
        this.sdk = SDK.create(
                context,
                oAuthParams = OAuthParams(
                        "https://gitlab.com/",
                        "appId",
                        "appKey",
                        "redirectUrl"
                ),
                isDebug = BuildConfig.DEBUG
        )
    }

    override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
        GeneratedPluginRegistrant.registerWith(flutterEngine);
    }
}

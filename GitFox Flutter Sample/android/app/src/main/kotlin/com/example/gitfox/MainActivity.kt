package com.example.gitfox

import android.os.Bundle
import androidx.annotation.NonNull
import gitfox.SDK
import gitfox.create
import gitfox.entity.app.session.OAuthParams
import gitfox.model.interactor.SessionInteractor
import io.flutter.BuildConfig
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugins.GeneratedPluginRegistrant

class MainActivity : FlutterActivity() {

    private val CHANNEL = "gitfox/oauth-url"

    private var sdk: SDK? = null

    private var sessionInteractor: SessionInteractor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initSDK()
    }

    private fun initSDK() {
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

        this.sessionInteractor = sdk?.getSessionInteractor()
    }

    override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
        GeneratedPluginRegistrant.registerWith(flutterEngine);
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL).setMethodCallHandler { call, result ->
            if (call.method == "getOAuthUrl") {
                val oAuthUrl = sessionInteractor?.oauthUrl
                oAuthUrl?.let {
                    result.success(it)
                } ?: run {
                    result.error("UNAVAILABLE", "OAuth URL is not available.", null)
                }
            } else {
                result.notImplemented()
            }
        }
    }
}

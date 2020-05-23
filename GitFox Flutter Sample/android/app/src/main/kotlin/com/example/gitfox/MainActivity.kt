package com.example.gitfox

import android.os.Bundle
import androidx.annotation.NonNull
import com.google.gson.Gson
import gitfox.SDK
import gitfox.create
import gitfox.entity.OrderBy
import gitfox.entity.Project
import gitfox.entity.Sort
import gitfox.entity.app.session.OAuthParams
import gitfox.model.interactor.AccountInteractor
import gitfox.model.interactor.LaunchInteractor
import gitfox.model.interactor.ProjectInteractor
import gitfox.model.interactor.SessionInteractor
import io.flutter.BuildConfig
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugins.GeneratedPluginRegistrant
import kotlinx.coroutines.*

class MainActivity : FlutterActivity(), CoroutineScope by CoroutineScope(Dispatchers.Main) {

    private val CHANNEL = "gitfox/platform"

    private var sdk: SDK? = null

    private var sessionInteractor: SessionInteractor? = null

    private val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initSDK()
    }

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }

    private fun initSDK() {
        this.sdk = SDK.create(
                context,
                oAuthParams = OAuthParams(
                        "https://gitlab.com/",
                        "808b7f51c6634294afd879edd75d5eaf55f1a75e7fe5bd91ca8b7140a5af639d",
                        "a9dd39c8d2e781b65814007ca0f8b555d34f79b4d30c9356c38bb7ad9909c6f3",
                        "app://gitlab.client/"
                ),
                isDebug = BuildConfig.DEBUG
        )

        this.sessionInteractor = sdk?.getSessionInteractor()
    }

    override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
        GeneratedPluginRegistrant.registerWith(flutterEngine);
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL).setMethodCallHandler { call, result ->
            when (call.method) {
                "getOAuthUrl" -> {
                    getOAuthUrl(result)
                }
                "checkOAuthRedirect" -> {
                    checkOAuthRedirect(call, result)
                }
                "login" -> {
                    login(call, result)
                }
                "signInToLastSession" -> {
                    signInToLastSession(result)
                }
                "retrieveProjectsList" ->
                    retrieveProjectsList(result)
                "hasAccount" ->
                    hasAccount(result)
                else -> {
                    result.notImplemented()
                }
            }
        }
    }

    private fun getOAuthUrl(result: MethodChannel.Result) {
        val oAuthUrl = sessionInteractor?.oauthUrl
        oAuthUrl?.let {
            result.success(it)
        } ?: run {
            result.error("N/A",
                    "OAuth URL is not available.",
                    null)
        }
    }

    private fun checkOAuthRedirect(call: MethodCall, result: MethodChannel.Result) {
        val url = call.argument<String>("url") ?: ""
        val isRedirected = sessionInteractor?.checkOAuthRedirect(url)
        isRedirected?.let {
            result.success(isRedirected)
        } ?: run {
            result.error("N/A",
                    "OAuth URL redirect was not checked.",
                    null)
        }
    }

    private fun login(call: MethodCall, result: MethodChannel.Result) {
        val url = call.argument<String>("url") ?: ""
        launch {
            try {
                sessionInteractor?.login(url)
                result.success(true)
            } catch (e: Exception) {
                result.error("N/A",
                        "Error during login.",
                        e.localizedMessage)
            }
        }
    }

    private fun signInToLastSession(result: MethodChannel.Result) {
        sdk?.getLaunchInteractor()?.signInToLastSession()
        result.success(true)
    }

    private fun hasAccount(result: MethodChannel.Result) {
        val hasAccount = sdk?.getLaunchInteractor()?.hasAccount
        result.success(hasAccount)
    }

    private fun retrieveProjectsList(result: MethodChannel.Result) {
        launch {
            try {
                val projects = sdk?.getProjectInteractor()?.getProjectsList(
                        archived = false,
                        visibility = null,
                        orderBy = OrderBy.NAME,
                        sort = Sort.ASC,
                        search = null,
                        simple = null,
                        owned = null,
                        membership = true,
                        starred = null,
                        page = 0,
                        pageSize = 20)
                val projectsJson = gson.toJson(projects)
                result.success(projectsJson)
            } catch (e: Exception) {
                result.error("N/A",
                        "Error during user's projects list retrieving.",
                        e.localizedMessage)
            }
        }
    }
}

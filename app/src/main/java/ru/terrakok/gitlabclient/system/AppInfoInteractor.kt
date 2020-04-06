package ru.terrakok.gitlabclient.system

import android.content.Context
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.list
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import java.io.InputStreamReader
import kotlin.coroutines.resume

class AppInfoInteractor(
    private val context: Context,
    private val appInfo: AppInfo
) {

    fun getAppInfo(): AppInfo = appInfo

    suspend fun getAppLibraries(): List<AppLibrary> = suspendCancellableCoroutine { continuation ->
        context.assets.open("app/app_libraries.json").use { stream ->
            val list: List<AppLibrary> = Json(configuration = JsonConfiguration.Stable).parse(
                AppLibrary.serializer().list,
                InputStreamReader(stream).readText()
            )
            continuation.resume(list)
        }
    }
}

data class AppInfo(
    val versionName: String,
    val versionCode: Int,
    val description: String,
    val buildId: String,
    val url: String,
    val feedbackUrl: String
)

@Serializable
data class AppLibrary(
    @SerialName("name") val name: String,
    @SerialName("url") val url: String,
    @SerialName("license") val license: LicenseType
)

enum class LicenseType {
    MIT,
    APACHE_2,
    CUSTOM,
    NONE
}
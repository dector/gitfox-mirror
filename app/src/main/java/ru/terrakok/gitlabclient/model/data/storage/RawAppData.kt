package ru.terrakok.gitlabclient.model.data.storage

import android.content.res.AssetManager
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.serialization.builtins.list
import kotlinx.serialization.json.Json
import ru.terrakok.gitlabclient.entity.app.develop.AppLibrary
import java.io.InputStreamReader
import kotlin.coroutines.resume

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 03.12.17.
 */
class RawAppData(
    private val assets: AssetManager,
    private val json: Json
) {

    suspend fun getAppLibraries(): List<AppLibrary> = suspendCancellableCoroutine { continuation ->
        assets.open("app/app_libraries.json").use { stream ->
            val list = json.parse(
                AppLibrary.serializer().list,
                InputStreamReader(stream).readText()
            )
            continuation.resume(list)
        }
    }
}

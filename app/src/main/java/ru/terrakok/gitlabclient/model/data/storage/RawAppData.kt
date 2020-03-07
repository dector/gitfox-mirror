package ru.terrakok.gitlabclient.model.data.storage

import android.content.res.AssetManager
import io.reactivex.Single
import kotlinx.serialization.builtins.list
import kotlinx.serialization.json.Json
import ru.terrakok.gitlabclient.entity.app.develop.AppLibrary
import java.io.InputStreamReader
import javax.inject.Inject

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 03.12.17.
 */
class RawAppData @Inject constructor(
    private val assets: AssetManager,
    private val json: Json
) {

    fun getAppLibraries(): Single<List<AppLibrary>> = Single.defer {
        assets.open("app/app_libraries.json").use { stream ->
            Single.just<List<AppLibrary>>(
                json.parse(
                    AppLibrary.serializer().list,
                    InputStreamReader(stream).readText()
                )
            )
        }
    }
}

package ru.terrakok.gitlabclient.model.data.storage

import android.content.res.AssetManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.Single
import ru.terrakok.gitlabclient.entity.app.develop.AppDeveloper
import ru.terrakok.gitlabclient.entity.app.develop.AppLibrary
import java.io.InputStreamReader
import javax.inject.Inject

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 03.12.17.
 */
class RawAppData @Inject constructor(
        private val assets: AssetManager,
        private val gson: Gson
) {

    fun getAppLibraries(): Single<List<AppLibrary>> = fromAsset("app_libraries.json")
    fun getAppDevelopers(): Single<List<AppDeveloper>> = fromAsset("app_developers.json")

    private inline fun <reified T> fromAsset(pathToAsset: String) = Single.defer {
        assets.open(pathToAsset).use { stream ->
            Single.just<T>(gson.fromJson(InputStreamReader(stream), object : TypeToken<T>() {}.type))
        }
    }

}
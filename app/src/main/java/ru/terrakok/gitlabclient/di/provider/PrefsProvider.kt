package ru.terrakok.gitlabclient.di.provider

import android.content.Context
import com.russhwolf.settings.AndroidSettings
import gitfox.model.data.storage.Prefs
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Provider

class PrefsProvider @Inject constructor(
        private val context: Context,
        private val json: Json
) : Provider<Prefs> {

    override fun get() = Prefs(
        AndroidSettings(
            context.getSharedPreferences(
                "gf_prefs",
                Context.MODE_PRIVATE
            )
        ),
        json
    )
}

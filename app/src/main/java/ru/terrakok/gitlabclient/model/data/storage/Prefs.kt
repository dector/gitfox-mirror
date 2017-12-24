package ru.terrakok.gitlabclient.model.data.storage

import android.content.Context
import ru.terrakok.gitlabclient.model.data.auth.AuthHolder
import ru.terrakok.gitlabclient.toothpick.qualifier.DefaultServerPath
import javax.inject.Inject

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 28.03.17
 */
class Prefs @Inject constructor(
        private val context: Context,
        @DefaultServerPath private val defaultServerPath: String
) : AuthHolder {
    private val AUTH_DATA = "auth_data"
    private val KEY_TOKEN = "ad_token"
    private val KEY_SERVER_PATH = "ad_server_path"
    private val KEY_IS_OAUTH = "ad_is_oauth"

    private fun getSharedPreferences(prefsName: String)
            = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)

    override var token: String?
        get() = getSharedPreferences(AUTH_DATA).getString(KEY_TOKEN, null)
        set(value) {
            getSharedPreferences(AUTH_DATA).edit().putString(KEY_TOKEN, value).apply()
        }

    override var serverPath: String
        get() = getSharedPreferences(AUTH_DATA).getString(KEY_SERVER_PATH, defaultServerPath)
        set(value) {
            getSharedPreferences(AUTH_DATA).edit().putString(KEY_SERVER_PATH, value).apply()
        }

    override var isOAuthToken: Boolean
        get() = getSharedPreferences(AUTH_DATA).getBoolean(KEY_IS_OAUTH, false)
        set(value) {
            getSharedPreferences(AUTH_DATA).edit().putBoolean(KEY_IS_OAUTH, value).apply()
        }
}
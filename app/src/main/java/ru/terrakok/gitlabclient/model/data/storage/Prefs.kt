package ru.terrakok.gitlabclient.model.data.storage

import android.content.Context
import ru.terrakok.gitlabclient.model.data.auth.AuthHolder
import javax.inject.Inject

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 28.03.17
 */
class Prefs @Inject constructor(private val context: Context) : AuthHolder {
    private val AUTH_DATA = "auth_data"
    private val KEY_TOKEN = "ad_token"

    private fun getSharedPreferences(prefsName: String)
            = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)

    override fun getAuthToken(): String? = getSharedPreferences(AUTH_DATA).getString(KEY_TOKEN, null)
    override fun putAuthToken(token: String?) = getSharedPreferences(AUTH_DATA).edit().putString(KEY_TOKEN, token).apply()
}
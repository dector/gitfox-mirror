package ru.terrakok.gitlabclient.model.storage

import android.content.Context

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 28.03.17
 */
class Prefs(private val context: Context) {
    private val AUTH_DATA = "auth_data"
    private val KEY_TOKEN = "ad_token"

    private fun getSharedPreferences(prefsName: String)
            = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)

    fun getAuthToken(): String? = getSharedPreferences(AUTH_DATA).getString(KEY_TOKEN, null)
    fun putAuthToken(token: String?) = getSharedPreferences(AUTH_DATA).edit().putString(KEY_TOKEN, token).apply()
}
package ru.terrakok.gitlabclient.model.data.storage

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.terrakok.gitlabclient.entity.app.session.UserAccount
import javax.inject.Inject

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 28.03.17
 */
class Prefs @Inject constructor(
    private val context: Context,
    private val gson: Gson
) {

    private fun getSharedPreferences(prefsName: String) = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)

    //region auth
    private val AUTH_DATA = "auth_data"
    private val KEY_CURRENT_ACCOUNT = "ad_current_account"
    private val KEY_USER_ACCOUNTS = "ad_accounts"
    private val authPrefs by lazy { getSharedPreferences(AUTH_DATA) }

    var selectedAccount: String?
        get() = authPrefs.getString(KEY_CURRENT_ACCOUNT, null)
        set(value) {
            authPrefs.edit().putString(KEY_CURRENT_ACCOUNT, value).apply()
        }

    private val accountsTypeToken = object : TypeToken<List<UserAccount>>() {}.type
    var accounts: List<UserAccount>
        get() {
            //todo restore current account
            return gson.fromJson(authPrefs.getString(KEY_USER_ACCOUNTS, "[]"), accountsTypeToken)
        }
        set(value) {
            authPrefs.edit().putString(KEY_USER_ACCOUNTS, gson.toJson(value)).apply()
        }
    //endregion

    //region app
    private val APP_DATA = "app_data"
    private val KEY_FIRST_LAUNCH_TIME = "launch_ts"
    private val appPrefs by lazy { getSharedPreferences(APP_DATA) }

    var firstLaunchTimeStamp: Long?
        get() = appPrefs.getLong(KEY_FIRST_LAUNCH_TIME, 0).takeIf { it > 0 }
        set(value) {
            appPrefs.edit().putLong(KEY_FIRST_LAUNCH_TIME, value ?: 0).apply()
        }
    //endregion
}
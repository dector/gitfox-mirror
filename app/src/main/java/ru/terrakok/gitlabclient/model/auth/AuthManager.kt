package ru.terrakok.gitlabclient.model.auth

import ru.terrakok.gitlabclient.model.storage.Prefs

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 27.03.17
 */
class AuthManager(private val prefs: Prefs) {

    fun isSignedIn() = !prefs.getAuthToken().isNullOrEmpty()
    fun getToken() = prefs.getAuthToken()
    fun saveToken(token: String) = prefs.putAuthToken(token)
    fun logout() = prefs.putAuthToken(null)
}
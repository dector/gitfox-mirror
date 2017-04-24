package ru.terrakok.gitlabclient.model.auth

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 23.04.17.
 */
interface AuthData {
    fun putAuthToken(token: String?)
    fun getAuthToken(): String?
}
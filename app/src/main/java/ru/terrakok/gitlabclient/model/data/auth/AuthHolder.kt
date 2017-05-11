package ru.terrakok.gitlabclient.model.data.auth

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 23.04.17.
 */
interface AuthHolder {
    fun putAuthToken(token: String?)
    fun getAuthToken(): String?
}
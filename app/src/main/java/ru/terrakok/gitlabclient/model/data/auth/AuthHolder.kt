package ru.terrakok.gitlabclient.model.data.auth

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 23.04.17.
 */
interface AuthHolder {
    var token: String?
    var serverPath: String
    var isOAuthToken: Boolean
}
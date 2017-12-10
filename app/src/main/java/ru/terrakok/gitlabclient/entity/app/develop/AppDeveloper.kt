package ru.terrakok.gitlabclient.entity.app.develop

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 03.12.17.
 */
data class AppDeveloper(
        val name: String,
        val gitlabId: Long? = null,
        val email: String? = null,
        val role: String
)

package ru.terrakok.gitlabclient.entity.app.develop

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 03.12.17.
 */
data class AppInfo(
        val versionName: String,
        val versionCode: Int,
        val description: String,
        val buildId: String,
        val url: String,
        val feedbackEmail: String
)

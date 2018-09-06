package ru.terrakok.gitlabclient.entity.app.develop

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 03.12.17.
 */
data class AppLibrary(
    val name: String,
    val url: String,
    val license: LicenseType
)

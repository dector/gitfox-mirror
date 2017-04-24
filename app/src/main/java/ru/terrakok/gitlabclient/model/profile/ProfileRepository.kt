package ru.terrakok.gitlabclient.model.profile

import ru.terrakok.gitlabclient.model.server.GitlabApi

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 24.04.17.
 */
class ProfileRepository(private val api: GitlabApi) {
    fun getMyProfile() = api.getMyUser()
}
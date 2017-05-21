package ru.terrakok.gitlabclient.entity.app

import ru.terrakok.gitlabclient.entity.common.User

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 11.05.17.
 */

data class MyUserInfo(val user: User?, val serverName: String)
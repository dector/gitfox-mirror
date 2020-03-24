package ru.terrakok.gitlabclient.entity

import io.ktor.utils.io.errors.IOException

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 03.11.17.
 */
class ServerError(val errorCode: Int) : IOException()
class TokenInvalidError : RuntimeException()
class ReadmeNotFound : Exception()

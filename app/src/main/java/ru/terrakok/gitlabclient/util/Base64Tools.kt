package ru.terrakok.gitlabclient.util

import android.util.Base64

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 29.05.17
 */
class Base64Tools {
    fun decode(input: String) = String(Base64.decode(input.toByteArray(), Base64.DEFAULT))
    fun encode(input: String) = String(Base64.encode(input.toByteArray(), Base64.DEFAULT))
}

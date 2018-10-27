package ru.terrakok.gitlabclient.model.system

import android.content.Context
import javax.inject.Inject

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 28.03.17
 */
class ResourceManager @Inject constructor(private val context: Context) {

    fun getString(id: Int) = context.getString(id)

    fun getString(id: Int, vararg formatArgs: Any) = String.format(context.getString(id, *formatArgs))
}
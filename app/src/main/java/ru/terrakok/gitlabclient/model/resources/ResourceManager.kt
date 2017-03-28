package ru.terrakok.gitlabclient.model.resources

import android.content.Context

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 28.03.17
 */
class ResourceManager(private val context: Context) {

    fun getString(id: Int) = context.getString(id)
}
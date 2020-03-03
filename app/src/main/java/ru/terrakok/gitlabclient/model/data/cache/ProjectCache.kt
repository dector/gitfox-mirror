package ru.terrakok.gitlabclient.model.data.cache

import javax.inject.Inject
import ru.terrakok.gitlabclient.di.CacheLifetime
import ru.terrakok.gitlabclient.di.PrimitiveWrapper
import ru.terrakok.gitlabclient.entity.Project
import timber.log.Timber

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 14.10.18.
 */
class ProjectCache @Inject constructor(
    @CacheLifetime lifetimeWrapper: PrimitiveWrapper<Long>
) : ExpirableCache<Long, Project>(lifetimeWrapper.value) {

    override val itemType = "project"

    fun put(data: List<Project>) {
        Timber.d("Put projects")
        data.forEach {
            put(it.id, it)
        }
    }
}

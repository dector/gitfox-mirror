package ru.terrakok.gitlabclient.model.data.cache

import ru.terrakok.gitlabclient.entity.Label
import ru.terrakok.gitlabclient.toothpick.PrimitiveWrapper
import ru.terrakok.gitlabclient.toothpick.qualifier.CacheLifetime
import timber.log.Timber
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

class ProjectLabelCache @Inject constructor(
    @CacheLifetime lifetimeWrapper: PrimitiveWrapper<Long>
) {
    private val lifetime = lifetimeWrapper.value

    private data class ProjectLabelsCacheItem(val time: Long, val data: List<Label>)

    private val cache = ConcurrentHashMap<Long, ProjectLabelsCacheItem>()

    fun clear() {
        Timber.d("Clear cache")
        cache.clear()
    }

    fun put(projectId: Long, data: List<Label>) {
        Timber.d("Put labels")
        cache.put(projectId, ProjectLabelsCacheItem(System.currentTimeMillis(), data))
    }

    fun get(projectId: Long): List<Label>? {
        val item = cache[projectId]
        if (item == null || System.currentTimeMillis() - item.time > lifetime) {
            Timber.d("Get NULL project($projectId) labels")
            return null
        } else {
            Timber.d("Get CACHED project($projectId) labels")
            return item.data
        }
    }

}
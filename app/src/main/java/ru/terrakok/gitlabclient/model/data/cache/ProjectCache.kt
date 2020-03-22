package ru.terrakok.gitlabclient.model.data.cache

import com.github.aakira.napier.Napier
import ru.terrakok.gitlabclient.entity.Project
import java.util.concurrent.ConcurrentHashMap

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 14.10.18.
 */
class ProjectCache(private val lifetime: Long) {

    private data class ProjectCacheItem(val time: Long, val data: Project)

    private val cache = ConcurrentHashMap<Long, ProjectCacheItem>()

    fun clear() {
        Napier.d("Clear cache")
        cache.clear()
    }

    fun put(data: List<Project>) {
        Napier.d("Put projects")
        cache.putAll(
            data
                .asSequence()
                .map { ProjectCacheItem(System.currentTimeMillis(), it) }
                .associateBy { it.data.id }
        )
    }

    fun get(id: Long): Project? {
        val item = cache[id]
        if (item == null || System.currentTimeMillis() - item.time > lifetime) {
            Napier.d("Get NULL project($id)")
            return null
        } else {
            Napier.d("Get CACHED project($id)")
            return item.data
        }
    }
}

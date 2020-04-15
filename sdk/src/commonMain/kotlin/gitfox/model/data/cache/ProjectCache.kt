package gitfox.model.data.cache

import com.github.aakira.napier.Napier
import gitfox.entity.Project
import gitfox.util.currentTimeMillis

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 14.10.18.
 */
internal class ProjectCache(private val lifetime: Long) {

    private data class ProjectCacheItem(val time: Long, val data: Project)

    private val cache = mutableMapOf<Long, ProjectCacheItem>()

    fun clear() {
        Napier.d("Clear cache")
        cache.clear()
    }

    fun put(data: List<Project>) {
        Napier.d("Put projects")
        cache.putAll(
            data
                .asSequence()
                .map { ProjectCacheItem(currentTimeMillis(), it) }
                .associateBy { it.data.id }
        )
    }

    fun get(id: Long): Project? {
        val item = cache[id]
        if (item == null || currentTimeMillis() - item.time > lifetime) {
            Napier.d("Get NULL project($id)")
            return null
        } else {
            Napier.d("Get CACHED project($id)")
            return item.data
        }
    }
}

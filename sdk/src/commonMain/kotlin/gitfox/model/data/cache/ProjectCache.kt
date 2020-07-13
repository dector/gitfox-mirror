package gitfox.model.data.cache

import com.github.aakira.napier.Napier
import gitfox.entity.Project

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 14.10.18.
 */
internal class ProjectCache constructor(
    cacheLifetime: Long
) : ExpirableCache<Long, Project>(cacheLifetime) {

    override val itemType = "project"

    fun put(data: List<Project>) {
        Napier.d("Put projects")
        data.forEach {
            put(it.id, it)
        }
    }
}

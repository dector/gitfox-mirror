package ru.terrakok.gitlabclient.model.data.cache

import ru.terrakok.gitlabclient.di.CacheLifetime
import ru.terrakok.gitlabclient.di.PrimitiveWrapper
import ru.terrakok.gitlabclient.entity.milestone.Milestone
import javax.inject.Inject

class ProjectMilestoneCache @Inject constructor(
    @CacheLifetime lifetimeWrapper: PrimitiveWrapper<Long>
) : ExpirableCache<Long, List<Milestone>>(lifetimeWrapper.value) {

    override val itemType = "project milestone"
}
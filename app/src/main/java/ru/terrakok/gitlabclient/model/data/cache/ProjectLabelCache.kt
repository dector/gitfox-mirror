package ru.terrakok.gitlabclient.model.data.cache

import ru.terrakok.gitlabclient.di.CacheLifetime
import ru.terrakok.gitlabclient.di.PrimitiveWrapper
import ru.terrakok.gitlabclient.entity.Label
import javax.inject.Inject

class ProjectLabelCache @Inject constructor(
    @CacheLifetime lifetimeWrapper: PrimitiveWrapper<Long>
) : ExpirableCache<Long, List<Label>>(lifetimeWrapper.value) {

    override val itemType = "project label"
}
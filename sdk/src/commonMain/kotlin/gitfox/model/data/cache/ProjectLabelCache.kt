package gitfox.model.data.cache

import gitfox.entity.Label

class ProjectLabelCache constructor(
    lifetime: Long
) : ExpirableCache<Long, List<Label>>(lifetime) {

    override val itemType = "project label"
}
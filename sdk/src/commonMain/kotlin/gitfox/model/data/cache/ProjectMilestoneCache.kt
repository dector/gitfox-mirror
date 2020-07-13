package gitfox.model.data.cache

import gitfox.entity.Milestone

class ProjectMilestoneCache constructor(
    lifetime: Long
) : ExpirableCache<Long, List<Milestone>>(lifetime) {

    override val itemType = "project milestone"
}
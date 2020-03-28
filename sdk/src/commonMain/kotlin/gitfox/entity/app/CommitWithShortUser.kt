package gitfox.entity.app

import gitfox.entity.Commit
import gitfox.entity.ShortUser

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 20.10.18.
 */
data class CommitWithShortUser(val commit: Commit, val shortUser: ShortUser?)

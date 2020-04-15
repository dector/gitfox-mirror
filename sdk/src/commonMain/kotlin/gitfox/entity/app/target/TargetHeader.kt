package gitfox.entity.app.target

import gitfox.entity.ShortUser
import gitfox.entity.Time

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 24.12.17.
 */
sealed class TargetHeader {
    data class Public(
        val author: ShortUser,
        val icon: TargetHeaderIcon,
        val title: TargetHeaderTitle,
        val body: CharSequence,
        val date: Time,
        val target: AppTarget,
        val targetId: Long,
        val internal: TargetInternal?,
        val badges: List<TargetBadge>,
        val action: TargetAction
    ) : TargetHeader()

    object Confidential : TargetHeader()
}

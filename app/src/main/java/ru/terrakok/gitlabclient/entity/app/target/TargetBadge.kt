package ru.terrakok.gitlabclient.entity.app.target

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 26.12.17.
 */
sealed class TargetBadge(
    val target: AppTarget?,
    val targetId: Long?,
    val internal: TargetInternal?
) {
    class Text(
        val text: String,
        target: AppTarget? = null,
        targetId: Long? = null,
        internal: TargetInternal? = null
    ) : TargetBadge(target, targetId, internal)

    class Icon(
        val icon: TargetBadgeIcon,
        val count: Int
    ) : TargetBadge(null, null, null)

    class Status(
        val status: TargetBadgeStatus
    ) : TargetBadge(null, null, null)
}
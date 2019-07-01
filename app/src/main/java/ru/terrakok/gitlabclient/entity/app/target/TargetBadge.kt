package ru.terrakok.gitlabclient.entity.app.target

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 26.12.17.
 */
sealed class TargetBadge {
    data class Text(
            val text: String,
            val target: AppTarget? = null,
            val targetId: Long? = null,
            val internal: TargetInternal? = null
    ) : TargetBadge()

    data class Icon(
            val icon: TargetBadgeIcon,
            val count: Int
    ) : TargetBadge()

    data class Status(
            val status: TargetBadgeStatus
    ) : TargetBadge()
}
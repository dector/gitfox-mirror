package ru.terrakok.gitlabclient.entity.app.target

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 26.12.17.
 */
sealed class TargetBadge(
        val target: AppTarget?,
        val targetId: Long?
) {
    class Text(
            val text: String,
            target: AppTarget? = null,
            targetId: Long? = null
    ) : TargetBadge(target, targetId)

    class Comments(
            val count: Int
    ) : TargetBadge(null, null)
}
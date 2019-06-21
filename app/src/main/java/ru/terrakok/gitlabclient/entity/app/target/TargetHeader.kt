package ru.terrakok.gitlabclient.entity.app.target

import org.threeten.bp.ZonedDateTime
import ru.terrakok.gitlabclient.entity.ShortUser

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 24.12.17.
 */
sealed class TargetHeader {
    data class Public(
        val author: ShortUser,
        val icon: TargetHeaderIcon,
        val title: TargetHeaderTitle,
        val body: String,
        val date: ZonedDateTime,
        val target: AppTarget,
        val targetId: Long,
        val internal: TargetInternal?,
        val badges: List<TargetBadge>,
        val action: TargetAction
    ) : TargetHeader()

    object Confidential : TargetHeader()
}
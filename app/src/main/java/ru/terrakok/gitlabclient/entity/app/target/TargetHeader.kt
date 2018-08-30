package ru.terrakok.gitlabclient.entity.app.target

import org.threeten.bp.LocalDateTime
import ru.terrakok.gitlabclient.entity.Author

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 24.12.17.
 */
data class TargetHeader(
        val author: Author,
        val icon: TargetHeaderIcon,
        val title: TargetHeaderTitle,
        val body: CharSequence,
        val date: LocalDateTime,
        val target: AppTarget,
        val targetId: Long,
        val internal: TargetInternal?,
        val badges: List<TargetBadge>
)
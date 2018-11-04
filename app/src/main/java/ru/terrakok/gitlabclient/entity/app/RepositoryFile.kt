package ru.terrakok.gitlabclient.entity.app

import org.threeten.bp.LocalDateTime
import ru.terrakok.gitlabclient.entity.RepositoryTreeNodeType

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 02.11.18.
 */
data class RepositoryFile(
    val id: String,
    val name: String,
    val nodeType: RepositoryTreeNodeType,
    val commitMessage: String,
    val authoredDate: LocalDateTime
)
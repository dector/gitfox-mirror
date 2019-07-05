package ru.terrakok.gitlabclient.entity.todo

import org.threeten.bp.ZonedDateTime
import ru.terrakok.gitlabclient.entity.Project
import ru.terrakok.gitlabclient.entity.ShortUser
import ru.terrakok.gitlabclient.entity.target.Target
import ru.terrakok.gitlabclient.entity.target.TargetType

/**
 * @author Eugene Shapovalov (CraggyHaggy). Date: 11.09.17
 */
data class Todo(
    val id: Long,
    val project: Project,
    val author: ShortUser,
    val actionName: TodoAction,
    val targetType: TargetType,
    val target: Target,
    val targetUrl: String,
    val body: String,
    val state: TodoState,
    val createdAt: ZonedDateTime
)
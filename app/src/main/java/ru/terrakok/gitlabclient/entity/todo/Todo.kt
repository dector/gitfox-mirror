package ru.terrakok.gitlabclient.entity.todo

import ru.terrakok.gitlabclient.entity.Author
import ru.terrakok.gitlabclient.entity.Project
import ru.terrakok.gitlabclient.entity.target.Target
import ru.terrakok.gitlabclient.entity.target.TargetType
import java.util.*

/**
 * @author Eugene Shapovalov (CraggyHaggy). Date: 11.09.17
 */
data class Todo(
        val id: Long,
        val project: Project,
        val author: Author,
        val actionName: TodoAction,
        val targetType: TargetType,
        val target: Target,
        val targetUrl: String,
        val body: String,
        val state: TodoState,
        val createdAt: Date
)
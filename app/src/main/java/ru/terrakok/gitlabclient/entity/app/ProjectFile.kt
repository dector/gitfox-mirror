package ru.terrakok.gitlabclient.entity.app

import ru.terrakok.gitlabclient.entity.RepositoryTreeNodeType

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 02.11.18.
 */
data class ProjectFile(
    val id: String,
    val name: String,
    val nodeType: RepositoryTreeNodeType
)

package gitfox.entity.app

import gitfox.entity.RepositoryTreeNodeType

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 02.11.18.
 */
data class ProjectFile(
    val id: String,
    val name: String,
    val nodeType: RepositoryTreeNodeType
)

package gitfox.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * @author Eugene Shapovalov (CraggyHaggy). Date: 06.02.18
 */
@Serializable
data class RepositoryTreeNode(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("type") val type: RepositoryTreeNodeType,
    @SerialName("path") val path: String,
    @SerialName("mode") val mode: String
)

@Serializable
enum class RepositoryTreeNodeType(private val jsonName: String) {
    @SerialName("tree")
    TREE("tree"),
    @SerialName("blob")
    BLOB("blob");

    override fun toString() = jsonName
}

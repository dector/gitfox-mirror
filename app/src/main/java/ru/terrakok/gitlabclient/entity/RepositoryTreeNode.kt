package ru.terrakok.gitlabclient.entity

import com.google.gson.annotations.SerializedName

/**
 * @author Eugene Shapovalov (CraggyHaggy). Date: 06.02.18
 */
data class RepositoryTreeNode(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("type") val type: RepositoryTreeNodeType,
    @SerializedName("path") val path: String,
    @SerializedName("mode") val mode: String
)

enum class RepositoryTreeNodeType(private val jsonName: String) {
    @SerializedName("tree")
    TREE("tree"),
    @SerializedName("blob")
    BLOB("blob");

    override fun toString() = jsonName
}

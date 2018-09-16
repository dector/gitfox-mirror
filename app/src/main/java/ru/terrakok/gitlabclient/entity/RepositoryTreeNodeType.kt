package ru.terrakok.gitlabclient.entity

import com.google.gson.annotations.SerializedName

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 08.02.18.
 */
enum class RepositoryTreeNodeType(private val jsonName: String) {
    @SerializedName("tree")
    TREE("tree"),
    @SerializedName("blob")
    BLOB("blob");

    override fun toString() = jsonName
}
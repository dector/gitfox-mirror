package ru.terrakok.gitlabclient.entity.todo

import com.google.gson.annotations.SerializedName

/**
 * @author Eugene Shapovalov (CraggyHaggy). Date: 11.09.17
 */
enum class TodoAction(private val jsonName: String) {
    @SerializedName("assigned")
    ASSIGNED("assigned"),
    @SerializedName("mentioned")
    MENTIONED("mentioned"),
    @SerializedName("build_failed")
    BUILD_FAILED("build_failed"),
    @SerializedName("marked")
    MARKED("marked"),
    @SerializedName("approval_required")
    APPROVAL_REQUIRED("approval_required"),
    @SerializedName("directly_addressed")
    DIRECTLY_ADDRESSED("directly_addressed"),
    @SerializedName("unmergeable")
    UNMERGEABLE("unmergeable");

    override fun toString() = jsonName
}

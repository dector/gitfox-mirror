package ru.terrakok.gitlabclient.entity

import com.google.gson.annotations.SerializedName
import org.threeten.bp.ZonedDateTime

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

enum class TodoState(private val jsonName: String) {
    @SerializedName("pending")
    PENDING("pending"),
    @SerializedName("done")
    DONE("done");

    override fun toString() = jsonName
}

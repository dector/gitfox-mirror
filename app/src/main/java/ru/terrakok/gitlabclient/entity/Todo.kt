@file:UseSerializers(ZonedDateTimeDeserializer::class)
package ru.terrakok.gitlabclient.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import org.threeten.bp.ZonedDateTime
import ru.terrakok.gitlabclient.model.data.server.deserializer.TodoDeserializer
import ru.terrakok.gitlabclient.model.data.server.deserializer.ZonedDateTimeDeserializer

/**
 * @author Eugene Shapovalov (CraggyHaggy). Date: 11.09.17
 */
@Serializable(with = TodoDeserializer::class)
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

@Serializable
enum class TodoAction(private val jsonName: String) {
    @SerialName("assigned")
    ASSIGNED("assigned"),
    @SerialName("mentioned")
    MENTIONED("mentioned"),
    @SerialName("build_failed")
    BUILD_FAILED("build_failed"),
    @SerialName("marked")
    MARKED("marked"),
    @SerialName("approval_required")
    APPROVAL_REQUIRED("approval_required"),
    @SerialName("directly_addressed")
    DIRECTLY_ADDRESSED("directly_addressed"),
    @SerialName("unmergeable")
    UNMERGEABLE("unmergeable");

    override fun toString() = jsonName
}

@Serializable
enum class TodoState(private val jsonName: String) {
    @SerialName("pending")
    PENDING("pending"),
    @SerialName("done")
    DONE("done");

    override fun toString() = jsonName
}

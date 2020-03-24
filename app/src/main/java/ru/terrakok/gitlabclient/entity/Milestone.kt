@file:UseSerializers(
    DateDeserializer::class,
    TimeDeserializer::class
)

package ru.terrakok.gitlabclient.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import ru.terrakok.gitlabclient.model.data.server.deserializer.DateDeserializer
import ru.terrakok.gitlabclient.model.data.server.deserializer.TimeDeserializer

@Serializable
data class Milestone(
    @SerialName("id") val id: Long,
    @SerialName("iid") val iid: Long,
    @SerialName("project_id") val projectId: Long? = null,
    @SerialName("description") val description: String? = null,
    @SerialName("state") val state: MilestoneState,
    @SerialName("due_date") val dueDate: Date? = null,
    @SerialName("start_date") val startDate: Date? = null,
    @SerialName("created_at") val createdAt: Time? = null,
    @SerialName("title") val title: String? = null,
    @SerialName("updated_at") val updatedAt: Time? = null,
    @SerialName("web_url") val webUrl: String? = null
)

@Serializable
enum class MilestoneState(private val jsonName: String) {
    @SerialName("active")
    ACTIVE("active"),
    @SerialName("closed")
    CLOSED("closed");

    override fun toString() = jsonName
}

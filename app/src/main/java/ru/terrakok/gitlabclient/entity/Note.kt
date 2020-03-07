@file:UseSerializers(ZonedDateTimeDeserializer::class)
package ru.terrakok.gitlabclient.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import org.threeten.bp.ZonedDateTime
import ru.terrakok.gitlabclient.model.data.server.deserializer.ZonedDateTimeDeserializer

@Serializable
data class Note(
    @SerialName("id") val id: Long,
    @SerialName("body") val body: String,
    @SerialName("author") val author: ShortUser,
    @SerialName("created_at") val createdAt: ZonedDateTime,
    @SerialName("updated_at") val updatedAt: ZonedDateTime? = null,
    @SerialName("system") val isSystem: Boolean,
    @SerialName("noteable_id") val noteableId: Long? = null,
    @SerialName("noteable_type") val noteableType: EventTargetType? = null,
    @SerialName("noteable_iid") val noteableIid: Long? = null
)

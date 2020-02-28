package ru.terrakok.gitlabclient.entity

import com.google.gson.annotations.SerializedName
import org.threeten.bp.ZonedDateTime
import ru.terrakok.gitlabclient.entity.event.EventTargetType

data class Note(
    @SerializedName("id") val id: Long,
    @SerializedName("body") val body: String,
    @SerializedName("author") val author: ShortUser,
    @SerializedName("created_at") val createdAt: ZonedDateTime,
    @SerializedName("updated_at") val updatedAt: ZonedDateTime?,
    @SerializedName("system") val isSystem: Boolean,
    @SerializedName("noteable_id") val noteableId: Long,
    @SerializedName("noteable_type") val noteableType: EventTargetType?,
    @SerializedName("noteable_iid") val noteableIid: Long
)

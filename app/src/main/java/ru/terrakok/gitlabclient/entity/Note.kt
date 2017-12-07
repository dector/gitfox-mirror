package ru.terrakok.gitlabclient.entity

import com.google.gson.annotations.SerializedName
import ru.terrakok.gitlabclient.entity.event.EventTargetType
import java.util.*

data class Note(
        @SerializedName("id") val id: Long,
        @SerializedName("body") val body: String,
        @SerializedName("author") val author: Author,
        @SerializedName("created_at") val createdAt: Date,
        @SerializedName("updated_at") val updatedAt: Date?,
        @SerializedName("system") val isSystem: Boolean,
        @SerializedName("noteable_id") val noteableId: Long,
        @SerializedName("noteable_type") val noteableType: EventTargetType,
        @SerializedName("noteable_iid") val noteableIid: Long
)
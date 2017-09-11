package ru.terrakok.gitlabclient.entity.event

import com.google.gson.annotations.SerializedName
import ru.terrakok.gitlabclient.entity.Author
import java.util.*


/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 22.07.17.
 */
data class Event(
        @SerializedName("title") val title: String?,
        @SerializedName("project_id") val projectId: Long,
        @SerializedName("action_name") val actionName: String,
        @SerializedName("target_id") val targetId: Long?,
        @SerializedName("target_type") val targetType: EventTargetType?,
        @SerializedName("author_id") val authorId: Long,
        @SerializedName("data") val data: Any?,
        @SerializedName("target_title") val targetTitle: String?,
        @SerializedName("created_at") val createdAt: Date?,
        @SerializedName("author") val author: Author?,
        @SerializedName("author_username") val authorUsername: String?
)

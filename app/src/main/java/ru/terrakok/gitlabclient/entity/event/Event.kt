package ru.terrakok.gitlabclient.entity.event

import com.google.gson.annotations.SerializedName
import org.threeten.bp.ZonedDateTime
import ru.terrakok.gitlabclient.entity.Note
import ru.terrakok.gitlabclient.entity.PushData
import ru.terrakok.gitlabclient.entity.ShortUser

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 22.07.17.
 */
data class Event(
    @SerializedName("project_id") val projectId: Long,
    @SerializedName("action_name") val actionName: EventAction,
    @SerializedName("target_id") val targetId: Long?,
    @SerializedName("target_iid") val targetIid: Long?,
    @SerializedName("target_type") val targetType: EventTargetType?,
    @SerializedName("author_id") val authorId: Long,
    @SerializedName("target_title") val targetTitle: String?,
    @SerializedName("created_at") val createdAt: ZonedDateTime,
    @SerializedName("author") val author: ShortUser,
    @SerializedName("author_username") val authorUsername: String,
    @SerializedName("push_data") val pushData: PushData?,
    @SerializedName("note") val note: Note?
)

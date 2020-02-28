package ru.terrakok.gitlabclient.entity

import com.google.gson.annotations.SerializedName
import org.threeten.bp.ZonedDateTime

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 20.10.18.
 */
data class Commit(
    @SerializedName("id") val id: String,
    @SerializedName("short_id") val shortId: String,
    @SerializedName("title") val title: String,
    @SerializedName("author_name") val authorName: String,
    @SerializedName("author_email") val authorEmail: String?,
    @SerializedName("authored_date") val authoredDate: ZonedDateTime,
    @SerializedName("commiter_name") val commiterName: String?,
    @SerializedName("commiter_email") val commiterEmail: String?,
    @SerializedName("commited_date") val commitedDate: ZonedDateTime?,
    @SerializedName("created_at") val createdAt: ZonedDateTime,
    @SerializedName("message") val message: String,
    @SerializedName("parent_ids") val parentIds: List<String>
)

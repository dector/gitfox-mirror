package ru.terrakok.gitlabclient.entity

import com.google.gson.annotations.SerializedName

/**
 * @author Alexei Korshun on 09/11/2017.
 */
class Commit(
        @SerializedName("id") val id: String,
        @SerializedName("short_id") val shortId: String,
        @SerializedName("title") val title: String,
        @SerializedName("author_name") val authorName: String,
        @SerializedName("author_email") val authorEmail: String?,
        @SerializedName("created_at") val createdAt: String,
        @SerializedName("message") val message: String?
)
package ru.terrakok.gitlabclient.entity

import com.google.gson.annotations.SerializedName

/**
 * @author Alexei Korshun on 09/11/2017.
 */
data class Note(
        @SerializedName("id") var id: Int,
        @SerializedName("body") var body: String,
        @SerializedName("author") var author: Author,
        @SerializedName("created_at") var createdAt: String,
        @SerializedName("updated_at") var updatedAt: String,
        @SerializedName("system") var system: Boolean,
        @SerializedName("noteable_id") var noteableId: Int,
        @SerializedName("noteable_type") var noteableType: String,
        @SerializedName("noteable_iid") var noteableIid: Int
)
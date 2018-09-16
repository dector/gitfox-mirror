package ru.terrakok.gitlabclient.entity

import com.google.gson.annotations.SerializedName
import org.threeten.bp.LocalDateTime

data class Milestone(
    @SerializedName("id") val id: Long,
    @SerializedName("iid") val iid: Long,
    @SerializedName("project_id") val projectId: Long,
    @SerializedName("description") val description: String?,
    @SerializedName("state") val state: String?,
    @SerializedName("due_date") val dueDate: String?,
    @SerializedName("created_at") val createdAt: LocalDateTime?,
    @SerializedName("title") val title: String?,
    @SerializedName("updated_at") val updatedAt: String?
)

package ru.terrakok.gitlabclient.entity

import com.google.gson.annotations.SerializedName
import org.threeten.bp.LocalDate
import org.threeten.bp.ZonedDateTime

data class Milestone(
    @SerializedName("id") val id: Long,
    @SerializedName("iid") val iid: Long,
    @SerializedName("project_id") val projectId: Long,
    @SerializedName("description") val description: String?,
    @SerializedName("state") val state: MilestoneState,
    @SerializedName("due_date") val dueDate: LocalDate?,
    @SerializedName("start_date") val startDate: LocalDate?,
    @SerializedName("created_at") val createdAt: ZonedDateTime?,
    @SerializedName("title") val title: String?,
    @SerializedName("updated_at") val updatedAt: ZonedDateTime?,
    @SerializedName("web_url") val webUrl: String?
)

enum class MilestoneState(private val jsonName: String) {
    @SerializedName("active")
    ACTIVE("active"),
    @SerializedName("closed")
    CLOSED("closed");

    override fun toString() = jsonName
}

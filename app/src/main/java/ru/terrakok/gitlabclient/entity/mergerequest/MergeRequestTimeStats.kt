package ru.terrakok.gitlabclient.entity.mergerequest

import com.google.gson.annotations.SerializedName

data class MergeRequestTimeStats(
        @SerializedName("time_estimate") val timeEstimate: Int,
        @SerializedName("total_time_spent") val totalTimeSpent: Int,
        @SerializedName("human_time_estimate") val humanTimeEstimate: String?,
        @SerializedName("human_total_time_spent") val humanTotalTimeSpent: String?
)
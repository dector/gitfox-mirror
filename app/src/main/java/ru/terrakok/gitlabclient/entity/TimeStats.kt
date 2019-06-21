package ru.terrakok.gitlabclient.entity

import com.google.gson.annotations.SerializedName

/**
 * @author Eugene Shapovalov (CraggyHaggy). Date: 18.09.17
 */
data class TimeStats(
    @SerializedName("time_estimate") val timeEstimate: Int,
    @SerializedName("total_time_spent") val totalTimeSpent: Int,
    @SerializedName("human_time_estimate") val humanTimeEstimate: String?,
    @SerializedName("human_total_time_spent") val humanTotalTimeSpent: String?
)
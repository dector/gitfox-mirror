package ru.terrakok.gitlabclient.entity

import com.google.gson.annotations.SerializedName

data class TaskCompletionStatus(
    @SerializedName("count") val count: Int,
    @SerializedName("completed_count") val completedCount: Int
)
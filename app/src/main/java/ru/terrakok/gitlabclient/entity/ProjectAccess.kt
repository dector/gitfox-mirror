package ru.terrakok.gitlabclient.entity

import com.google.gson.annotations.SerializedName

data class ProjectAccess(
        @SerializedName("access_level")
        val accessLevel: Long,
        @SerializedName("notification_level")
        val notificationLevel: Long
)
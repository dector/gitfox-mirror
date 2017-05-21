package ru.terrakok.gitlabclient.entity.common

import com.google.gson.annotations.SerializedName

data class GroupAccess(
        @SerializedName("access_level") val accessLevel: Long,
        @SerializedName("notification_level") val notificationLevel: Long
)

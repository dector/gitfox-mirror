package ru.terrakok.gitlabclient.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProjectAccess(
    @SerialName("access_level") val accessLevel: Long,
    @SerialName("notification_level") val notificationLevel: Long
)

package ru.terrakok.gitlabclient.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SharedWithGroup(
    @SerialName("group_id") val groupId: Long,
    @SerialName("group_name") val groupName: String? = null,
    @SerialName("group_access_level") val groupAccessLevel: Long
)

package ru.terrakok.gitlabclient.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Permissions(
    @SerialName("project_access") val projectAccess: ProjectAccess? = null,
    @SerialName("group_access") val groupAccess: GroupAccess? = null
)

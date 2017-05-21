package ru.terrakok.gitlabclient.entity.common

import com.google.gson.annotations.SerializedName

data class Permissions(
        @SerializedName("project_access") val projectAccess: ProjectAccess?,
        @SerializedName("group_access") val groupAccess: GroupAccess?
)

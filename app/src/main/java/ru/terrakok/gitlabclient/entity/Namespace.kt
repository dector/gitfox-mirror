package ru.terrakok.gitlabclient.entity

import com.google.gson.annotations.SerializedName

data class Namespace(
        @SerializedName("id") val id: Long,
        @SerializedName("name") val name: String?,
        @SerializedName("path") val path: String?,
        @SerializedName("kind") val kind: String?,
        @SerializedName("full_path") val fullPath: String?
)

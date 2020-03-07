package ru.terrakok.gitlabclient.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Namespace(
    @SerialName("id") val id: Long,
    @SerialName("name") val name: String? = null,
    @SerialName("path") val path: String? = null,
    @SerialName("kind") val kind: String? = null,
    @SerialName("full_path") val fullPath: String? = null
)

package ru.terrakok.gitlabclient.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Identity(
    @SerialName("provider") val provider: String,
    @SerialName("extern_uid") val externUid: String
)

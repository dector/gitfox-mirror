package ru.terrakok.gitlabclient.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * @author Eugene Shapovalov (CraggyHaggy). Date: 11.09.17
 */
@Serializable
data class Links(
    @SerialName("self") val self: String? = null,
    @SerialName("notes") val notes: String? = null,
    @SerialName("award_emoji") val awardEmoji: String? = null,
    @SerialName("project") val project: String? = null
)

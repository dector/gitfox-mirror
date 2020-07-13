package gitfox.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Branch(
    @SerialName("name") val name: String,
    @SerialName("merged") val merged: Boolean,
    @SerialName("protected") val protected: Boolean,
    @SerialName("default") val default: Boolean,
    @SerialName("developers_can_push") val developersCanPush: Boolean,
    @SerialName("developers_can_merge") val developersCanMerge: Boolean,
    @SerialName("can_push") val canPush: Boolean
)

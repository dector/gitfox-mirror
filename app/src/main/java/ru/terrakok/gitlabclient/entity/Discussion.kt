package ru.terrakok.gitlabclient.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Discussion(
    @SerialName("id") val id: String,
    @SerialName("individual_note") val isIndividualNote: Boolean,
    @SerialName("notes") val notes: List<Note>
)

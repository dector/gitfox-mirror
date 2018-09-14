package ru.terrakok.gitlabclient.entity

import com.google.gson.annotations.SerializedName

data class Discussion(
    @SerializedName("id") val id: String,
    @SerializedName("individual_note") val isIndividualNote: Boolean,
    @SerializedName("notes") val notes: List<Note>
)
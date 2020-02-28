package ru.terrakok.gitlabclient.entity

import com.google.gson.annotations.SerializedName

data class Branch(
    @SerializedName("name") val name: String,
    @SerializedName("merged") val merged: Boolean,
    @SerializedName("protected") val protected: Boolean,
    @SerializedName("default") val default: Boolean,
    @SerializedName("developers_can_push") val developersCanPush: Boolean,
    @SerializedName("developers_can_merge") val developersCanMerge: Boolean,
    @SerializedName("can_push") val canPush: Boolean
)

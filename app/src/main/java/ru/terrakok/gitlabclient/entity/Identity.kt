package ru.terrakok.gitlabclient.entity

import com.google.gson.annotations.SerializedName

data class Identity(
    @SerializedName("provider") val provider: String,
    @SerializedName("extern_uid") val externUid: String
)

package ru.terrakok.gitlabclient.entity.common

import com.google.gson.annotations.SerializedName
import java.util.*

data class Owner (
        @SerializedName("id") val id: Long,
        @SerializedName("name") val name: String?,
        @SerializedName("created_at") val createdAt: Date?
)

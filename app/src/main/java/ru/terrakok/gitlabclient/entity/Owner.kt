package ru.terrakok.gitlabclient.entity

import com.google.gson.annotations.SerializedName
import org.threeten.bp.LocalDateTime

data class Owner (
        @SerializedName("id") val id: Long,
        @SerializedName("name") val name: String,
        @SerializedName("username") val username: String,
        @SerializedName("created_at") val createdAt: LocalDateTime?
)

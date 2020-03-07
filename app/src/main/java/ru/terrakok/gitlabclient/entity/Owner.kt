@file:UseSerializers(ZonedDateTimeDeserializer::class)
package ru.terrakok.gitlabclient.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import org.threeten.bp.ZonedDateTime
import ru.terrakok.gitlabclient.model.data.server.deserializer.ZonedDateTimeDeserializer

@Serializable
data class Owner(
    @SerialName("id") val id: Long,
    @SerialName("name") val name: String,
    @SerialName("username") val username: String,
    @SerialName("created_at") val createdAt: ZonedDateTime? = null
)

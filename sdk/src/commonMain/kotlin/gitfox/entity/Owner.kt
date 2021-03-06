@file:UseSerializers(TimeDeserializer::class)
package gitfox.entity

import gitfox.model.data.server.deserializer.TimeDeserializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers

@Serializable
data class Owner(
    @SerialName("id") val id: Long,
    @SerialName("name") val name: String,
    @SerialName("username") val username: String,
    @SerialName("created_at") val createdAt: Time? = null
)

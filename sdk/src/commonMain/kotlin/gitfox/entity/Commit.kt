@file:UseSerializers(TimeDeserializer::class)
package gitfox.entity

import gitfox.model.data.server.deserializer.TimeDeserializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 20.10.18.
 */
@Serializable
data class Commit(
    @SerialName("id") val id: String,
    @SerialName("short_id") val shortId: String,
    @SerialName("title") val title: String,
    @SerialName("author_name") val authorName: String,
    @SerialName("author_email") val authorEmail: String? = null,
    @SerialName("authored_date") val authoredDate: Time,
    @SerialName("commiter_name") val commiterName: String? = null,
    @SerialName("commiter_email") val commiterEmail: String? = null,
    @SerialName("commited_date") val commitedDate: Time? = null,
    @SerialName("created_at") val createdAt: Time,
    @SerialName("message") val message: String,
    @SerialName("parent_ids") val parentIds: List<String>
)

@file:UseSerializers(ColorDeserializer::class)
package gitfox.entity

import gitfox.model.data.server.deserializer.ColorDeserializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers

/**
 * @author Maxim Myalkin (MaxMyalkin) on 29.10.2018.
 */
@Serializable
data class Label(
    @SerialName("id") val id: Long,
    @SerialName("name") val name: String,
    @SerialName("color") val color: Color,
    @SerialName("description") val description: String? = null,
    @SerialName("open_issues_count") val openIssuesCount: Int = 0,
    @SerialName("closed_issues_count") val closedIssuesCount: Int = 0,
    @SerialName("open_merge_requests_count") val openMergeRequestsCount: Int = 0,
    @SerialName("subscribed") val subscribed: Boolean,
    @SerialName("priority") val priority: Int? = null
)

data class Color(
    val name: String
)

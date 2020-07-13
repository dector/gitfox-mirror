package gitfox.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 19.11.17.
 */
@Serializable
data class PushData(
    @SerialName("commit_count") val commitCount: Int,
    @SerialName("action") val action: PushDataAction,
    @SerialName("ref_type") val refType: PushDataRefType,
    @SerialName("commit_from") val commitFrom: String? = null,
    @SerialName("commit_to") val commitTo: String? = null,
    @SerialName("ref") val ref: String? = null,
    @SerialName("commit_title") val commitTitle: String? = null
)

@Serializable
enum class PushDataAction(private val jsonName: String) {
    @SerialName("pushed")
    PUSHED("pushed"),
    @SerialName("removed")
    REMOVED("removed"),
    @SerialName("created")
    CREATED("created");

    override fun toString() = jsonName
}

@Serializable
enum class PushDataRefType(private val jsonName: String) {
    @SerialName("branch")
    BRANCH("branch"),
    @SerialName("tag")
    TAG("tag");

    override fun toString() = jsonName
}

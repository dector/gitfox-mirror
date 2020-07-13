package gitfox.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * @author Eugene Shapovalov (CraggyHaggy). Date: 18.09.17
 */
@Serializable
data class TimeStats(
    @SerialName("time_estimate") val timeEstimate: Int,
    @SerialName("total_time_spent") val totalTimeSpent: Int,
    @SerialName("human_time_estimate") val humanTimeEstimate: String? = null,
    @SerialName("human_total_time_spent") val humanTotalTimeSpent: String? = null
)

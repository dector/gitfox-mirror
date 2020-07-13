package gitfox.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 30.03.17
 */
@Serializable
enum class Sort(private val jsonName: String) {
    @SerialName("asc")
    ASC("asc"),
    @SerialName("desc")
    DESC("desc");

    override fun toString() = jsonName
}

package gitfox.entity.app.develop

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 03.12.17.
 */
@Serializable
data class AppLibrary(
    @SerialName("name") val name: String,
    @SerialName("url") val url: String,
    @SerialName("license") val license: LicenseType
)

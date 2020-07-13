package gitfox.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 26.10.18.
 */
@Serializable
data class DiffData(
    @SerialName("old_path") val oldPath: String,
    @SerialName("new_path") val newPath: String,
    @SerialName("a_mode") val aMode: String,
    @SerialName("b_mode") val bMode: String,
    @SerialName("new_file") val newFile: Boolean,
    @SerialName("renamed_file") val renamedFile: Boolean,
    @SerialName("deleted_file") val deletedFile: Boolean,
    @SerialName("diff") val diff: String
)

package ru.terrakok.gitlabclient.entity.mergerequest

import com.google.gson.annotations.SerializedName

/**
 * @author Alexei Korshun on 09/11/2017.
 */
data class MergeRequestChange(
        @SerializedName("old_path") val oldPath: String,
        @SerializedName("new_path") val newPath: String,
        @SerializedName("a_mode") val aMode: String,
        @SerializedName("b_mode") val bMode: String,
        @SerializedName("diff") val diff: String,
        @SerializedName("new_file") val newFile: Boolean,
        @SerializedName("renamed_file") val renamedFile: Boolean,
        @SerializedName("delete_file") val deletedFile: Boolean
)
package ru.terrakok.gitlabclient.entity.mergerequest

import com.google.gson.annotations.SerializedName

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 26.10.18.
 */
data class MergeRequestChange(
    @SerializedName("old_path") val oldPath: String,
    @SerializedName("new_path") val newPath: String,
    @SerializedName("a_mode") val aMode: String,
    @SerializedName("b_mode") val bMode: String,
    @SerializedName("new_file") val newFile: Boolean,
    @SerializedName("renamed_file") val renamedFile: Boolean,
    @SerializedName("deleted_file") val deletedFile: Boolean,
    @SerializedName("diff") val diff: String
)
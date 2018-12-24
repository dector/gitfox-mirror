package ru.terrakok.gitlabclient.entity.commit

import com.google.gson.annotations.SerializedName

data class CommitDiff(
    @SerializedName("old_path") val oldPath: String,
    @SerializedName("new_path") val newPath: String,
    @SerializedName("a_mode") val aMode: String,
    @SerializedName("b_mode") val bMode: String,
    @SerializedName("new_file") val newFile: Boolean,
    @SerializedName("renamed_file") val renamedFile: Boolean,
    @SerializedName("deleted_file") val deletedFile: Boolean,
    @SerializedName("diff") val diff: String
)
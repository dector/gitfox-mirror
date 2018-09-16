package ru.terrakok.gitlabclient.entity

import com.google.gson.annotations.SerializedName

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 27.04.17.
 */
data class File(
    @SerializedName("file_name") val name: String,
    @SerializedName("file_path") val path: String,
    @SerializedName("size") val size: Long,
    @SerializedName("encoding") val encoding: String,
    @SerializedName("content") val content: String,
    @SerializedName("ref") val branch: String,
    @SerializedName("blob_id") val blobId: String,
    @SerializedName("commit_id") val commitId: String,
    @SerializedName("last_commit_id") val lastCommitId: String
)
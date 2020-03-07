package ru.terrakok.gitlabclient.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 27.04.17.
 */
@Serializable
data class File(
    @SerialName("file_name") val name: String,
    @SerialName("file_path") val path: String,
    @SerialName("size") val size: Long,
    @SerialName("encoding") val encoding: String,
    @SerialName("content") val content: String,
    @SerialName("ref") val branch: String,
    @SerialName("blob_id") val blobId: String,
    @SerialName("commit_id") val commitId: String,
    @SerialName("last_commit_id") val lastCommitId: String
)

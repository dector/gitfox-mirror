package ru.terrakok.gitlabclient.entity.target.mergerequest

import com.google.gson.annotations.SerializedName

/**
 * @author Eugene Shapovalov (CraggyHaggy). Date: 18.09.17
 */
enum class MergeRequestMergeStatus(private val jsonName: String) {
    @SerializedName("can_be_merged")
    CAN_BE_MERGED("can_be_merged"),
    @SerializedName("cannot_be_merged")
    CANNOT_BE_MERGED("cannot_be_merged"),
    @SerializedName("unchecked")
    UNCHECKED("unchecked");

    override fun toString() = jsonName
}

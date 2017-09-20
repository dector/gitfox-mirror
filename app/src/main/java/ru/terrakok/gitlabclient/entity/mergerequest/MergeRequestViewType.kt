package ru.terrakok.gitlabclient.entity.mergerequest

import com.google.gson.annotations.SerializedName

enum class MergeRequestViewType(val jsonName: String) {
    @SerializedName("simple") SIMPLE("simple");

    override fun toString() = jsonName
}
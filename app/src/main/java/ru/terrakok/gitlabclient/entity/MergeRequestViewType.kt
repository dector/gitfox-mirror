package ru.terrakok.gitlabclient.entity

import com.google.gson.annotations.SerializedName

enum class MergeRequestViewType(val jsonName: String) {
    @SerializedName("simple") SIMPLE("simple");

    override fun toString() = jsonName
}
package ru.terrakok.gitlabclient.entity.app

data class AccountMainBadges(
    val issueCount: Int,
    val mergeRequestCount: Int,
    val todoCount: Int
)
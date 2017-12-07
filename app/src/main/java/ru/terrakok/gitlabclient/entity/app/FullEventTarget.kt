package ru.terrakok.gitlabclient.entity.app

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 19.11.17.
 */
enum class FullEventTarget {
    ISSUE,
    MERGE_REQUEST,
    BRANCH,
    PROJECT,
    SNIPPET,
    MILESTONE
}
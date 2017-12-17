package ru.terrakok.gitlabclient.entity.app.event

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 19.11.17.
 */
enum class AppEventTarget {
    ISSUE,
    MERGE_REQUEST,
    BRANCH,
    PROJECT,
    SNIPPET,
    MILESTONE,
    COMMIT
}
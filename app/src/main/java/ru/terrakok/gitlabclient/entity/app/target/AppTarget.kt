package ru.terrakok.gitlabclient.entity.app.target

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 19.11.17.
 */
enum class AppTarget(private val str: String) {
    USER("user"),
    ISSUE("issue"),
    MERGE_REQUEST("merge request"),
    BRANCH("branch"),
    TAG("tag"),
    PROJECT("project"),
    SNIPPET("snippet"),
    MILESTONE("milestone"),
    COMMIT("commit");

    override fun toString() = str
}
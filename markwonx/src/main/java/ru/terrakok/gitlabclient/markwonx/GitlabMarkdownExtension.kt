package ru.terrakok.gitlabclient.markwonx;

enum class GitlabMarkdownExtension {
    LABEL;

    companion object {
        fun byString(value: String): GitlabMarkdownExtension? = values().firstOrNull { it.toString() == value }
    }
}
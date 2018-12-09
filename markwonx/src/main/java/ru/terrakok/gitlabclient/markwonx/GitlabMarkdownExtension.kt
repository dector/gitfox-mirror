package ru.terrakok.gitlabclient.markwonx;

enum class GitlabMarkdownExtension {
    LABEL,
    MILESTONE;

    companion object {
        fun byString(value: String): GitlabMarkdownExtension? = values().firstOrNull { it.toString() == value }
    }
}
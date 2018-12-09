package ru.terrakok.gitlabclient.markwonx.milestone;

import ru.terrakok.gitlabclient.markwonx.GitlabExtensionsDelimiterProcessor.Companion.DELIMITER_NEGATIVE_MATCH

enum class MilestoneType(
    val regex: Regex
) {
    SINGLE("$DELIMITER_NEGATIVE_MATCH%([a-zA-Z]+)".toRegex()),
    MULTIPLE("$DELIMITER_NEGATIVE_MATCH%\"(.*?)\"".toRegex()),
    ID("$DELIMITER_NEGATIVE_MATCH%(\\d+)".toRegex());

    companion object {
        fun byString(value: String): MilestoneType? = MilestoneType.values().firstOrNull { it.toString() == value }
    }
}
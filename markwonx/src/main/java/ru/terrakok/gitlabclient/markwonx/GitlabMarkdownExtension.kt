package ru.terrakok.gitlabclient.markwonx;

import ru.terrakok.gitlabclient.markwonx.label.LabelType
import ru.terrakok.gitlabclient.markwonx.milestone.MilestoneType

enum class GitlabMarkdownExtension(val regexList: List<ExtensionDataHolder>) {
    LABEL(LabelType.sortedValues().map { type -> ExtensionDataHolder(type.regex, type.name) }),
    MILESTONE(MilestoneType.sortedValues().map { type -> ExtensionDataHolder(type.regex, type.name) });

    companion object {

        const val OPTS_DELIMITER = '\u2600'

        fun byString(value: String): GitlabMarkdownExtension? = values().firstOrNull { it.toString() == value }
    }
}


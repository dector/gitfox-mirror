package ru.terrakok.gitlabclient.markwonx;

import ru.terrakok.gitlabclient.markwonx.label.LabelType

enum class GitlabMarkdownExtension(regexListFactory: () -> List<ExtensionDataHolder>) {
    LABEL({ LabelType.sortedValues().map { type -> ExtensionDataHolder(type.regex, type.name) } });

    val regexList by lazy(regexListFactory)

    companion object {

        const val EXTENSION_OPTIONS_DELIMITER = '\u2600'

        fun byString(value: String): GitlabMarkdownExtension? = values().firstOrNull { it.toString() == value }
    }
}


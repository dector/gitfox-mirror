package ru.terrakok.gitlabclient.markwonx.milestone

import ru.terrakok.gitlabclient.markwonx.GitlabExtensionsDelimiterProcessor
import ru.terrakok.gitlabclient.markwonx.GitlabMarkdownExtension
import ru.terrakok.gitlabclient.markwonx.MarkdownDecorator

class MilestoneDecorator : MarkdownDecorator {

    override fun decorate(markdown: String): String = MilestoneType
        .values()
        .fold(markdown) { acc, type ->
            type.regex.replace(acc) { matchResult ->
                val value = matchResult.groupValues[2]
                getDecoratedString(type, value)
            }
        }

    companion object {
        fun getDecoratedString(type: MilestoneType, value: String) = GitlabExtensionsDelimiterProcessor.decorate(
            "${GitlabMarkdownExtension.MILESTONE}_${type.name}_$value"
        )
    }
}
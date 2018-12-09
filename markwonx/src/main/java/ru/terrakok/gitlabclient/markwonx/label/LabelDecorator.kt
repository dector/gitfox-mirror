package ru.terrakok.gitlabclient.markwonx.label

import ru.terrakok.gitlabclient.markwonx.GitlabExtensionsDelimiterProcessor
import ru.terrakok.gitlabclient.markwonx.GitlabMarkdownExtension
import ru.terrakok.gitlabclient.markwonx.MarkdownDecorator

class LabelDecorator(
    val labelDescriptions: List<LabelDescription>
) : MarkdownDecorator {

    val supportedLabels by lazy {
        labelDescriptions.flatMap {
            listOf(it.id.toString(), it.name)
        }
    }

    override fun decorate(markdown: String): String = LabelType
        .values()
        .fold(markdown) { acc, type ->
            type.regex.replace(acc) { matchResult ->
                val value = matchResult.groupValues[1]
                if (supportedLabels.contains(value)) {
                    getDecoratedString(type, value)
                } else {
                    matchResult.groupValues[0]
                }
            }
        }

    companion object {
        fun getDecoratedString(type: LabelType, value: String) = GitlabExtensionsDelimiterProcessor.decorate(
            "${GitlabMarkdownExtension.LABEL}_${type.name}_$value"
        )
    }
}
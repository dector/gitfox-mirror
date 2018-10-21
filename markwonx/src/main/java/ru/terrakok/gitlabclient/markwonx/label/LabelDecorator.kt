package ru.terrakok.gitlabclient.markwonx.label

import ru.terrakok.gitlabclient.markwonx.GitlabMarkdownExtension
import ru.terrakok.gitlabclient.markwonx.MarkdownDecorator

class LabelDecorator : MarkdownDecorator {
    override fun decorate(markdown: String): String = LabelType
        .values()
        .fold(markdown) { acc, type ->
            type.regex.replace(acc) { matchResult ->
                val value = matchResult.groupValues[1]
                "%%%%%${GitlabMarkdownExtension.LABEL}_${type.name}_$value%%%%%"
            }
        }
}
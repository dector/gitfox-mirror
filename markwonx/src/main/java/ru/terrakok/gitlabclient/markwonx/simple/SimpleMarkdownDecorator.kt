package ru.terrakok.gitlabclient.markwonx.simple

import ru.terrakok.gitlabclient.markwonx.GitlabExtensionsDelimiterProcessor
import ru.terrakok.gitlabclient.markwonx.GitlabMarkdownExtension
import ru.terrakok.gitlabclient.markwonx.MarkdownDecorator

class SimpleMarkdownDecorator : MarkdownDecorator {

    override fun decorate(markdown: String): String {

        return GitlabMarkdownExtension.values().fold(markdown) { acc, ext ->
            ext.regexList.fold(acc) { acc, type ->
                type.regex.replace(acc) { matchResult ->
                    val result = matchResult.groupValues[1]
                    val exactPrefix = if (type.prefix != null) "${type.prefix}${GitlabMarkdownExtension.OPTS_DELIMITER}" else ""
                    "${GitlabExtensionsDelimiterProcessor.DELIMITER_START}$ext${GitlabMarkdownExtension.OPTS_DELIMITER}$exactPrefix$result${GitlabExtensionsDelimiterProcessor.DELIMITER_END}"
                }
            }
        }
    }

}
package ru.terrakok.gitlabclient.markwonx.simple

import org.commonmark.node.CustomNode
import org.commonmark.node.Delimited
import ru.terrakok.gitlabclient.markwonx.GitlabExtensionsDelimiterProcessor
import ru.terrakok.gitlabclient.markwonx.GitlabMarkdownExtension

data class SimpleNode(
    val type: GitlabMarkdownExtension,
    val data: String
) : CustomNode(), Delimited {
    override fun getOpeningDelimiter(): String = GitlabExtensionsDelimiterProcessor.DELIMITER_START_STR

    override fun getClosingDelimiter(): String = GitlabExtensionsDelimiterProcessor.DELIMITER_END_STR

}
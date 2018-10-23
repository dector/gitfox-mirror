package ru.terrakok.gitlabclient.markwonx.label

import org.commonmark.node.CustomNode
import org.commonmark.node.Delimited
import ru.terrakok.gitlabclient.markwonx.GitlabExtensionsDelimiterProcessor

data class LabelNode(
    val label: String
) : CustomNode(), Delimited {
    override fun getOpeningDelimiter(): String = GitlabExtensionsDelimiterProcessor.DELIMITER_STRING

    override fun getClosingDelimiter(): String = GitlabExtensionsDelimiterProcessor.DELIMITER_STRING

}
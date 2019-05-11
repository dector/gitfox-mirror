package ru.terrakok.gitlabclient.markwonx.label

import org.commonmark.node.CustomNode
import ru.noties.markwon.SpannableBuilder
import ru.noties.markwon.SpannableConfiguration
import ru.noties.markwon.renderer.SpannableMarkdownVisitor
import ru.terrakok.gitlabclient.markwonx.GitlabMarkdownExtension

class SimpleVisitor(
    configuration: SpannableConfiguration,
    val builder: SpannableBuilder,
    val nodeVisitors: Map<GitlabMarkdownExtension, SimpleNodeVisitor>
) : SpannableMarkdownVisitor(configuration, builder) {

    override fun visit(customNode: CustomNode) {
        if (customNode is SimpleNode) {
            nodeVisitors[customNode.type]?.visit(customNode.data, builder)
        } else {
            super.visit(customNode)
        }
    }

}
package ru.terrakok.gitlabclient.markwonx.simple

import io.noties.markwon.MarkwonVisitor
import ru.terrakok.gitlabclient.markwonx.GitlabMarkdownExtension

class SimpleVisitor(
    private val nodeVisitors: Map<GitlabMarkdownExtension, SimpleNodeVisitor>
) : MarkwonVisitor.NodeVisitor<SimpleNode> {

    override fun visit(visitor: MarkwonVisitor, node: SimpleNode) {
        nodeVisitors[node.type]?.visit(visitor, node.data)
    }

}
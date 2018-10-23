package ru.terrakok.gitlabclient.markwonx

import org.commonmark.node.CustomNode
import org.commonmark.node.Visitor
import ru.noties.markwon.SpannableBuilder
import ru.noties.markwon.SpannableConfiguration
import ru.noties.markwon.renderer.SpannableMarkdownVisitor

class CompositeVisitor(
    configuration: SpannableConfiguration,
    builder: SpannableBuilder,
    private vararg val visitors: Visitor
) : SpannableMarkdownVisitor(configuration, builder) {

    override fun visit(customNode: CustomNode) {
        visitors.forEach { customNode.accept(it) }
    }

}
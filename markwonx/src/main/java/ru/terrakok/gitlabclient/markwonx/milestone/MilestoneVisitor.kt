package ru.terrakok.gitlabclient.markwonx.milestone

import org.commonmark.node.CustomNode
import ru.noties.markwon.SpannableBuilder
import ru.noties.markwon.SpannableConfiguration
import ru.noties.markwon.renderer.SpannableMarkdownVisitor

class MilestoneVisitor constructor(
    configuration: SpannableConfiguration,
    val builder: SpannableBuilder
) : SpannableMarkdownVisitor(configuration, builder) {

    override fun visit(customNode: CustomNode) {
        if (customNode is MilestoneNode) {
            val milestone = customNode.milestone
            val length = builder.length
            when {
                milestone.id != null -> builder.append(milestone.id.toString())
                milestone.name != null -> builder.append(milestone.name.toString())
            }
            builder.setSpan(MilestoneSpan(milestone), length)
        } else {
            super.visit(customNode)
        }
    }

}
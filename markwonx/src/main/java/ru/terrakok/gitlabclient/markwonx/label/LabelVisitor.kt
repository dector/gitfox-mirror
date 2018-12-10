package ru.terrakok.gitlabclient.markwonx.label

import android.graphics.Color
import org.commonmark.node.CustomNode
import ru.noties.markwon.SpannableBuilder
import ru.noties.markwon.SpannableConfiguration
import ru.noties.markwon.renderer.SpannableMarkdownVisitor
import ru.terrakok.gitlabclient.markwonx.GitlabMarkdownExtension
import ru.terrakok.gitlabclient.markwonx.MarkdownClickMediator

class LabelVisitor(
    configuration: SpannableConfiguration,
    val config: LabelSpanConfig,
    val builder: SpannableBuilder,
    val clickListener: MarkdownClickMediator
) : SpannableMarkdownVisitor(configuration, builder) {

    override fun visit(customNode: CustomNode) {
        if (customNode is LabelNode) {
            val label = customNode.label
            val length = builder.length
            val color = try {
                Color.parseColor(label.color)
            } catch (e: IllegalArgumentException) {
                null
            }
            if (color != null) {
                builder.append(label.name)
                builder.setSpan(LabelSpan(label, color, config) {
                    clickListener.markdownClicked(GitlabMarkdownExtension.LABEL, it)
                }, length)
            }
        } else {
            super.visit(customNode)
        }
    }

}
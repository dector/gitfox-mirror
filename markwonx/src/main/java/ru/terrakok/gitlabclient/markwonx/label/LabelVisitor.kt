package ru.terrakok.gitlabclient.markwonx.label

import android.graphics.Typeface
import android.text.style.StyleSpan
import org.commonmark.node.CustomNode
import ru.noties.markwon.SpannableBuilder
import ru.noties.markwon.SpannableConfiguration
import ru.noties.markwon.renderer.SpannableMarkdownVisitor


class LabelVisitor(
    configuration: SpannableConfiguration,
    val builder: SpannableBuilder
) : SpannableMarkdownVisitor(configuration, builder) {

    override fun visit(customNode: CustomNode) {
        if (customNode is LabelNode) {
            val label = customNode.label
            val length = builder.length
            builder.append(label)
            builder.setSpan(StyleSpan(Typeface.BOLD), length)
        } else {
            super.visit(customNode)
        }
    }

}
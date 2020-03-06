package ru.terrakok.gitlabclient.markwonx.label

import android.graphics.Color
import android.graphics.Typeface
import android.text.style.StyleSpan
import io.noties.markwon.MarkwonVisitor
import ru.terrakok.gitlabclient.markwonx.GitlabMarkdownExtension
import ru.terrakok.gitlabclient.markwonx.MarkdownClickHandler
import ru.terrakok.gitlabclient.markwonx.simple.SimpleNodeVisitor

class SimpleLabelVisitor(
    private val labels: List<LabelDescription>,
    private val config: LabelSpanConfig,
    private val clickHandler: MarkdownClickHandler
) : SimpleNodeVisitor {

    override fun visit(visitor: MarkwonVisitor, args: String) {
        val builder = visitor.builder()
        val labelType = args.substringBefore(GitlabMarkdownExtension.OPTS_DELIMITER).let {
            LabelType.byString(
                it
            )
        }
        val arg = args.substringAfter(GitlabMarkdownExtension.OPTS_DELIMITER)

        val label = when (labelType) {
            LabelType.ID -> {
                val id = arg.toLong()
                labels.firstOrNull { it.id == id }
            }
            else -> {
                labels.firstOrNull { it.name == arg }
            }
        }

        if (label != null) {
            val start = builder.length
            builder.append(label.name)
            visitor.setSpans(start, StyleSpan(Typeface.BOLD))

            val color = try {
                Color.parseColor(label.color)
            } catch (e: IllegalArgumentException) {
                null
            }
            if (color != null) {
                val span = LabelSpan(
                    label,
                    color,
                    config
                ) {
                    clickHandler(GitlabMarkdownExtension.LABEL, label)
                }
                visitor.setSpans(start, span)
            }

        } else {
            val content = when (labelType) {
                LabelType.MULTIPLE -> "~\"$arg\""
                else -> "~$arg"
            }
            builder.append(content)
        }
    }

}
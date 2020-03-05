package ru.terrakok.gitlabclient.markwonx.label

import android.graphics.Color
import android.graphics.Typeface
import android.text.style.StyleSpan
import ru.noties.markwon.SpannableBuilder
import ru.terrakok.gitlabclient.markwonx.GitlabMarkdownExtension
import ru.terrakok.gitlabclient.markwonx.MarkdownClickHandler

class SimpleLabelVisitor(
    private val labels: List<LabelDescription>,
    private val config: LabelSpanConfig,
    private val clickHandler: MarkdownClickHandler
) : SimpleNodeVisitor {

    override fun visit(args: String, builder: SpannableBuilder) {
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
            val length = builder.length
            builder.append(label.name)
            builder.setSpan(StyleSpan(Typeface.BOLD), length)

            val color = try {
                Color.parseColor(label.color)
            } catch (e: IllegalArgumentException) {
                null
            }
            if (color != null) {
                val span = LabelSpan(label, color, config) {
                    clickHandler(GitlabMarkdownExtension.LABEL, label)
                }
                builder.setSpan(span, length)
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
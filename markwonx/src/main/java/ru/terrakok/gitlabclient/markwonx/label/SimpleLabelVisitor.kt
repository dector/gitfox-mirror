package ru.terrakok.gitlabclient.markwonx.label

import android.graphics.Typeface
import android.text.style.StyleSpan
import ru.noties.markwon.SpannableBuilder
import ru.terrakok.gitlabclient.markwonx.GitlabMarkdownExtension

class SimpleLabelVisitor(
    private val labels: List<LabelDescription>
): SimpleNodeVisitor {

    override fun visit(args: String, builder: SpannableBuilder) {
        val labelType = args.substringBefore(GitlabMarkdownExtension.EXTENSION_OPTIONS_DELIMITER).let {
            LabelType.byString(
                it
            )
        }
        val arg = args.substringAfter(GitlabMarkdownExtension.EXTENSION_OPTIONS_DELIMITER)

        val label = when (labelType) {
            LabelType.ID -> {
                val id = arg.toLong()
                labels.firstOrNull { it.id == id }
            }
            LabelType.SINGLE -> {
                labels.firstOrNull { it.name == arg }
            }
            LabelType.MULTIPLE -> {
                labels.firstOrNull { it.name == arg }
            }
            else -> null
        }

        if (label != null) {
            val length = builder.length
            builder.append(label.name)
            builder.setSpan(StyleSpan(Typeface.BOLD), length)
        } else {
            builder.append(arg)
        }
    }

}
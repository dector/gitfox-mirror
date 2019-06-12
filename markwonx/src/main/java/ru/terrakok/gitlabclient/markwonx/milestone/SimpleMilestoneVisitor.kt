package ru.terrakok.gitlabclient.markwonx.milestone

import ru.noties.markwon.SpannableBuilder
import ru.terrakok.gitlabclient.markwonx.GitlabMarkdownExtension
import ru.terrakok.gitlabclient.markwonx.MarkdownClickMediator
import ru.terrakok.gitlabclient.markwonx.label.SimpleNodeVisitor

class SimpleMilestoneVisitor(
    private val milestones: List<MilestoneDescription>,
    private val clicksMediator: MarkdownClickMediator
) : SimpleNodeVisitor {

    override fun visit(args: String, builder: SpannableBuilder) {
        val milestoneType = args.substringBefore(GitlabMarkdownExtension.OPTS_DELIMITER).let {
            MilestoneType.byString(
                it
            )
        }
        val arg = args.substringAfter(GitlabMarkdownExtension.OPTS_DELIMITER)
        val milestone = when (milestoneType) {
            MilestoneType.ID -> {
                val id = arg.toLong()
                milestones.firstOrNull { it.id == id }
            }
            else -> {
                milestones.firstOrNull { it.name == arg }
            }
        }

        if (milestone != null) {
            val length = builder.length
            builder.append(milestone.name)

            val span = MilestoneSpan(milestone) {
                clicksMediator.markdownClicked(GitlabMarkdownExtension.MILESTONE, milestone)
            }

            builder.setSpan(span, length)
        } else {
            val content = when (milestoneType) {
                MilestoneType.MULTIPLE -> "%\"$arg\""
                else -> "%$arg"
            }
            builder.append(content)
        }
    }

}
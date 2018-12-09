package ru.terrakok.gitlabclient.markwonx.milestone

import org.commonmark.node.Node
import ru.terrakok.gitlabclient.markwonx.ExtensionProcessor

class MilestoneExtensionProcessor  : ExtensionProcessor {

    override fun process(args: String): Node? {
        val labelType = args.substringBefore('_').let {
            MilestoneType.byString(
                it
            )
        }
        val arg = args.substringAfter('_')
        val label = when (labelType) {
            MilestoneType.ID -> {
                val id = arg.toInt()
                MilestoneDescription(id = id)
            }
            MilestoneType.SINGLE -> {
                MilestoneDescription(name = arg)
            }
            MilestoneType.MULTIPLE -> {
                MilestoneDescription(name = arg)
            }
            else -> null
        }
        return label?.let { MilestoneNode(it) }
    }

}
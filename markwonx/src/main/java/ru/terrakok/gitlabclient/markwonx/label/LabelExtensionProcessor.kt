package ru.terrakok.gitlabclient.markwonx.label

import org.commonmark.node.Node
import ru.terrakok.gitlabclient.markwonx.ExtensionProcessor

class LabelExtensionProcessor(
    val labels: List<LabelDescription>
) : ExtensionProcessor {

    override fun process(args: String): Node? {
        val labelType = args.substringBefore('_').let {
            LabelType.byString(
                it
            )
        }
        val arg = args.substringAfter('_')
        val label = when (labelType) {
            LabelType.ID -> {
                val id = arg.toLong()
                labels.first { it.id == id }
            }
            LabelType.SINGLE -> {
                labels.first { it.name == arg }

            }
            LabelType.MULTIPLE -> {
                labels.first { it.name == arg }
            }
            else -> null
        }
        return label?.let { LabelNode(it) }
    }

}
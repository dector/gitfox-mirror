package ru.terrakok.gitlabclient.markwonx.label

import org.commonmark.node.Node
import ru.terrakok.gitlabclient.markwonx.ExtensionProcessor

class LabelExtensionProcessor : ExtensionProcessor {

    override fun process(args: String): Node? {
        val arg = args.substringAfter('_')
        return LabelNode(arg)
    }

}
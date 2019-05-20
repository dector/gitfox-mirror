package ru.terrakok.gitlabclient.markwonx.label

import org.commonmark.node.Node
import ru.terrakok.gitlabclient.markwonx.ExtensionProcessor
import ru.terrakok.gitlabclient.markwonx.GitlabMarkdownExtension

class SimpleExtensionProcessor : ExtensionProcessor {

    override fun process(extType: GitlabMarkdownExtension, args: String): Node? = SimpleNode(extType, args)

}
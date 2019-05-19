package ru.terrakok.gitlabclient.markwonx

import org.commonmark.node.Node

interface ExtensionProcessor {
    fun process(extType: GitlabMarkdownExtension, args: String): Node?
}
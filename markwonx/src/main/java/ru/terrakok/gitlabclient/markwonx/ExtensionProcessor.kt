package ru.terrakok.gitlabclient.markwonx

import org.commonmark.node.Node

interface ExtensionProcessor {
    fun process(args: String): Node?
}
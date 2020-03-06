package ru.terrakok.gitlabclient.markwonx.simple

import io.noties.markwon.MarkwonVisitor

interface SimpleNodeVisitor {
    fun visit(visitor: MarkwonVisitor, args: String)
}
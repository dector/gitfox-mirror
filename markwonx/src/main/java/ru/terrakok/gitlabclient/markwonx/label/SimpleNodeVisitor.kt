package ru.terrakok.gitlabclient.markwonx.label

import ru.noties.markwon.SpannableBuilder

interface SimpleNodeVisitor {
    fun visit(args: String, builder: SpannableBuilder)
}
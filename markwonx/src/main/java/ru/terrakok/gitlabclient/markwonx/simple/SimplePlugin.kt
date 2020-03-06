package ru.terrakok.gitlabclient.markwonx.simple

import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.MarkwonVisitor
import org.commonmark.parser.Parser
import ru.terrakok.gitlabclient.markwonx.GitlabExtensionsDelimiterProcessor
import ru.terrakok.gitlabclient.markwonx.GitlabMarkdownExtension

class SimplePlugin(
    private val nodeVisitors: Map<GitlabMarkdownExtension, SimpleNodeVisitor>
) : AbstractMarkwonPlugin() {

    private val decorator by lazy {
        SimpleMarkdownDecorator()
    }

    override fun processMarkdown(markdown: String): String {
        return decorator.decorate(markdown)
    }

    override fun configureParser(builder: Parser.Builder) {
        val processor = SimpleExtensionProcessor()
        builder.customDelimiterProcessor(
            GitlabExtensionsDelimiterProcessor(
                nodeVisitors.mapValues { processor }
            )
        )
    }

    override fun configureVisitor(builder: MarkwonVisitor.Builder) {
        builder.on(SimpleNode::class.java, SimpleVisitor(nodeVisitors))
    }

}
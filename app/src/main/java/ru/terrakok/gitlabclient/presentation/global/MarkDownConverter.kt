package ru.terrakok.gitlabclient.presentation.global

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.commonmark.node.Visitor
import org.commonmark.parser.Parser
import ru.noties.markwon.SpannableBuilder
import ru.terrakok.gitlabclient.markwonx.MarkdownDecorator

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 28.05.17
 */
class MarkDownConverter(
    private val parser: Parser,
    private val decorator: MarkdownDecorator,
    private val visitorFactory: suspend (projectId: Long?, builder: SpannableBuilder) -> Visitor
) {

    suspend fun toSpannable(raw: String?, projectId: Long?): CharSequence =
        withContext(Dispatchers.Default) {
            val builder = SpannableBuilder()
            val visitor = visitorFactory(projectId, builder)
            val decorated = decorator.decorate(raw ?: "")
            val node = parser.parse(decorated)
            node.accept(visitor)
            builder.text()
        }
}

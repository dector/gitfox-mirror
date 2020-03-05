package ru.terrakok.gitlabclient.presentation.global

import io.reactivex.Single
import org.commonmark.node.Visitor
import org.commonmark.parser.Parser
import ru.noties.markwon.SpannableBuilder
import ru.terrakok.gitlabclient.markwonx.MarkdownClickHandler
import ru.terrakok.gitlabclient.markwonx.MarkdownDecorator
import ru.terrakok.gitlabclient.model.system.SchedulersProvider

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 28.05.17
 */
class MarkDownConverter(
    private val parser: Parser,
    private val decorator: MarkdownDecorator,
    private val visitorFactory: (
        projectId: Long?,
        builder: SpannableBuilder,
        clickHandler: MarkdownClickHandler
    ) -> Single<Visitor>,
    private val schedulers: SchedulersProvider
) {

    fun markdownToSpannable(raw: String, projectId: Long?, markdownClickHandler: MarkdownClickHandler): Single<CharSequence> = Single.defer {
        val builder = SpannableBuilder()
        visitorFactory(projectId, builder, markdownClickHandler)
            .map { visitor ->
                val decorated = decorator.decorate(raw)
                val node = parser.parse(decorated)
                node.accept(visitor)
                builder.text()
            }
            .subscribeOn(schedulers.computation())
            .observeOn(schedulers.ui())
    }
}

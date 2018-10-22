package ru.terrakok.gitlabclient.presentation.global

import io.reactivex.Single
import org.commonmark.node.Visitor
import org.commonmark.parser.Parser
import ru.noties.markwon.SpannableBuilder
import ru.noties.markwon.SpannableConfiguration
import ru.terrakok.gitlabclient.markwonx.MarkdownDecorator
import ru.terrakok.gitlabclient.model.system.SchedulersProvider

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 28.05.17
 */
class MarkDownConverter constructor(
    private val config: SpannableConfiguration,
    private val parser: Parser,
    private val decorator: MarkdownDecorator,
    private val visitorFactory: (SpannableBuilder) -> Visitor,
    private val schedulers: SchedulersProvider
) {

    fun markdownToSpannable(raw: String): Single<CharSequence> =
        Single
            .fromCallable {
                val decorated = decorator.decorate(raw)
                val node = parser.parse(decorated)
                val builder = SpannableBuilder()
                val visitor = visitorFactory(builder)
                node.accept(visitor)
                builder.text()
            }
            .subscribeOn(schedulers.computation())
            .observeOn(schedulers.ui())
}
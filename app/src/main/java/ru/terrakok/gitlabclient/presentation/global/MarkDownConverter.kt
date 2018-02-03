package ru.terrakok.gitlabclient.presentation.global

import io.reactivex.Single
import org.commonmark.parser.Parser
import org.commonmark.renderer.html.HtmlRenderer
import ru.terrakok.gitlabclient.model.system.SchedulersProvider
import javax.inject.Inject

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 28.05.17
 */
class MarkDownConverter @Inject constructor(
        private val schedulers: SchedulersProvider
) {
    private val parser: Parser = Parser.builder().build()
    private val renderer: HtmlRenderer = HtmlRenderer.builder().build()

    fun markdownToHtml(raw: String) = Single
            .fromCallable {
                renderer.render(parser.parse(raw))
            }
            .subscribeOn(schedulers.computation())
            .observeOn(schedulers.ui())
}
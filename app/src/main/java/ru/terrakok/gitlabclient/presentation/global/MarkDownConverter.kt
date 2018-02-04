package ru.terrakok.gitlabclient.presentation.global

import io.reactivex.Single
import org.commonmark.parser.Parser
import org.commonmark.renderer.html.HtmlRenderer
import ru.noties.markwon.Markwon
import ru.noties.markwon.SpannableConfiguration
import ru.terrakok.gitlabclient.model.system.SchedulersProvider
import javax.inject.Inject

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 28.05.17
 */
class MarkDownConverter @Inject constructor(
        config: Config,
        private val schedulers: SchedulersProvider
) {
    data class Config(
            val parser: Parser,
            val htmlRenderer: HtmlRenderer,
            val spannableConfiguration: SpannableConfiguration
    )

    private val parser = config.parser
    private val renderer = config.htmlRenderer
    private val mdConfig = config.spannableConfiguration

    fun markdownToHtml(raw: String): Single<String> = Single
            .fromCallable {
                renderer.render(parser.parse(raw))
            }
            .subscribeOn(schedulers.computation())
            .observeOn(schedulers.ui())

    fun markdownToSpannable(raw: String): Single<CharSequence> = Single
            .fromCallable {
                Markwon.markdown(mdConfig, raw)
            }
            .subscribeOn(schedulers.computation())
            .observeOn(schedulers.ui())
}
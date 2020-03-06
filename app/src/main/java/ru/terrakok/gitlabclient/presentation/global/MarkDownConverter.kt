package ru.terrakok.gitlabclient.presentation.global

import android.text.Spanned
import io.noties.markwon.Markwon
import io.reactivex.Single
import ru.terrakok.gitlabclient.markwonx.MarkdownClickHandler
import ru.terrakok.gitlabclient.model.system.SchedulersProvider

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 28.05.17
 */
class MarkDownConverter(
    private val markwonFactory: (
        projectId: Long?,
        clickHandler: MarkdownClickHandler
    ) -> Single<Markwon>,
    private val schedulers: SchedulersProvider
) {

    fun markdownToSpannable(
        raw: String,
        projectId: Long?,
        markdownClickHandler: MarkdownClickHandler
    ): Single<Spanned> = Single.defer {
        markwonFactory(projectId, markdownClickHandler)
            .map { markwon -> markwon.toMarkdown(raw) }
            .subscribeOn(schedulers.computation())
            .observeOn(schedulers.ui())
    }
}

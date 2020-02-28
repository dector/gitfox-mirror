package ru.terrakok.gitlabclient.presentation.global

import io.reactivex.Single
import ru.noties.markwon.Markwon
import ru.noties.markwon.SpannableConfiguration
import ru.terrakok.gitlabclient.model.system.SchedulersProvider

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 28.05.17
 */
class MarkDownConverter(
    private val config: SpannableConfiguration,
    private val schedulers: SchedulersProvider
) {

    fun markdownToSpannable(raw: String): Single<CharSequence> =
        Single
            .fromCallable { Markwon.markdown(config, raw) }
            .subscribeOn(schedulers.computation())
            .observeOn(schedulers.ui())
}

package ru.terrakok.gitlabclient.presentation.global

import io.reactivex.Single
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.rx2.rxSingle
import kotlinx.coroutines.withContext
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
        rxSingle { toSpannable(raw) }
            .observeOn(schedulers.ui())

    suspend fun toSpannable(raw: String): CharSequence =
        withContext(Dispatchers.Default) {
            Markwon.markdown(config, raw)
        }
}

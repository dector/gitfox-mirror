package ru.terrakok.gitlabclient.presentation.global

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.noties.markwon.Markwon
import ru.noties.markwon.SpannableConfiguration

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 28.05.17
 */
class MarkDownConverter(
    private val config: SpannableConfiguration
) {

    suspend fun toSpannable(raw: String?): CharSequence =
        withContext(Dispatchers.Default) {
            raw?.let { Markwon.markdown(config, raw) } ?: ""
        }
}

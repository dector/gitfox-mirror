package ru.terrakok.gitlabclient.presentation.global

import android.text.SpannableString
import android.text.Spanned
import io.noties.markwon.Markwon
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.terrakok.gitlabclient.markwonx.MarkdownClickHandler

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 28.05.17
 */
class MarkDownConverter(
    private val markwonFactory: suspend (
        projectId: Long?,
        clickHandler: MarkdownClickHandler
    ) -> Markwon
) {

    suspend fun toSpannable(
        raw: String?,
        projectId: Long?,
        markdownClickHandler: MarkdownClickHandler
    ): Spanned =
        withContext(Dispatchers.Default) {
            if (raw != null) {
                val markwon = markwonFactory(projectId, markdownClickHandler)
                markwon.toMarkdown(raw)
            } else {
                SpannableString("")
            }
        }
}

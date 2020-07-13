package ru.terrakok.gitlabclient.presentation.markdown

import com.github.aakira.napier.Napier
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import moxy.InjectViewState
import ru.terrakok.gitlabclient.markwonx.GitlabMarkdownExtension
import ru.terrakok.gitlabclient.markwonx.label.LabelDescription
import ru.terrakok.gitlabclient.markwonx.milestone.MilestoneDescription
import ru.terrakok.gitlabclient.presentation.global.BasePresenter
import ru.terrakok.gitlabclient.presentation.global.ErrorHandler
import ru.terrakok.gitlabclient.presentation.global.MarkDownConverter
import javax.inject.Inject

@InjectViewState
class MarkdownPresenter @Inject constructor(
    private val mdConverter: MarkDownConverter,
    private val errorHandler: ErrorHandler
) : BasePresenter<MarkdownView>() {

    private var conversionJob: Job? = null

    fun setMarkdown(markdown: String, projectId: Long?) {
        conversionJob?.cancel()
        conversionJob = launch {
            try {
                val text = mdConverter.toSpannable(
                    raw = markdown,
                    projectId = projectId,
                    markdownClickHandler = { extension, value ->
                        viewState.markdownClicked(extension, value)
                    }
                )
                viewState.setMarkdownText(text)
            } catch (e: Exception) {
                errorHandler.proceed(e)
            }
        }
    }

    override fun detachView(view: MarkdownView?) {
        super.detachView(view)
        conversionJob?.cancel()
    }

    fun markdownClicked(extension: GitlabMarkdownExtension, value: Any) {
        when (extension) {
            GitlabMarkdownExtension.LABEL -> Napier.d("Label clicked: ${value as LabelDescription}")
            GitlabMarkdownExtension.MILESTONE -> Napier.d("Milestione clicked: ${value as MilestoneDescription}")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        conversionJob?.cancel()
    }
}
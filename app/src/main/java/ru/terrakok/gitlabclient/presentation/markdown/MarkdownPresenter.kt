package ru.terrakok.gitlabclient.presentation.markdown

import io.reactivex.disposables.Disposable
import moxy.InjectViewState
import ru.terrakok.gitlabclient.markwonx.GitlabMarkdownExtension
import ru.terrakok.gitlabclient.markwonx.label.LabelDescription
import ru.terrakok.gitlabclient.markwonx.milestone.MilestoneDescription
import ru.terrakok.gitlabclient.presentation.global.BasePresenter
import ru.terrakok.gitlabclient.presentation.global.ErrorHandler
import ru.terrakok.gitlabclient.presentation.global.MarkDownConverter
import timber.log.Timber
import javax.inject.Inject

@InjectViewState
class MarkdownPresenter @Inject constructor(
    private val mdConverter: MarkDownConverter,
    private val errorHandler: ErrorHandler
) : BasePresenter<MarkdownView>() {

    private var conversionDisposable: Disposable? = null

    fun setMarkdown(markdown: String, projectId: Long?) {
        conversionDisposable?.dispose()
        conversionDisposable = mdConverter
            .markdownToSpannable(
                raw = markdown,
                projectId = projectId,
                markdownClickHandler = { extension, value ->
                    viewState.markdownClicked(extension, value)
                }
            )
            .subscribe(
                { viewState.setMarkdownText(it) },
                { errorHandler.proceed(it) }
            )
    }

    override fun detachView(view: MarkdownView?) {
        super.detachView(view)
        conversionDisposable?.dispose()
    }

    fun markdownClicked(extension: GitlabMarkdownExtension, value: Any) {
        when (extension) {
            GitlabMarkdownExtension.LABEL -> Timber.d("Label clicked: ${value as LabelDescription}")
            GitlabMarkdownExtension.MILESTONE -> Timber.d("Milestione clicked: ${value as MilestoneDescription}")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        conversionDisposable?.dispose()
    }
}
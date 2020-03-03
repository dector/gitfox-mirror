package ru.terrakok.gitlabclient.presentation.markdown

import io.reactivex.disposables.Disposable
import moxy.InjectViewState
import ru.terrakok.gitlabclient.markwonx.GitlabMarkdownExtension
import ru.terrakok.gitlabclient.markwonx.MarkdownClickMediator
import ru.terrakok.gitlabclient.markwonx.label.LabelDescription
import ru.terrakok.gitlabclient.markwonx.milestone.MilestoneDescription
import ru.terrakok.gitlabclient.presentation.global.BasePresenter
import ru.terrakok.gitlabclient.presentation.global.ErrorHandler
import timber.log.Timber
import ru.terrakok.gitlabclient.presentation.global.MarkDownConverter
import javax.inject.Inject

@InjectViewState
class MarkdownPresenter @Inject constructor(
    private val mdConverter: MarkDownConverter,
    private val errorHandler: ErrorHandler,
    private val markdownClickMediator: MarkdownClickMediator
) : BasePresenter<MarkdownView>() {

    private var conversionDisposable: Disposable? = null

    fun setMarkdown(markdown: String, projectId: Long?) {
        conversionDisposable?.dispose()
        conversionDisposable = mdConverter
            .markdownToSpannable(markdown, projectId)
            .subscribe(
                { viewState.setMarkdownText(it) },
                { errorHandler.proceed(it) }
            )
    }

    override fun onFirstViewAttach() {
        markdownClickMediator
            .getClickEvents()
            .subscribe {
                viewState.markdownClicked(it.extension, it.value)
            }
            .connect()
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
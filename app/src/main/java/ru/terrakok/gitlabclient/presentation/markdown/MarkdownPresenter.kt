package ru.terrakok.gitlabclient.presentation.markdown

import com.arellomobile.mvp.InjectViewState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import ru.terrakok.gitlabclient.markwonx.GitlabMarkdownExtension
import ru.terrakok.gitlabclient.markwonx.MarkdownClickMediator
import ru.terrakok.gitlabclient.markwonx.label.LabelDescription
import ru.terrakok.gitlabclient.markwonx.milestone.MilestoneDescription
import ru.terrakok.gitlabclient.presentation.global.BasePresenter
import ru.terrakok.gitlabclient.presentation.global.ErrorHandler
import ru.terrakok.gitlabclient.presentation.global.ProjectMarkDownConverterProvider
import timber.log.Timber
import javax.inject.Inject

@InjectViewState
class MarkdownPresenter @Inject constructor(
    private val mdConverterProvider: ProjectMarkDownConverterProvider,
    private val errorHandler: ErrorHandler,
    private val markdownClickMediator: MarkdownClickMediator
) : BasePresenter<MarkdownView>() {

    var conversionDisposable: Disposable? = null

    fun setMarkdown(markdown: String, projectId: Long?) {
        conversionDisposable?.dispose()
        conversionDisposable = mdConverterProvider
            .getMarkdownConverter(projectId)
            .flatMap { it.markdownToSpannable(markdown) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { viewState.setMarkdownText(it) },
                { errorHandler.proceed(it) }
            )
        conversionDisposable?.connect()
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

}
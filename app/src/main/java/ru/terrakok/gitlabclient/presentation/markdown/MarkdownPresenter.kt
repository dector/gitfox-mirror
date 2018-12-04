package ru.terrakok.gitlabclient.presentation.markdown

import com.arellomobile.mvp.InjectViewState
import io.reactivex.disposables.Disposable
import ru.terrakok.gitlabclient.presentation.global.BasePresenter
import ru.terrakok.gitlabclient.presentation.global.ErrorHandler
import ru.terrakok.gitlabclient.presentation.global.MarkDownConverter
import javax.inject.Inject

@InjectViewState
class MarkdownPresenter @Inject constructor(
    private val markDownConverter: MarkDownConverter,
    private val errorHandler: ErrorHandler
) : BasePresenter<MarkdownView>() {

    private var conversionDisposable: Disposable? = null

    fun setMarkdown(markdown: String, projectId: Long?) {
        conversionDisposable?.dispose()
        conversionDisposable = markDownConverter
            .markdownToSpannable(markdown)
            .subscribe(
                { viewState.setMarkdownText(it) },
                { errorHandler.proceed(it) }
            )
    }

    override fun detachView(view: MarkdownView?) {
        super.detachView(view)
        conversionDisposable?.dispose()
    }

    override fun onDestroy() {
        super.onDestroy()
        conversionDisposable?.dispose()
    }
}
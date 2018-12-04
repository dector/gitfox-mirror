package ru.terrakok.gitlabclient.presentation.markdown

import com.arellomobile.mvp.InjectViewState
import io.reactivex.android.schedulers.AndroidSchedulers
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

    var conversionDisposable: Disposable? = null

    fun setMarkdown(markdown: String, projectId: Long?) {
        conversionDisposable?.dispose()
        conversionDisposable = markDownConverter
            .markdownToSpannable(markdown)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { viewState.setMarkdownText(it) },
                { errorHandler.proceed(it) }
            )
        conversionDisposable?.connect()
    }

    override fun detachView(view: MarkdownView?) {
        super.detachView(view)
        conversionDisposable?.dispose()
    }

}
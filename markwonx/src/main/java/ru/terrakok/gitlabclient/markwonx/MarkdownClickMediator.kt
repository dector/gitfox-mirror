package ru.terrakok.gitlabclient.markwonx

import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers

class MarkdownClickMediator {
    data class MarkdownClickEvent(val extension: GitlabMarkdownExtension, val value: Any)

    private val events by lazy { PublishRelay.create<MarkdownClickEvent>() }

    fun markdownClicked(extension: GitlabMarkdownExtension, value: Any) {
        events.accept(MarkdownClickEvent(extension, value))
    }

    fun getClickEvents(): Observable<MarkdownClickEvent> =
        events.hide().observeOn(AndroidSchedulers.mainThread())
}
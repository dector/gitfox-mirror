package ru.terrakok.gitlabclient.presentation.markdown

import android.text.Spanned
import moxy.MvpView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.terrakok.gitlabclient.markwonx.GitlabMarkdownExtension

@StateStrategyType(OneExecutionStateStrategy::class)
interface MarkdownView : MvpView {

    fun setMarkdownText(text: Spanned)

    fun markdownClicked(extension: GitlabMarkdownExtension, value: Any)

}
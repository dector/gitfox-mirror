package ru.terrakok.gitlabclient.presentation.markdown

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import ru.terrakok.gitlabclient.markwonx.GitlabMarkdownExtension

@StateStrategyType(OneExecutionStateStrategy::class)
interface MarkdownView : MvpView {

    fun setMarkdownText(text: CharSequence)

    fun markdownClicked(extension: GitlabMarkdownExtension, value: Any)

}
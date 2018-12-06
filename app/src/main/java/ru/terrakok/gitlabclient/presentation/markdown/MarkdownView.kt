package ru.terrakok.gitlabclient.presentation.markdown

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(OneExecutionStateStrategy::class)
interface MarkdownView : MvpView {

    fun setMarkdownText(text: CharSequence)

}
package ru.terrakok.gitlabclient.presentation.project.labels

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import ru.terrakok.gitlabclient.presentation.global.ListMvpView

/**
 * @author Maxim Myalkin (MaxMyalkin) on 11.11.2018.
 */
interface ProjectLabelsView : MvpView, ListMvpView<LabelUi> {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showMessage(message: String)

}
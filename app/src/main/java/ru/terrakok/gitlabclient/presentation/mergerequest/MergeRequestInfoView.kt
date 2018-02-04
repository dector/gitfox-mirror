package ru.terrakok.gitlabclient.presentation.mergerequest

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import ru.terrakok.gitlabclient.entity.Project
import ru.terrakok.gitlabclient.entity.mergerequest.MergeRequest

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 27.04.17.
 */
@StateStrategyType(AddToEndSingleStrategy::class)
interface MergeRequestInfoView : MvpView {
    data class MergeRequestInfo(val mr: MergeRequest, val project: Project, val mdDescription: CharSequence)

    fun showMergeRequest(mrInfo: MergeRequestInfo)
    fun showProgress(show: Boolean)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showMessage(message: String)
}
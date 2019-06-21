package ru.terrakok.gitlabclient.presentation.mergerequest.details

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import ru.terrakok.gitlabclient.entity.mergerequest.MergeRequest

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 31.05.19.
 */
@StateStrategyType(AddToEndSingleStrategy::class)
interface MergeRequestDetailsView : MvpView {

    fun showDetails(mr: MergeRequest)
    fun showEmptyProgress(show: Boolean)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showMessage(message: String)
}
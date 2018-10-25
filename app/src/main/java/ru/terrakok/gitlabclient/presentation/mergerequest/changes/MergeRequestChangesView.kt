package ru.terrakok.gitlabclient.presentation.mergerequest.changes

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import ru.terrakok.gitlabclient.entity.mergerequest.MergeRequestChange

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 26.10.18.
 */
@StateStrategyType(AddToEndSingleStrategy::class)
interface MergeRequestChangesView : MvpView {

    fun showEmptyProgress(show: Boolean)
    fun showChanges(changes: List<MergeRequestChange>)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showMessage(message: String)
}
package ru.terrakok.gitlabclient.presentation.mergerequest.notes

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import ru.terrakok.gitlabclient.presentation.global.NoteWithFormattedBody

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 12.02.18.
 */
@StateStrategyType(AddToEndSingleStrategy::class)
interface MergeRequestNotesView : MvpView {

    fun showRefreshProgress(show: Boolean)
    fun showEmptyProgress(show: Boolean)
    fun showPageProgress(show: Boolean)
    fun showEmptyView(show: Boolean)
    fun showEmptyError(show: Boolean, message: String?)
    fun showNotes(show: Boolean, notes: List<NoteWithFormattedBody>)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showMessage(message: String)
}
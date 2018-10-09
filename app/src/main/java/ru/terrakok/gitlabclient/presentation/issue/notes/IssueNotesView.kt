package ru.terrakok.gitlabclient.presentation.issue.notes

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import ru.terrakok.gitlabclient.presentation.global.NoteWithFormattedBody

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 12.02.18.
 */
@StateStrategyType(AddToEndSingleStrategy::class)
interface IssueNotesView : MvpView {

    fun showEmptyProgress(show: Boolean)
    fun showNotes(notes: List<NoteWithFormattedBody>)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showMessage(message: String)
}
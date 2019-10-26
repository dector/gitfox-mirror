package ru.terrakok.gitlabclient.presentation.issue.notes

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.terrakok.gitlabclient.presentation.global.NoteWithFormattedBody

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 12.02.18.
 */
@StateStrategyType(AddToEndSingleStrategy::class)
interface IssueNotesView : MvpView {

    fun showEmptyProgress(show: Boolean)
    fun showNotes(notes: List<NoteWithFormattedBody>, scrollToPosition: Int?)
    fun showBlockingProgress(show: Boolean)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showMessage(message: String)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun clearInput()
}
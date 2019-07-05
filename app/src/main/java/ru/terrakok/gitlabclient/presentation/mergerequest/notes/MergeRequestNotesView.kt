package ru.terrakok.gitlabclient.presentation.mergerequest.notes

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import ru.terrakok.gitlabclient.presentation.global.NoteWithProjectId

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 12.02.18.
 */
@StateStrategyType(AddToEndSingleStrategy::class)
interface MergeRequestNotesView : MvpView {

    fun showEmptyProgress(show: Boolean)
    fun showNotes(notes: List<NoteWithProjectId>, scrollToPosition: Int?)
    fun showBlockingProgress(show: Boolean)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showMessage(message: String)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun clearInput()
}
package ru.terrakok.gitlabclient.presentation.mergerequest.notes

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import ru.terrakok.gitlabclient.entity.Note

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 12.02.18.
 */
@StateStrategyType(AddToEndSingleStrategy::class)
interface MergeRequestNotesView : MvpView {
    data class NoteWithFormattedBody(val note: Note, val body: CharSequence)

    fun showNotes(notes: List<NoteWithFormattedBody>)
    fun showProgress(show: Boolean)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showMessage(message: String)
}
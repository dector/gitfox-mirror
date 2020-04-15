package ru.terrakok.gitlabclient.presentation.mergerequest.notes

import gitfox.entity.app.target.TargetAction
import gitfox.model.interactor.MergeRequestInteractor
import kotlinx.coroutines.launch
import moxy.InjectViewState
import ru.terrakok.gitlabclient.di.MergeRequestId
import ru.terrakok.gitlabclient.di.PrimitiveWrapper
import ru.terrakok.gitlabclient.di.ProjectId
import ru.terrakok.gitlabclient.presentation.global.BasePresenter
import ru.terrakok.gitlabclient.presentation.global.ErrorHandler
import ru.terrakok.gitlabclient.presentation.global.MarkDownConverter
import ru.terrakok.gitlabclient.presentation.global.NoteWithFormattedBody
import javax.inject.Inject

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 12.02.18.
 */
@InjectViewState
class MergeRequestNotesPresenter @Inject constructor(
    @ProjectId projectIdWrapper: PrimitiveWrapper<Long>,
    @MergeRequestId mrIdWrapper: PrimitiveWrapper<Long>,
    private val targetAction: TargetAction,
    private val mrInteractor: MergeRequestInteractor,
    private val mdConverter: MarkDownConverter,
    private val errorHandler: ErrorHandler
) : BasePresenter<MergeRequestNotesView>() {

    private val projectId = projectIdWrapper.value
    private val mrId = mrIdWrapper.value

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        launch {
            viewState.showEmptyProgress(true)
            try {
                val notes = mrInteractor.getAllMergeRequestNotes(projectId, mrId)
                    .map { note ->
                        NoteWithFormattedBody(note, mdConverter.toSpannable(note.body))
                    }
                val selectedNotePosition = targetAction.let { it as? TargetAction.CommentedOn }
                    ?.noteId
                    ?.let { noteIdToSelect -> notes.indexOfFirst { it.note.id == noteIdToSelect } }
                viewState.showNotes(notes, selectedNotePosition)
            } catch (e: Exception) {
                errorHandler.proceed(e) { viewState.showMessage(it) }
            }
            viewState.showEmptyProgress(false)
        }
    }

    fun onSendClicked(body: String) {
        launch {
            viewState.showBlockingProgress(true)
            try {
                mrInteractor.createMergeRequestNote(projectId, mrId, body)
                val notes = mrInteractor.getAllMergeRequestNotes(projectId, mrId)
                    .map { note ->
                        NoteWithFormattedBody(note, mdConverter.toSpannable(note.body))
                    }
                viewState.showNotes(notes, notes.size - 1)
                viewState.clearInput()
            } catch (e: Exception) {
                errorHandler.proceed(e) { viewState.showMessage(it) }
            }
            viewState.showBlockingProgress(false)
        }
    }
}

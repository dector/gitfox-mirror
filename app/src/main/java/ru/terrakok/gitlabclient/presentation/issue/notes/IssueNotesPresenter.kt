package ru.terrakok.gitlabclient.presentation.issue.notes

import gitfox.entity.app.target.TargetAction
import gitfox.model.interactor.IssueInteractor
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import moxy.InjectViewState
import ru.terrakok.gitlabclient.di.IssueId
import ru.terrakok.gitlabclient.di.PrimitiveWrapper
import ru.terrakok.gitlabclient.di.ProjectId
import ru.terrakok.gitlabclient.presentation.global.BasePresenter
import ru.terrakok.gitlabclient.presentation.global.ErrorHandler
import ru.terrakok.gitlabclient.presentation.global.NoteWithProjectId
import javax.inject.Inject

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 12.02.18.
 */
@InjectViewState
class IssueNotesPresenter @Inject constructor(
    @ProjectId projectIdWrapper: PrimitiveWrapper<Long>,
    @IssueId issueIdWrapper: PrimitiveWrapper<Long>,
    private val targetAction: TargetAction,
    private val issueInteractor: IssueInteractor,
    private val errorHandler: ErrorHandler
) : BasePresenter<IssueNotesView>() {

    private val projectId = projectIdWrapper.value
    private val issueId = issueIdWrapper.value

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        launch {
            loadAndScrollToPosition()
            issueInteractor.issueChanges
                .filter { it == issueId }
                .collect { loadAndScrollToPosition() }
        }
    }

    private suspend fun loadAndScrollToPosition() {
        viewState.showEmptyProgress(true)
        try {
            val notes = getAllIssueNotes()
            val selectedNotePosition =
                notes.indexOfFirst { it.note.id == (targetAction as? TargetAction.CommentedOn)?.noteId }
            viewState.showNotes(notes, selectedNotePosition)
        } catch (e: Exception) {
            errorHandler.proceed(e) { viewState.showMessage(it) }
        }
        viewState.showEmptyProgress(false)
    }

    fun onSendClicked(body: String) {
        launch {
            viewState.showBlockingProgress(true)
            try {
                issueInteractor.createIssueNote(projectId, issueId, body)
                val allNotes = getAllIssueNotes()
                viewState.showNotes(allNotes, allNotes.size - 1)
                viewState.clearInput()
            } catch (e: Exception) {
                errorHandler.proceed(e) { viewState.showMessage(it) }
            }
            viewState.showBlockingProgress(false)
        }
    }

    private suspend fun getAllIssueNotes(): List<NoteWithProjectId> =
        issueInteractor.getAllIssueNotes(projectId, issueId)
            .map { note -> NoteWithProjectId(note, projectId) }
}

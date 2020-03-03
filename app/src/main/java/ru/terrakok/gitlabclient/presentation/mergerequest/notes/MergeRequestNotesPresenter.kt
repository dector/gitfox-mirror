package ru.terrakok.gitlabclient.presentation.mergerequest.notes

import moxy.InjectViewState
import ru.terrakok.gitlabclient.di.MergeRequestId
import ru.terrakok.gitlabclient.di.PrimitiveWrapper
import ru.terrakok.gitlabclient.di.ProjectId
import ru.terrakok.gitlabclient.entity.app.target.TargetAction
import ru.terrakok.gitlabclient.model.interactor.MergeRequestInteractor
import ru.terrakok.gitlabclient.presentation.global.BasePresenter
import ru.terrakok.gitlabclient.presentation.global.ErrorHandler
import ru.terrakok.gitlabclient.presentation.global.NoteWithProjectId
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
    private val errorHandler: ErrorHandler
) : BasePresenter<MergeRequestNotesView>() {

    private val projectId = projectIdWrapper.value
    private val mrId = mrIdWrapper.value

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        mrInteractor
            .getAllMergeRequestNotes(projectId, mrId)
            .doOnSubscribe { viewState.showEmptyProgress(true) }
            .doAfterTerminate { viewState.showEmptyProgress(false) }
            .subscribe(
                { notes ->
                    val notesWithProjectId =
                        notes.map {
                            NoteWithProjectId(it, projectId)
                        }

                    val selectedNotePosition =
                        targetAction
                            .let { it as? TargetAction.CommentedOn }
                            ?.noteId
                            ?.let { noteIdToSelect -> notes.indexOfFirst { note -> note.id == noteIdToSelect } }
                    viewState.showNotes(notesWithProjectId, selectedNotePosition)
                },
                { errorHandler.proceed(it, { viewState.showMessage(it) }) }
            )
            .connect()
    }

    fun onSendClicked(body: String) =
        mrInteractor.createMergeRequestNote(projectId, mrId, body)
            .flatMap { mrInteractor.getAllMergeRequestNotes(projectId, mrId) }
            .doOnSubscribe { viewState.showBlockingProgress(true) }
            .doAfterTerminate { viewState.showBlockingProgress(false) }
            .subscribe(
                {
                    viewState.showNotes(it.map { NoteWithProjectId(it, projectId) }, it.size - 1)
                    viewState.clearInput()
                },
                { errorHandler.proceed(it, { viewState.showMessage(it) }) }
            )
            .connect()
}

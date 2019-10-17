package ru.terrakok.gitlabclient.presentation.issue.notes

import com.arellomobile.mvp.InjectViewState
import io.reactivex.Single
import ru.terrakok.gitlabclient.di.IssueId
import ru.terrakok.gitlabclient.di.PrimitiveWrapper
import ru.terrakok.gitlabclient.di.ProjectId
import ru.terrakok.gitlabclient.entity.app.target.TargetAction
import ru.terrakok.gitlabclient.model.interactor.IssueInteractor
import ru.terrakok.gitlabclient.presentation.global.BasePresenter
import ru.terrakok.gitlabclient.presentation.global.ErrorHandler
import ru.terrakok.gitlabclient.presentation.global.MarkDownConverter
import ru.terrakok.gitlabclient.presentation.global.NoteWithFormattedBody
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
    private val mdConverter: MarkDownConverter,
    private val errorHandler: ErrorHandler
) : BasePresenter<IssueNotesView>() {

    private val projectId = projectIdWrapper.value
    private val issueId = issueIdWrapper.value

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        issueInteractor
            .issueChanges
            .startWith(issueId)
            .filter { it == issueId }
            .switchMapSingle { getAllIssueNotes() }
            .subscribe(
                { notes ->
                    val selectedNotePosition = targetAction.let { it as? TargetAction.CommentedOn }
                        ?.noteId
                        ?.let { noteIdToSelect -> notes.indexOfFirst { it.note.id == noteIdToSelect } }
                    viewState.showNotes(notes, selectedNotePosition)
                },
                { errorHandler.proceed(it, { viewState.showMessage(it) }) }
            )
            .connect()
    }

    private fun getAllIssueNotes(): Single<MutableList<NoteWithFormattedBody>> {
        return issueInteractor
            .getAllIssueNotes(projectId, issueId)
            .doOnSubscribe { viewState.showEmptyProgress(true) }
            .doAfterTerminate { viewState.showEmptyProgress(false) }
            .flattenAsObservable { it }
            .concatMap { note ->
                mdConverter.markdownToSpannable(note.body)
                    .map { NoteWithFormattedBody(note, it) }
                    .toObservable()
            }
            .toList()
    }

    fun onSendClicked(body: String) =
        issueInteractor.createIssueNote(projectId, issueId, body)
            .flatMap {
                issueInteractor.getAllIssueNotes(projectId, issueId)
                    .flattenAsObservable { it }
                    .concatMap { note ->
                        mdConverter.markdownToSpannable(note.body)
                            .map { NoteWithFormattedBody(note, it) }
                            .toObservable()
                    }
                    .toList()
            }
            .doOnSubscribe { viewState.showBlockingProgress(true) }
            .doAfterTerminate { viewState.showBlockingProgress(false) }
            .subscribe(
                {
                    viewState.showNotes(it, it.size - 1)
                    viewState.clearInput()
                },
                { errorHandler.proceed(it, { viewState.showMessage(it) }) }
            )
            .connect()
}
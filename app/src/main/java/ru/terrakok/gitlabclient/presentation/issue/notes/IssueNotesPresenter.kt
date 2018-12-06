package ru.terrakok.gitlabclient.presentation.issue.notes

import com.arellomobile.mvp.InjectViewState
import ru.terrakok.gitlabclient.model.interactor.issue.IssueInteractor
import ru.terrakok.gitlabclient.presentation.global.NoteWithProjectId
import ru.terrakok.gitlabclient.presentation.global.BasePresenter
import ru.terrakok.gitlabclient.presentation.global.ErrorHandler
import ru.terrakok.gitlabclient.toothpick.PrimitiveWrapper
import ru.terrakok.gitlabclient.toothpick.qualifier.IssueId
import ru.terrakok.gitlabclient.toothpick.qualifier.ProjectId
import javax.inject.Inject

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 12.02.18.
 */
@InjectViewState
class IssueNotesPresenter @Inject constructor(
    @ProjectId projectIdWrapper: PrimitiveWrapper<Long>,
    @IssueId issueIdWrapper: PrimitiveWrapper<Long>,
    private val issueInteractor: IssueInteractor,
    private val errorHandler: ErrorHandler
) : BasePresenter<IssueNotesView>() {

    private val projectId = projectIdWrapper.value
    private val issueId = issueIdWrapper.value

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        issueInteractor
            .getAllIssueNotes(projectId, issueId)
            .doOnSubscribe { viewState.showEmptyProgress(true) }
            .doAfterTerminate { viewState.showEmptyProgress(false) }
            .subscribe(
                { viewState.showNotes(it.map {
                    NoteWithProjectId(
                        it,
                        projectId
                    )
                }, false) },
                { errorHandler.proceed(it, { viewState.showMessage(it) }) }
            )
            .connect()
    }

    fun onSendClicked(body: String) =
        issueInteractor.createIssueNote(projectId, issueId, body)
            .flatMap {
                issueInteractor.getAllIssueNotes(projectId, issueId)
            }
            .doOnSubscribe { viewState.showBlockingProgress(true) }
            .doAfterTerminate { viewState.showBlockingProgress(false) }
            .subscribe(
                { viewState.showNotes(it.map {
                    NoteWithProjectId(
                        it,
                        projectId
                    )
                }, true) },
                { errorHandler.proceed(it, { viewState.showMessage(it) }) }
            )
            .connect()
}
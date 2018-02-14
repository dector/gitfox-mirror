package ru.terrakok.gitlabclient.presentation.mergerequest.notes

import ru.terrakok.cicerone.Router
import ru.terrakok.gitlabclient.model.interactor.mergerequest.MergeRequestInteractor
import ru.terrakok.gitlabclient.presentation.global.BasePresenter
import ru.terrakok.gitlabclient.presentation.global.ErrorHandler
import ru.terrakok.gitlabclient.presentation.global.MarkDownConverter
import ru.terrakok.gitlabclient.toothpick.PrimitiveWrapper
import ru.terrakok.gitlabclient.toothpick.qualifier.MergeRequestId
import ru.terrakok.gitlabclient.toothpick.qualifier.ProjectId
import javax.inject.Inject

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 12.02.18.
 */
class MergeRequestNotesPresenter @Inject constructor(
        @ProjectId projectIdWrapper: PrimitiveWrapper<Long>,
        @MergeRequestId mrIdWrapper: PrimitiveWrapper<Long>,
        private val router: Router,
        private val mrInteractor: MergeRequestInteractor,
        private val mdConverter: MarkDownConverter,
        private val errorHandler: ErrorHandler
) : BasePresenter<MergeRequestNotesView>() {

    private val projectId = projectIdWrapper.value
    private val mrId = mrIdWrapper.value

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        mrInteractor.getMergeRequestNotes(projectId, mrId)
                .toObservable()
                .flatMapIterable { it }
                .flatMap { note ->
                    mdConverter.markdownToSpannable(note.body)
                            .map { MergeRequestNotesView.NoteWithFormattedBody(note, it) }
                            .toObservable()
                }
                .toList()
                .doOnSubscribe { viewState.showProgress(true) }
                .doAfterTerminate { viewState.showProgress(false) }
                .subscribe(
                        { viewState.showNotes(it) },
                        { errorHandler.proceed(it, { viewState.showMessage(it) }) }
                )
                .connect()
    }
}
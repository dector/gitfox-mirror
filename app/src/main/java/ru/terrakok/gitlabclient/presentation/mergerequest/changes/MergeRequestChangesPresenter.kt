package ru.terrakok.gitlabclient.presentation.mergerequest.changes

import com.arellomobile.mvp.InjectViewState
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
@InjectViewState
class MergeRequestChangesPresenter @Inject constructor(
    @ProjectId projectIdWrapper: PrimitiveWrapper<Long>,
    @MergeRequestId mrIdWrapper: PrimitiveWrapper<Long>,
    private val mrInteractor: MergeRequestInteractor,
    private val mdConverter: MarkDownConverter,
    private val errorHandler: ErrorHandler
) : BasePresenter<MergeRequestChangesView>() {

    private val projectId = projectIdWrapper.value
    private val mrId = mrIdWrapper.value

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        mrInteractor
            .getMergeRequestChanges(projectId, mrId)
            .doOnSubscribe { viewState.showEmptyProgress(true) }
            .doAfterTerminate { viewState.showEmptyProgress(false) }
            .subscribe(
                { viewState.showChanges(it) },
                { errorHandler.proceed(it, { viewState.showMessage(it) }) }
            )
            .connect()
    }
}
package ru.terrakok.gitlabclient.presentation.mergerequest.changes

import javax.inject.Inject
import moxy.InjectViewState
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.di.MergeRequestId
import ru.terrakok.gitlabclient.di.PrimitiveWrapper
import ru.terrakok.gitlabclient.di.ProjectId
import ru.terrakok.gitlabclient.entity.DiffData
import ru.terrakok.gitlabclient.model.interactor.MergeRequestInteractor
import ru.terrakok.gitlabclient.model.system.flow.FlowRouter
import ru.terrakok.gitlabclient.presentation.global.BasePresenter
import ru.terrakok.gitlabclient.presentation.global.ErrorHandler

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 12.02.18.
 */
@InjectViewState
class MergeRequestDiffDataListPresenter @Inject constructor(
    @ProjectId projectIdWrapper: PrimitiveWrapper<Long>,
    @MergeRequestId mrIdWrapper: PrimitiveWrapper<Long>,
    private val mrInteractor: MergeRequestInteractor,
    private val errorHandler: ErrorHandler,
    private val flowRouter: FlowRouter
) : BasePresenter<MergeRequestDiffDataListView>() {

    private val projectId = projectIdWrapper.value
    private val mrId = mrIdWrapper.value

    private val diffDataList = arrayListOf<DiffData>()
    private var isEmptyError: Boolean = false

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        mrInteractor
            .getMergeRequestDiffDataList(projectId, mrId)
            .doOnSubscribe { viewState.showEmptyProgress(true) }
            .doAfterTerminate { viewState.showEmptyProgress(false) }
            .subscribe(
                {
                    if (it.isNotEmpty()) {
                        diffDataList.addAll(it)
                        viewState.showDiffDataList(true, it)
                    } else {
                        viewState.showDiffDataList(false, it)
                        viewState.showEmptyView(true)
                    }
                },
                {
                    isEmptyError = true
                    errorHandler.proceed(it, { viewState.showEmptyError(true, it) })
                }
            )
            .connect()
    }

    fun refreshDiffDataList() {
        mrInteractor
            .getMergeRequestDiffDataList(projectId, mrId)
            .doOnSubscribe {
                if (isEmptyError) {
                    viewState.showEmptyError(false, null)
                    isEmptyError = false
                }
                if (diffDataList.isEmpty()) {
                    viewState.showEmptyView(false)
                }
                if (diffDataList.isNotEmpty()) {
                    viewState.showRefreshProgress(true)
                } else {
                    viewState.showEmptyProgress(true)
                }
            }
            .subscribe(
                {
                    if (diffDataList.isNotEmpty()) {
                        viewState.showRefreshProgress(false)
                    } else {
                        viewState.showEmptyProgress(false)
                    }
                    diffDataList.clear()
                    if (it.isNotEmpty()) {
                        diffDataList.addAll(it)
                        viewState.showDiffDataList(true, it)
                    } else {
                        viewState.showDiffDataList(false, it)
                        viewState.showEmptyView(true)
                    }
                },
                {
                    if (diffDataList.isNotEmpty()) {
                        viewState.showRefreshProgress(false)
                    } else {
                        viewState.showEmptyProgress(false)
                    }
                    errorHandler.proceed(
                        it,
                        {
                            if (diffDataList.isNotEmpty()) {
                                viewState.showMessage(it)
                            } else {
                                isEmptyError = true
                                viewState.showEmptyError(true, it)
                            }
                        }
                    )
                }
            )
            .connect()
    }

    fun onMergeRequestDiffDataClicked(item: DiffData) {
        mrInteractor.getMergeRequest(projectId, mrId)
            .doOnSubscribe { viewState.showFullscreenProgress(true) }
            .doAfterTerminate { viewState.showFullscreenProgress(false) }
            .subscribe(
                { flowRouter.startFlow(Screens.ProjectFile(projectId, item.newPath, it.sha)) },
                { errorHandler.proceed(it, { viewState.showMessage(it) }) }
            )
            .connect()
    }
}

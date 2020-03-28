package ru.terrakok.gitlabclient.presentation.mergerequest.changes

import gitfox.entity.DiffData
import gitfox.model.interactor.MergeRequestInteractor
import kotlinx.coroutines.launch
import moxy.InjectViewState
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.di.MergeRequestId
import ru.terrakok.gitlabclient.di.PrimitiveWrapper
import ru.terrakok.gitlabclient.di.ProjectId
import ru.terrakok.gitlabclient.presentation.global.BasePresenter
import ru.terrakok.gitlabclient.presentation.global.ErrorHandler
import ru.terrakok.gitlabclient.system.flow.FlowRouter
import javax.inject.Inject

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

        launch {
            viewState.showEmptyProgress(true)
            try {
                val diffList = mrInteractor.getMergeRequestDiffDataList(projectId, mrId)
                if (diffList.isNotEmpty()) {
                    diffDataList.addAll(diffList)
                    viewState.showDiffDataList(true, diffList)
                } else {
                    viewState.showDiffDataList(false, diffList)
                    viewState.showEmptyView(true)
                }
            } catch (e: Exception) {
                isEmptyError = true
                errorHandler.proceed(e) { viewState.showEmptyError(true, it) }
            }
            viewState.showEmptyProgress(false)
        }
    }

    fun refreshDiffDataList() {
        launch {
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
            try {
                val diffList = mrInteractor.getMergeRequestDiffDataList(projectId, mrId)
                if (diffDataList.isNotEmpty()) {
                    viewState.showRefreshProgress(false)
                } else {
                    viewState.showEmptyProgress(false)
                }
                diffDataList.clear()
                if (diffList.isNotEmpty()) {
                    diffDataList.addAll(diffList)
                    viewState.showDiffDataList(true, diffList)
                } else {
                    viewState.showDiffDataList(false, diffList)
                    viewState.showEmptyView(true)
                }
            } catch (e: Exception) {

                if (diffDataList.isNotEmpty()) {
                    viewState.showRefreshProgress(false)
                } else {
                    viewState.showEmptyProgress(false)
                }
                errorHandler.proceed(e) {
                    if (diffDataList.isNotEmpty()) {
                        viewState.showMessage(it)
                    } else {
                        isEmptyError = true
                        viewState.showEmptyError(true, it)
                    }
                }
            }
        }
    }

    fun onMergeRequestDiffDataClicked(item: DiffData) {
        launch {
            viewState.showFullscreenProgress(true)
            try {
                val mr = mrInteractor.getMergeRequest(projectId, mrId)
                flowRouter.startFlow(Screens.ProjectFile(projectId, item.newPath, mr.sha))
            } catch (e: Exception) {
                errorHandler.proceed(e) { viewState.showMessage(it) }
            }
            viewState.showFullscreenProgress(false)
        }
    }
}

package ru.terrakok.gitlabclient.presentation.commit

import gitfox.entity.DiffData
import gitfox.model.interactor.CommitInteractor
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import moxy.InjectViewState
import ru.terrakok.cicerone.Router
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.di.CommitId
import ru.terrakok.gitlabclient.di.PrimitiveWrapper
import ru.terrakok.gitlabclient.di.ProjectId
import ru.terrakok.gitlabclient.presentation.global.BasePresenter
import ru.terrakok.gitlabclient.presentation.global.ErrorHandler
import javax.inject.Inject

/**
 * @author Valentin Logvinovitch (glvvl) on 18.06.19.
 */
@InjectViewState
class CommitPresenter @Inject constructor(
    private val router: Router,
    @ProjectId private val projectIdWrapper: PrimitiveWrapper<Long>,
    @CommitId private val commitId: String,
    private val commitInteractor: CommitInteractor,
    private val errorHandler: ErrorHandler
) : BasePresenter<CommitView>() {

    private val projectId = projectIdWrapper.value

    private val diffDataList = arrayListOf<DiffData>()
    private var isEmptyError: Boolean = false

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        launch {
            viewState.showBlockingProgress(true)
            try {
                val commit = async { commitInteractor.getCommit(projectId, commitId) }
                val diff = commitInteractor.getCommitDiffData(projectId, commitId)
                viewState.showCommitInfo(commit.await())
                if (diff.isNotEmpty()) {
                    diffDataList.addAll(diff)
                    viewState.showDiffDataList(true, diff)
                } else {
                    viewState.showDiffDataList(false, diff)
                    viewState.showEmptyView(true)
                }
            } catch (e: Exception) {
                isEmptyError = true
                errorHandler.proceed(e) { viewState.showEmptyError(true, it) }
            }
            viewState.showBlockingProgress(false)
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
                val diffList = commitInteractor.getCommitDiffData(projectId, commitId)
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

    fun onDiffDataClicked(item: DiffData) =
        router.navigateTo(Screens.ProjectFile(projectId, item.newPath, commitId))

    fun onBackPressed() = router.exit()
}

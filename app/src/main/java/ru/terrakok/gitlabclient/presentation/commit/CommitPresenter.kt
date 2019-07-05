package ru.terrakok.gitlabclient.presentation.commit

import com.arellomobile.mvp.InjectViewState
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import ru.terrakok.cicerone.Router
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.di.CommitId
import ru.terrakok.gitlabclient.di.PrimitiveWrapper
import ru.terrakok.gitlabclient.di.ProjectId
import ru.terrakok.gitlabclient.entity.Commit
import ru.terrakok.gitlabclient.entity.DiffData
import ru.terrakok.gitlabclient.model.interactor.commit.CommitInteractor
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

        Single.zip(
            commitInteractor.getCommit(projectId, commitId),
            commitInteractor.getCommitDiffData(projectId, commitId),
            BiFunction<Commit, List<DiffData>, Pair<Commit, List<DiffData>>> { commit, diff -> commit to diff }
        )
                .doOnSubscribe { viewState.showBlockingProgress(true) }
                .doAfterTerminate { viewState.showBlockingProgress(false) }
                .subscribe({
                    viewState.showCommitInfo(it.first)
                    if (it.second.isNotEmpty()) {
                        diffDataList.addAll(it.second)
                        viewState.showDiffDataList(true, it.second)
                    } else {
                        viewState.showDiffDataList(false, it.second)
                        viewState.showEmptyView(true)
                    }
                }, {
                    isEmptyError = true
                    errorHandler.proceed(it, { viewState.showEmptyError(true, it) })
                }
                )
                .connect()
    }

    fun refreshDiffDataList() {
        commitInteractor
                .getCommitDiffData(projectId, commitId)
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
                        errorHandler.proceed(it) {
                            if (diffDataList.isNotEmpty()) {
                                viewState.showMessage(it)
                            } else {
                                isEmptyError = true
                                viewState.showEmptyError(true, it)
                            }
                        }
                    }
                )
                .connect()
    }

    fun onDiffDataClicked(item: DiffData) =
            router.navigateTo(Screens.ProjectFile(projectId, item.newPath, commitId))

    fun onBackPressed() = router.exit()
}
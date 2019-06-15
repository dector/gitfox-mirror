package ru.terrakok.gitlabclient.presentation.main

import com.arellomobile.mvp.InjectViewState
import io.reactivex.Single
import io.reactivex.functions.Function3
import javax.inject.Inject
import ru.terrakok.gitlabclient.model.interactor.issue.IssueInteractor
import ru.terrakok.gitlabclient.model.interactor.mergerequest.MergeRequestInteractor
import ru.terrakok.gitlabclient.model.interactor.todo.TodoInteractor
import ru.terrakok.gitlabclient.presentation.global.BasePresenter

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 20.05.19.
 */
@InjectViewState
class MainPresenter @Inject constructor(
    private val issueInteractor: IssueInteractor,
    private val mergeRequestInteractor: MergeRequestInteractor,
    private val todoInteractor: TodoInteractor
) : BasePresenter<MainView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        Single
            .zip(
                issueInteractor.getMyAssignedIssueCount(),
                mergeRequestInteractor.getMyAssignedMergeRequestCount(),
                todoInteractor.getMyAssignedTodoCount(),
                Function3<Int, Int, Int, Triple<Int, Int, Int>> { issueCount, mergeRequestCount, todoCount ->
                    Triple(issueCount, mergeRequestCount, todoCount)
                }
            )
            .subscribe(
                { viewState.setAssignedNotifications(it.first, it.second, it.third) },
                {
                    // TODO: user activity badges (Maybe we can retry this request, until it finishes correctly?).
                }
            )
            .connect()
    }
}
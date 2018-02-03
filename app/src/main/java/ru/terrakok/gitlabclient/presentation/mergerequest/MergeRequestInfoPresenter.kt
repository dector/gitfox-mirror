package ru.terrakok.gitlabclient.presentation.mergerequest

import com.arellomobile.mvp.InjectViewState
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import ru.terrakok.cicerone.Router
import ru.terrakok.gitlabclient.entity.Project
import ru.terrakok.gitlabclient.entity.mergerequest.MergeRequest
import ru.terrakok.gitlabclient.model.interactor.mergerequest.MergeRequestInteractor
import ru.terrakok.gitlabclient.model.interactor.project.ProjectInteractor
import ru.terrakok.gitlabclient.presentation.global.BasePresenter
import ru.terrakok.gitlabclient.presentation.global.ErrorHandler
import ru.terrakok.gitlabclient.presentation.global.MarkDownConverter
import ru.terrakok.gitlabclient.toothpick.PrimitiveWrapper
import ru.terrakok.gitlabclient.toothpick.qualifier.MergeRequestId
import ru.terrakok.gitlabclient.toothpick.qualifier.ProjectId
import javax.inject.Inject

private typealias MergeRequestLinker = BiFunction<Pair<MergeRequest, String>, Project, MergeRequestInfoView.MergeRequestInfo>

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 05.01.18.
 */
@InjectViewState
class MergeRequestInfoPresenter @Inject constructor(
        @ProjectId private val projectIdWrapper: PrimitiveWrapper<Long>,
        @MergeRequestId private val mrIdWrapper: PrimitiveWrapper<Long>,
        private val router: Router,
        private val mrInteractor: MergeRequestInteractor,
        private val projectInteractor: ProjectInteractor,
        private val mdConverter: MarkDownConverter,
        private val errorHandler: ErrorHandler
) : BasePresenter<MergeRequestInfoView>() {

    private val projectId = projectIdWrapper.value
    private val mrId = mrIdWrapper.value

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        Single
                .zip(
                        mrInteractor
                                .getMergeRequest(projectId, mrId)
                                .flatMap { mr ->
                                    mdConverter
                                            .markdownToHtml(mr.description ?: "")
                                            .map { Pair(mr, it) }
                                },
                        projectInteractor.getProject(projectId),
                        MergeRequestLinker { (mr, html), project -> MergeRequestInfoView.MergeRequestInfo(mr, project, html) }
                )
                .doOnSubscribe { viewState.showProgress(true) }
                .doAfterTerminate { viewState.showProgress(false) }
                .subscribe(
                        { viewState.showMergeRequest(it) },
                        { errorHandler.proceed(it, { viewState.showMessage(it) }) }
                )
                .connect()
    }

    fun onBackPressed() = router.exit()
}
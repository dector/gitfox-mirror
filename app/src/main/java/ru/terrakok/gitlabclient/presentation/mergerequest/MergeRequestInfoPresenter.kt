package ru.terrakok.gitlabclient.presentation.mergerequest

import ru.terrakok.cicerone.Router
import ru.terrakok.gitlabclient.model.interactor.mergerequest.MergeRequestInteractor
import ru.terrakok.gitlabclient.model.interactor.utils.UtilsInteractor
import ru.terrakok.gitlabclient.presentation.global.BasePresenter
import ru.terrakok.gitlabclient.presentation.global.ErrorHandler
import ru.terrakok.gitlabclient.toothpick.PrimitiveWrapper
import ru.terrakok.gitlabclient.toothpick.qualifier.MergeRequestId
import ru.terrakok.gitlabclient.toothpick.qualifier.ProjectId
import javax.inject.Inject

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 05.01.18.
 */
class MergeRequestInfoPresenter @Inject constructor(
        @ProjectId private val projectIdWrapper: PrimitiveWrapper<Long>,
        @MergeRequestId private val mrIdWrapper: PrimitiveWrapper<Long>,
        private val router: Router,
        private val interactor: MergeRequestInteractor,
        private val utils: UtilsInteractor,
        private val errorHandler: ErrorHandler
) : BasePresenter<MergeRequestInfoView>() {

    private val projectId = projectIdWrapper.value
    private val mrId = mrIdWrapper.value

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        interactor.getMergeRequest(projectId, mrId)
                .flatMap { mr ->
                    utils
                            .md2html(mr.description ?: "")
                            .map { Pair(mr, it) }
                }
                .doOnSubscribe { viewState.showProgress(true) }
                .doAfterTerminate { viewState.showProgress(false) }
                .subscribe(
                        { (mr, description) -> viewState.showMergeRequest(mr, description) },
                        { errorHandler.proceed(it, { viewState.showMessage(it) }) }
                )
                .connect()
    }

    fun onBackPressed() = router.exit()
}
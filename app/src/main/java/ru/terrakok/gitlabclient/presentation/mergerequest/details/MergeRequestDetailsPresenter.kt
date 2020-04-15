package ru.terrakok.gitlabclient.presentation.mergerequest.details

import gitfox.model.interactor.MergeRequestInteractor
import kotlinx.coroutines.launch
import moxy.InjectViewState
import ru.terrakok.gitlabclient.di.MergeRequestId
import ru.terrakok.gitlabclient.di.PrimitiveWrapper
import ru.terrakok.gitlabclient.di.ProjectId
import ru.terrakok.gitlabclient.presentation.global.BasePresenter
import ru.terrakok.gitlabclient.presentation.global.ErrorHandler
import ru.terrakok.gitlabclient.presentation.global.MarkDownConverter
import javax.inject.Inject

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 31.05.19.
 */
@InjectViewState
class MergeRequestDetailsPresenter @Inject constructor(
    @ProjectId projectIdWrapper: PrimitiveWrapper<Long>,
    @MergeRequestId mrIdWrapper: PrimitiveWrapper<Long>,
    private val mrInteractor: MergeRequestInteractor,
    private val mdConverter: MarkDownConverter,
    private val errorHandler: ErrorHandler
) : BasePresenter<MergeRequestDetailsView>() {

    private val projectId = projectIdWrapper.value
    private val mrId = mrIdWrapper.value

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        launch {
            viewState.showEmptyProgress(true)
            try {
                val mr = mrInteractor.getMergeRequest(projectId, mrId)
                val spnbl = mdConverter.toSpannable(mr.description)
                viewState.showDetails(mr, spnbl)
            } catch (e: Exception) {
                errorHandler.proceed(e) { viewState.showMessage(it) }
            }
            viewState.showEmptyProgress(false)
        }
    }
}

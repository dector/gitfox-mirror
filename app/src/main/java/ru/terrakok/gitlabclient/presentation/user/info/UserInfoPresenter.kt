package ru.terrakok.gitlabclient.presentation.user.info

import gitfox.model.interactor.UserInteractor
import kotlinx.coroutines.launch
import moxy.InjectViewState
import ru.terrakok.gitlabclient.di.PrimitiveWrapper
import ru.terrakok.gitlabclient.di.UserId
import ru.terrakok.gitlabclient.presentation.global.BasePresenter
import ru.terrakok.gitlabclient.presentation.global.ErrorHandler
import ru.terrakok.gitlabclient.system.flow.FlowRouter
import javax.inject.Inject

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 25.11.17.
 */
@InjectViewState
class UserInfoPresenter @Inject constructor(
    private val userInteractor: UserInteractor,
    private val router: FlowRouter,
    private val errorHandler: ErrorHandler,
    @UserId userIdWrapper: PrimitiveWrapper<Long>
) : BasePresenter<UserInfoView>() {
    private val userId = userIdWrapper.value

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        launch {
            viewState.showProgress(true)
            try {
                val user = userInteractor.getUser(userId)
                viewState.showUser(user)
            } catch (e: Exception) {
                errorHandler.proceed(e) { viewState.showMessage(it) }
            }
            viewState.showProgress(false)
        }
    }

    fun onBackPressed() = router.exit()
}

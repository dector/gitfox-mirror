package ru.terrakok.gitlabclient.presentation.user

import com.arellomobile.mvp.InjectViewState
import ru.terrakok.cicerone.Router
import ru.terrakok.gitlabclient.model.interactor.user.UserInteractor
import ru.terrakok.gitlabclient.presentation.global.BasePresenter
import ru.terrakok.gitlabclient.presentation.global.ErrorHandler
import ru.terrakok.gitlabclient.toothpick.PrimitiveWrapper
import ru.terrakok.gitlabclient.toothpick.qualifier.UserId
import javax.inject.Inject

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 25.11.17.
 */
@InjectViewState
class UserInfoPresenter @Inject constructor(
        private val userInteractor: UserInteractor,
        private val router: Router,
        private val errorHandler: ErrorHandler,
        @UserId userIdWrapper: PrimitiveWrapper<Long>
) : BasePresenter<UserInfoView>() {
    private val userId = userIdWrapper.value

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        userInteractor
                .getUser(userId)
                .doOnSubscribe { viewState.showProgress(true) }
                .doAfterTerminate { viewState.showProgress(false) }
                .subscribe(
                        { viewState.showUser(it) },
                        { errorHandler.proceed(it, { viewState.showMessage(it) }) }
                )
                .connect()
    }

    fun onBackPressed() = router.exit()
}
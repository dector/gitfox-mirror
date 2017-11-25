package ru.terrakok.gitlabclient.presentation.drawer

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.disposables.CompositeDisposable
import ru.terrakok.cicerone.Router
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.extension.addTo
import ru.terrakok.gitlabclient.model.interactor.auth.AuthInteractor
import ru.terrakok.gitlabclient.model.interactor.profile.MyProfileInteractor
import ru.terrakok.gitlabclient.presentation.drawer.NavigationDrawerView.MenuItem
import ru.terrakok.gitlabclient.presentation.drawer.NavigationDrawerView.MenuItem.*
import ru.terrakok.gitlabclient.presentation.global.ErrorHandler
import ru.terrakok.gitlabclient.presentation.global.GlobalMenuController
import javax.inject.Inject

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 04.04.17
 */
@InjectViewState
class NavigationDrawerPresenter @Inject constructor(
        private val router: Router,
        private val menuController: GlobalMenuController,
        private val authInteractor: AuthInteractor,
        private val myProfileInteractor: MyProfileInteractor,
        private val errorHandler: ErrorHandler
) : MvpPresenter<NavigationDrawerView>() {

    private var currentSelectedItem: MenuItem? = null
    private val compositeDisposable = CompositeDisposable()

    override fun onFirstViewAttach() {
        subscribeOnProfileUpdates()
    }

    private fun subscribeOnProfileUpdates() {
        myProfileInteractor.getMyProfile()
                .subscribe(
                        { viewState.showUserInfo(it) },
                        { errorHandler.proceed(it) }
                )
                .addTo(compositeDisposable)
    }

    fun onScreenChanged(item: MenuItem) {
        menuController.close()
        currentSelectedItem = item
        viewState.selectMenuItem(item)
    }

    fun onMenuItemClick(item: MenuItem) {
        menuController.close()
        if (item != currentSelectedItem) {
            when (item) {
                ACTIVITY -> router.newRootScreen(Screens.MAIN_SCREEN)
                PROJECTS -> router.newRootScreen(Screens.PROJECTS_SCREEN)
                ABOUT -> router.newRootScreen(Screens.ABOUT_SCREEN)
            }
        }
    }

    fun onLogoutClick() {
        menuController.close()
        authInteractor.logout()
        router.newRootScreen(Screens.AUTH_SCREEN)
    }

    fun onUserClick(id: Long) = router.navigateTo(Screens.USER_INFO_SCREEN, id)

    override fun onDestroy() {
        compositeDisposable.dispose()
    }
}
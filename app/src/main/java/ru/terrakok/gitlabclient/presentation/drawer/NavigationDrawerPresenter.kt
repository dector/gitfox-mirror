package ru.terrakok.gitlabclient.presentation.drawer

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.disposables.CompositeDisposable
import ru.terrakok.cicerone.Router
import ru.terrakok.gitlabclient.BuildConfig
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.extension.addTo
import ru.terrakok.gitlabclient.model.interactor.auth.AuthInteractor
import ru.terrakok.gitlabclient.model.interactor.profile.MyProfileInteractor
import ru.terrakok.gitlabclient.presentation.drawer.NavigationDrawerView.MenuItem
import ru.terrakok.gitlabclient.presentation.drawer.NavigationDrawerView.MenuItem.ABOUT
import ru.terrakok.gitlabclient.presentation.drawer.NavigationDrawerView.MenuItem.PROJECTS
import javax.inject.Inject

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 04.04.17
 */
@InjectViewState
class NavigationDrawerPresenter : MvpPresenter<NavigationDrawerView>() {
    @Inject lateinit var router: Router
    @Inject lateinit var authInteractor: AuthInteractor
    @Inject lateinit var myProfileInteractor: MyProfileInteractor

    private var currentSelectedItem: MenuItem? = null
    private val compositeDisposable = CompositeDisposable()

    override fun onFirstViewAttach() {
        subscribeOnProfileUpdates()
        viewState.showVersionName("${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})")
    }

    private fun subscribeOnProfileUpdates() {
        myProfileInteractor.getMyProfile()
                .subscribe({ viewState.showUserInfo(it) })
                .addTo(compositeDisposable)
    }

    fun onScreenChanged(item: MenuItem) {
        currentSelectedItem = item
        viewState.selectMenuItem(item)
    }

    fun onMenuItemClick(item: MenuItem) {
        if (item != currentSelectedItem) {
            when (item) {
                PROJECTS -> router.newRootScreen(Screens.MAIN_SCREEN)
                ABOUT -> router.newRootScreen(Screens.ABOUT_SCREEN)
                else -> router.showSystemMessage("Unknown screen yet!") //todo
            }
        }
    }

    fun onLogoutClick() {
        authInteractor.logout()
                .subscribe({ router.newRootScreen(Screens.AUTH_SCREEN) })
                .addTo(compositeDisposable)
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
    }
}
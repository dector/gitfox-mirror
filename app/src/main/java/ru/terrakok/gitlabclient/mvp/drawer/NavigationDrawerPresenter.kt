package ru.terrakok.gitlabclient.mvp.drawer

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.disposables.CompositeDisposable
import ru.terrakok.cicerone.Router
import ru.terrakok.gitlabclient.App
import ru.terrakok.gitlabclient.BuildConfig
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.extension.addTo
import ru.terrakok.gitlabclient.model.auth.AuthManager
import ru.terrakok.gitlabclient.model.profile.MyProfileManager
import ru.terrakok.gitlabclient.mvp.drawer.NavigationDrawerView.MenuItem
import ru.terrakok.gitlabclient.mvp.drawer.NavigationDrawerView.MenuItem.PROJECTS
import javax.inject.Inject

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 04.04.17
 */
@InjectViewState
class NavigationDrawerPresenter : MvpPresenter<NavigationDrawerView>() {
    @Inject lateinit var router: Router
    @Inject lateinit var authManager: AuthManager
    @Inject lateinit var myProfileManager: MyProfileManager

    private var currentSelectedItem: MenuItem? = null
    private val compositeDisposable = CompositeDisposable()

    init {
        App.DAGGER.appComponent.inject(this)
    }

    override fun onFirstViewAttach() {
        subscribeOnProfileUpdates()
        viewState.showVersionName("${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})")
    }

    private fun subscribeOnProfileUpdates() {
        myProfileManager.getMyProfile()
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
                else -> router.showSystemMessage("Unknown screen yet!") //todo
            }
        }
    }

    fun onLogoutClick() {
        authManager.logout()
                .subscribe({ router.newRootScreen(Screens.AUTH_SCREEN) })
                .addTo(compositeDisposable)
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
    }
}
package ru.terrakok.gitlabclient.ui.launch

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_main.*
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.android.SupportAppNavigator
import ru.terrakok.cicerone.commands.Command
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.model.system.ServerSwitcher
import ru.terrakok.gitlabclient.presentation.drawer.NavigationDrawerView
import ru.terrakok.gitlabclient.presentation.global.GlobalMenuController
import ru.terrakok.gitlabclient.presentation.launch.LaunchPresenter
import ru.terrakok.gitlabclient.presentation.launch.LaunchView
import ru.terrakok.gitlabclient.toothpick.DI
import ru.terrakok.gitlabclient.toothpick.module.MainActivityModule
import ru.terrakok.gitlabclient.ui.about.AboutFragment
import ru.terrakok.gitlabclient.ui.auth.AuthFragment
import ru.terrakok.gitlabclient.ui.drawer.NavigationDrawerFragment
import ru.terrakok.gitlabclient.ui.global.BaseActivity
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import ru.terrakok.gitlabclient.ui.main.MainFragment
import ru.terrakok.gitlabclient.ui.project.ProjectInfoFragment
import toothpick.Toothpick
import javax.inject.Inject

class MainActivity : BaseActivity(), LaunchView {
    @Inject lateinit var restarter: ServerSwitcher
    @Inject lateinit var navigationHolder: NavigatorHolder
    @Inject lateinit var menuController: GlobalMenuController

    private var restarterDisposable: Disposable? = null
    private var menuStateDisposable: Disposable? = null

    @InjectPresenter lateinit var presenter: LaunchPresenter

    @ProvidePresenter
    fun providePresenter(): LaunchPresenter {
        return Toothpick
                .openScope(DI.SERVER_SCOPE)
                .getInstance(LaunchPresenter::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)

        Toothpick.openScopes(DI.SERVER_SCOPE, DI.MAIN_ACTIVITY_SCOPE).apply {
            installModules(MainActivityModule())
            Toothpick.inject(this@MainActivity, this)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        restarterDisposable = restarter.restartLaunchActivitySignal.subscribe { restartActivity() }
        menuStateDisposable = menuController.state.subscribe { openNavDrawer(it) }
        navigationHolder.setNavigator(navigator)
    }

    override fun onPause() {
        restarterDisposable?.dispose()
        menuStateDisposable?.dispose()
        navigationHolder.removeNavigator()
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isFinishing) Toothpick.closeScope(DI.MAIN_ACTIVITY_SCOPE)
    }

    private val navigator = object : SupportAppNavigator(this, R.id.mainContainer) {

        override fun applyCommand(command: Command?) {
            super.applyCommand(command)
            updateNavDrawer()
        }

        override fun createActivityIntent(screenKey: String?, data: Any?) = null

        override fun createFragment(screenKey: String?, data: Any?): Fragment? = when (screenKey) {
            Screens.AUTH_SCREEN -> AuthFragment()
            Screens.MAIN_SCREEN -> MainFragment()
            Screens.PROJECT_SCREEN -> ProjectInfoFragment.createNewInstance(data as Long)
            Screens.ABOUT_SCREEN -> AboutFragment()
            else -> null
        }
    }

    //region nav drawer
    private fun openNavDrawer(open: Boolean) {
        drawerLayout.postDelayed({
            if (open) drawerLayout.openDrawer(GravityCompat.START)
            else drawerLayout.closeDrawer(GravityCompat.START)
        }, 150)
    }

    private fun enableNavDrawer(enable: Boolean) {
        drawerLayout.setDrawerLockMode(
                if (enable) DrawerLayout.LOCK_MODE_UNLOCKED
                else DrawerLayout.LOCK_MODE_LOCKED_CLOSED,
                GravityCompat.START
        )
    }

    private fun updateNavDrawer() {
        supportFragmentManager.executePendingTransactions()

        val drawerFragment = supportFragmentManager.findFragmentById(R.id.navigationDrawer) as NavigationDrawerFragment
        supportFragmentManager.findFragmentById(R.id.mainContainer)?.let {
            when (it) {
                is MainFragment -> drawerFragment.onScreenChanged(NavigationDrawerView.MenuItem.ACTIVITY)
                is AboutFragment -> drawerFragment.onScreenChanged(NavigationDrawerView.MenuItem.ABOUT)
            }
            enableNavDrawer(isNavDrawerAvailableForFragment(it))
        }
    }

    private fun isNavDrawerAvailableForFragment(currentFragment: Fragment) = when (currentFragment) {
        is MainFragment -> true
        is AboutFragment -> true
        else -> false
    }
    //endregion

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            openNavDrawer(false)
        } else {
            val fragment = supportFragmentManager.findFragmentById(R.id.mainContainer)
            if (fragment is BaseFragment) {
                fragment.onBackPressed()
            } else {
                presenter.onBackPressed()
            }
        }
    }

    private fun restartActivity() {
        finish()
        startActivity(Intent(this, MainActivity::class.java))
    }
}

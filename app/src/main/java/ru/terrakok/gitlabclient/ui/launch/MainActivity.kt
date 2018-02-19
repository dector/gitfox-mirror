package ru.terrakok.gitlabclient.ui.launch

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_main.*
import ru.terrakok.cicerone.commands.Command
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.model.system.flow.FlowNavigator
import ru.terrakok.gitlabclient.presentation.drawer.NavigationDrawerView
import ru.terrakok.gitlabclient.presentation.global.GlobalMenuController
import ru.terrakok.gitlabclient.presentation.launch.LaunchPresenter
import ru.terrakok.gitlabclient.presentation.launch.LaunchView
import ru.terrakok.gitlabclient.toothpick.DI
import ru.terrakok.gitlabclient.toothpick.module.MainActivityModule
import ru.terrakok.gitlabclient.ui.about.AboutFragment
import ru.terrakok.gitlabclient.ui.drawer.NavigationDrawerFragment
import ru.terrakok.gitlabclient.ui.global.BaseActivity
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import ru.terrakok.gitlabclient.ui.libraries.LibrariesFragment
import ru.terrakok.gitlabclient.ui.main.MainFragment
import ru.terrakok.gitlabclient.ui.projects.ProjectsContainerFragment
import toothpick.Toothpick
import javax.inject.Inject

class MainActivity : BaseActivity(), LaunchView {
    @Inject lateinit var menuController: GlobalMenuController

    private var menuStateDisposable: Disposable? = null

    override val layoutRes = R.layout.activity_main

    private val currentFragment
        get() = supportFragmentManager.findFragmentById(R.id.mainContainer) as BaseFragment?

    private val drawerFragment
        get() = supportFragmentManager.findFragmentById(R.id.navDrawerContainer) as NavigationDrawerFragment?

    @InjectPresenter
    lateinit var presenter: LaunchPresenter

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
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        menuStateDisposable = menuController.state.subscribe { openNavDrawer(it) }
    }

    override fun onPause() {
        menuStateDisposable?.dispose()
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isFinishing) Toothpick.closeScope(DI.MAIN_ACTIVITY_SCOPE)
    }

    override fun initMainScreen() {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.mainContainer, MainFragment())
                .replace(R.id.navDrawerContainer, NavigationDrawerFragment())
                .commitNow()
        updateNavDrawer()
    }

    override val navigator = object : FlowNavigator(this, R.id.mainContainer) {

        override fun applyCommands(commands: Array<out Command>?) {
            super.applyCommands(commands)
            updateNavDrawer()
        }

        override fun createFlowIntent(flowKey: String, data: Any?) =
                Screens.getFlowIntent(this@MainActivity, flowKey, data)

        override fun createFragment(screenKey: String?, data: Any?): Fragment? = when (screenKey) {
            Screens.MAIN_SCREEN -> MainFragment()
            Screens.PROJECTS_SCREEN -> ProjectsContainerFragment()
            Screens.ABOUT_SCREEN -> AboutFragment()
            Screens.APP_LIBRARIES_SCREEN -> LibrariesFragment()
            else -> null
        }
    }

    //region nav drawer
    private fun openNavDrawer(open: Boolean) {
        if (open) drawerLayout.openDrawer(GravityCompat.START)
        else drawerLayout.closeDrawer(GravityCompat.START)
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

        drawerFragment?.let { drawerFragment ->
            currentFragment?.let {
                when (it) {
                    is MainFragment -> drawerFragment.onScreenChanged(NavigationDrawerView.MenuItem.ACTIVITY)
                    is ProjectsContainerFragment -> drawerFragment.onScreenChanged(NavigationDrawerView.MenuItem.PROJECTS)
                    is AboutFragment -> drawerFragment.onScreenChanged(NavigationDrawerView.MenuItem.ABOUT)
                }
                enableNavDrawer(isNavDrawerAvailableForFragment(it))
            }
        }
    }

    private fun isNavDrawerAvailableForFragment(currentFragment: Fragment) = when (currentFragment) {
        is MainFragment,
        is ProjectsContainerFragment,
        is AboutFragment -> true
        else -> false
    }
    //endregion

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            openNavDrawer(false)
        } else {
            currentFragment?.onBackPressed() ?: presenter.onBackPressed()
        }
    }

    companion object {
        fun getStartIntent(context: Context) =
                Intent(context, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                }
    }
}

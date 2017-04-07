package ru.terrakok.gitlabclient.ui.launch

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.activity_main.*
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.android.SupportAppNavigator
import ru.terrakok.cicerone.commands.Command
import ru.terrakok.cicerone.commands.Forward
import ru.terrakok.gitlabclient.App
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.mvp.drawer.NavigationDrawerView
import ru.terrakok.gitlabclient.mvp.launch.LaunchPresenter
import ru.terrakok.gitlabclient.mvp.launch.LaunchView
import ru.terrakok.gitlabclient.ui.auth.AuthFragment
import ru.terrakok.gitlabclient.ui.drawer.NavigationDrawerFragment
import ru.terrakok.gitlabclient.ui.global.BaseActivity
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import ru.terrakok.gitlabclient.ui.main.MainFragment
import javax.inject.Inject

class MainActivity : BaseActivity(), LaunchView {
    @Inject lateinit var navigationHolder: NavigatorHolder

    @InjectPresenter lateinit var presenter: LaunchPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        App.DAGGER.appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        navigationHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigationHolder.removeNavigator()
        super.onPause()
    }

    private val navigator = object : SupportAppNavigator(this, R.id.mainContainer) {

        override fun applyCommand(command: Command?) {
            if (command is Forward && command.screenKey == Screens.NAVIGATION_DRAWER) {
                openNavDrawer(true)
            } else {
                openNavDrawer(false)
                super.applyCommand(command)
                updateNavDrawer()
            }
        }

        override fun createActivityIntent(screenKey: String?, data: Any?) = null

        override fun createFragment(screenKey: String?, data: Any?): Fragment? = when (screenKey) {
            Screens.AUTH_SCREEN -> AuthFragment()
            Screens.MAIN_SCREEN -> MainFragment()
            else -> null
        }
    }

    fun openNavDrawer(open: Boolean) {
        drawerLayout.post {
            if (open) drawerLayout.openDrawer(GravityCompat.START)
            else drawerLayout.closeDrawer(GravityCompat.START)
        }
    }

    fun enableNavDrawer(enable: Boolean) {
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
                is MainFragment -> drawerFragment.onScreenChanged(NavigationDrawerView.MenuItem.PROJECTS)
            }
            enableNavDrawer(isNavDrawerAvailableForFragment(it))
        }
    }

    private fun isNavDrawerAvailableForFragment(currentFragment: Fragment) = when(currentFragment) {
        is MainFragment -> true
        else -> false
    }

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
}

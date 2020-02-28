package ru.terrakok.gitlabclient.ui.drawer

import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import io.reactivex.disposables.Disposable
import javax.inject.Inject
import kotlinx.android.synthetic.main.drawer_flow_fragment.*
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import ru.terrakok.cicerone.commands.Command
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.di.module.FlowNavigationModule
import ru.terrakok.gitlabclient.di.module.GlobalMenuModule
import ru.terrakok.gitlabclient.presentation.drawer.NavigationDrawerView
import ru.terrakok.gitlabclient.presentation.global.GlobalMenuController
import ru.terrakok.gitlabclient.ui.about.AboutFragment
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import ru.terrakok.gitlabclient.ui.main.MainFragment
import ru.terrakok.gitlabclient.ui.projects.ProjectsContainerFragment
import ru.terrakok.gitlabclient.util.setLaunchScreen
import toothpick.Scope
import toothpick.Toothpick

class DrawerFlowFragment : BaseFragment() {
    @Inject
    lateinit var menuController: GlobalMenuController

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    @Inject
    lateinit var router: Router

    private var menuStateDisposable: Disposable? = null

    override val layoutRes = R.layout.drawer_flow_fragment

    private val currentFragment
        get() = childFragmentManager.findFragmentById(R.id.mainContainer) as? BaseFragment

    private val drawerFragment
        get() = childFragmentManager.findFragmentById(R.id.navDrawerContainer) as? NavigationDrawerFragment

    override fun installModules(scope: Scope) {
        scope.installModules(
            FlowNavigationModule(scope.getInstance(Router::class.java)),
            GlobalMenuModule()
        )
    }

    private val navigator: Navigator by lazy {
        object : SupportAppNavigator(this.activity!!, childFragmentManager, R.id.mainContainer) {

            override fun applyCommands(commands: Array<out Command>) {
                super.applyCommands(commands)
                updateNavDrawer()
            }

            override fun activityBack() {
                router.exit()
            }

            override fun setupFragmentTransaction(
                command: Command,
                currentFragment: Fragment?,
                nextFragment: Fragment?,
                fragmentTransaction: FragmentTransaction
            ) {
                // Fix incorrect order lifecycle callback of MainFragment
                fragmentTransaction.setReorderingAllowed(true)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Toothpick.inject(this, scope)

        if (childFragmentManager.fragments.isEmpty()) {
            childFragmentManager
                .beginTransaction()
                .replace(R.id.navDrawerContainer, NavigationDrawerFragment())
                .commitNow()

            navigator.setLaunchScreen(Screens.Main)
        } else {
            updateNavDrawer()
        }
    }

    override fun onResume() {
        super.onResume()
        menuStateDisposable = menuController.state.subscribe { openNavDrawer(it) }
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        menuStateDisposable?.dispose()
        super.onPause()
    }

    //region nav drawer
    private fun openNavDrawer(open: Boolean) {
        if (open) drawerLayout.openDrawer(GravityCompat.START)
        else drawerLayout.closeDrawer(GravityCompat.START)
    }

    private fun updateNavDrawer() {
        childFragmentManager.executePendingTransactions()

        drawerFragment?.let { drawerFragment ->
            currentFragment?.let {
                when (it) {
                    is MainFragment -> drawerFragment.onScreenChanged(NavigationDrawerView.MenuItem.ACTIVITY)
                    is ProjectsContainerFragment -> drawerFragment.onScreenChanged(NavigationDrawerView.MenuItem.PROJECTS)
                    is AboutFragment -> drawerFragment.onScreenChanged(NavigationDrawerView.MenuItem.ABOUT)
                }
            }
        }
    }
    //endregion

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            openNavDrawer(false)
        } else {
            currentFragment?.onBackPressed() ?: router.exit()
        }
    }
}

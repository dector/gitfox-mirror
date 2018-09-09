package ru.terrakok.gitlabclient.ui.global

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.android.SupportFragmentNavigator
import ru.terrakok.cicerone.commands.Command
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.Screens
import javax.inject.Inject

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 03.09.18.
 */
abstract class FlowFragment : BaseFragment() {
    override val layoutRes: Int = R.layout.layout_container

    private val currentFragment
        get() = childFragmentManager.findFragmentById(R.id.container) as? BaseFragment

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    protected val navigator: Navigator by lazy {
        object : SupportFragmentNavigator(childFragmentManager, R.id.container) {
            override fun exit() {
                onExit()
            }

            override fun createFragment(screenKey: String, data: Any?): Fragment? =
                Screens.createFragment(screenKey, data)

            override fun showSystemMessage(message: String?) {}

            override fun setupFragmentTransactionAnimation(
                command: Command?,
                currentFragment: Fragment?,
                nextFragment: Fragment?,
                fragmentTransaction: FragmentTransaction
            ) {
                //fix incorrect order lifecycle callback of MainFlowFragment
                fragmentTransaction.setReorderingAllowed(true)
            }
        }
    }

    override fun onBackPressed() {
        currentFragment?.onBackPressed() ?: super.onBackPressed()
    }

    open fun onExit() {}

    override fun onResume() {
        super.onResume()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }
}
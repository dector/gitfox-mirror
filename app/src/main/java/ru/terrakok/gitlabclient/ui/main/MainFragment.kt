package ru.terrakok.gitlabclient.ui.main

import android.os.Bundle
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter
import com.aurelhubert.ahbottomnavigation.notification.AHNotification
import kotlinx.android.synthetic.main.fragment_main.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.terrakok.cicerone.android.support.SupportAppScreen
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.entity.app.AccountMainBadges
import ru.terrakok.gitlabclient.presentation.main.MainPresenter
import ru.terrakok.gitlabclient.presentation.main.MainView
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import ru.terrakok.gitlabclient.util.addSystemBottomPadding
import ru.terrakok.gitlabclient.util.color

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 02.04.17
 */
class MainFragment : BaseFragment(), MainView {
    override val layoutRes = R.layout.fragment_main

    private val currentTabFragment: BaseFragment?
        get() = childFragmentManager.fragments.firstOrNull { !it.isHidden } as? BaseFragment

    @InjectPresenter
    lateinit var presenter: MainPresenter

    @ProvidePresenter
    fun providePresenter(): MainPresenter = scope.getInstance(MainPresenter::class.java)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        linearLayout.addSystemBottomPadding(bottomBar, true)
        AHBottomNavigationAdapter(activity, R.menu.main_bottom_menu).apply {
            setupWithBottomNavigation(bottomBar)
        }
        with(bottomBar) {
            accentColor = context.color(R.color.colorPrimary)
            inactiveColor = context.color(R.color.silver)

            setOnTabSelectedListener { position, wasSelected ->
                if (!wasSelected) selectTab(
                    when (position) {
                        0 -> eventsTab
                        1 -> issuesTab
                        2 -> mrsTab
                        else -> todosTab
                    }
                )
                true
            }
            val leftMargin = resources.getDimension(R.dimen.bottom_bar_notification_left_margin).toInt()
            setNotificationMarginLeft(leftMargin, leftMargin)
        }

        selectTab(
            when (currentTabFragment?.tag) {
                eventsTab.screenKey -> eventsTab
                issuesTab.screenKey -> issuesTab
                mrsTab.screenKey -> mrsTab
                todosTab.screenKey -> todosTab
                else -> eventsTab
            }
        )
    }

    private fun selectTab(tab: SupportAppScreen) {
        val currentFragment = currentTabFragment
        val newFragment = childFragmentManager.findFragmentByTag(tab.screenKey)

        if (currentFragment != null && newFragment != null && currentFragment == newFragment) return

        childFragmentManager.beginTransaction().apply {
            if (newFragment == null) add(R.id.mainScreenContainer, createTabFragment(tab), tab.screenKey)

            currentFragment?.let {
                hide(it)
                it.userVisibleHint = false
            }
            newFragment?.let {
                show(it)
                it.userVisibleHint = true
            }
        }.commitNow()
    }

    private fun createTabFragment(tab: SupportAppScreen) = tab.fragment!!

    override fun onBackPressed() {
        currentTabFragment?.onBackPressed()
    }

    override fun setAssignedNotifications(badges: AccountMainBadges) {
        with(bottomBar) {
            setNotification(buildBottomBarNotification(R.color.fruit_salad, badges.issueCount), 1)
            setNotification(
                buildBottomBarNotification(
                    R.color.brandy_punch,
                    badges.mergeRequestCount
                ), 2
            )
            setNotification(buildBottomBarNotification(R.color.mariner, badges.todoCount), 3)
        }
    }

    private fun buildBottomBarNotification(@ColorRes backgroundColor: Int, count: Int): AHNotification {
        return if (count > 0) {
            AHNotification.Builder()
                .setBackgroundColor(ContextCompat.getColor(requireContext(), backgroundColor))
                .setText(count.toString())
                .build()
        } else {
            AHNotification()
        }
    }

    companion object {
        private val eventsTab = Screens.MyEvents
        private val issuesTab = Screens.MyIssuesContainer
        private val mrsTab = Screens.MyMrContainer
        private val todosTab = Screens.MyTodosContainer
    }
}

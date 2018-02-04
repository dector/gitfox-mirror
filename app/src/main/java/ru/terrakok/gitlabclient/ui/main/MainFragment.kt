package ru.terrakok.gitlabclient.ui.main

import android.os.Bundle
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter
import kotlinx.android.synthetic.main.fragment_main.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.extension.color
import ru.terrakok.gitlabclient.presentation.main.MainPresenter
import ru.terrakok.gitlabclient.presentation.main.MainView
import ru.terrakok.gitlabclient.toothpick.DI
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import ru.terrakok.gitlabclient.ui.my.activity.MyEventsFragment
import ru.terrakok.gitlabclient.ui.my.issues.MyIssuesContainerFragment
import ru.terrakok.gitlabclient.ui.my.mergerequests.MyMergeRequestsContainerFragment
import ru.terrakok.gitlabclient.ui.my.todos.MyTodosContainerFragment
import toothpick.Toothpick

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 02.04.17
 */
class MainFragment : BaseFragment(), MainView {
    override val layoutRes = R.layout.fragment_main

    private lateinit var tabs: HashMap<String, BaseFragment>
    private val tabKeys = listOf(
            tabIdToFragmentTag(R.id.tab_activity),
            tabIdToFragmentTag(R.id.tab_issue),
            tabIdToFragmentTag(R.id.tab_merge),
            tabIdToFragmentTag(R.id.tab_todo)
    )

    @InjectPresenter
    lateinit var presenter: MainPresenter

    @ProvidePresenter
    fun providePresenter(): MainPresenter {
        return Toothpick
                .openScope(DI.APP_SCOPE)
                .getInstance(MainPresenter::class.java)
    }

    private fun tabIdToFragmentTag(id: Int) = "tab_$id"

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (savedInstanceState == null) {
            tabs = createNewFragments()
            childFragmentManager.beginTransaction()
                    .add(R.id.mainScreenContainer, tabs[tabKeys[0]], tabKeys[0])
                    .add(R.id.mainScreenContainer, tabs[tabKeys[1]], tabKeys[1])
                    .add(R.id.mainScreenContainer, tabs[tabKeys[2]], tabKeys[2])
                    .add(R.id.mainScreenContainer, tabs[tabKeys[3]], tabKeys[3])
                    .commit()
            showTab(0)
            bottomBar.setCurrentItem(0, false)
        } else {
            tabs = findFragments()
        }

        AHBottomNavigationAdapter(activity, R.menu.main_bottom_menu).apply {
            setupWithBottomNavigation(bottomBar)
        }
        with(bottomBar) {
            accentColor = context.color(R.color.colorPrimary)
            inactiveColor = context.color(R.color.silver)

            setOnTabSelectedListener { position, wasSelected ->
                if (!wasSelected) postDelayed({ showTab(position) }, 150)
                true
            }
        }
    }

    private fun createNewFragments(): HashMap<String, BaseFragment> = hashMapOf(
            tabKeys[0] to MyEventsFragment(),
            tabKeys[1] to MyIssuesContainerFragment(),
            tabKeys[2] to MyMergeRequestsContainerFragment(),
            tabKeys[3] to MyTodosContainerFragment()
    )

    private fun findFragments(): HashMap<String, BaseFragment> = hashMapOf(
            tabKeys[0] to childFragmentManager.findFragmentByTag(tabKeys[0]) as BaseFragment,
            tabKeys[1] to childFragmentManager.findFragmentByTag(tabKeys[1]) as BaseFragment,
            tabKeys[2] to childFragmentManager.findFragmentByTag(tabKeys[2]) as BaseFragment,
            tabKeys[3] to childFragmentManager.findFragmentByTag(tabKeys[3]) as BaseFragment
    )

    private fun showTab(position: Int) {
        childFragmentManager.beginTransaction()
                .detach(tabs[tabKeys[0]])
                .detach(tabs[tabKeys[1]])
                .detach(tabs[tabKeys[2]])
                .detach(tabs[tabKeys[3]])
                .attach(tabs[tabKeys[position]])
                .commit()
    }

    override fun onBackPressed() = presenter.onBackPressed()
}
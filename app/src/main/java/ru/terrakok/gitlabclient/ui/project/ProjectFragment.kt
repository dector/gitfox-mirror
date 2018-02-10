package ru.terrakok.gitlabclient.ui.project

import android.os.Bundle
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter
import kotlinx.android.synthetic.main.fragment_project.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.extension.color
import ru.terrakok.gitlabclient.toothpick.DI
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import ru.terrakok.gitlabclient.ui.project.info.ProjectInfoFragment
import toothpick.Toothpick

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 10.02.18.
 */
class ProjectFragment : BaseFragment(), ProjectView {

    override val layoutRes: Int = R.layout.fragment_project

    private lateinit var tabs: HashMap<String, BaseFragment>
    private val tabKeys = listOf(
            tabIdToFragmentTag(R.id.tab_info),
            tabIdToFragmentTag(R.id.tab_issue),
            tabIdToFragmentTag(R.id.tab_merge_request)
    )

    private fun tabIdToFragmentTag(id: Int) = "tab_$id"

    @InjectPresenter
    lateinit var presenter: ProjectPresenter

    @ProvidePresenter
    fun providePresenter(): ProjectPresenter {
        return Toothpick
                .openScope(DI.PROJECT_SCOPE)
                .getInstance(ProjectPresenter::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (savedInstanceState == null) {
            tabs = createNewFragments()
            childFragmentManager.beginTransaction()
                    .add(R.id.projectScreenContainer, tabs[tabKeys[0]], tabKeys[0])
                    .commit()
            showTab(0)
            bottomBar.setCurrentItem(0, false)
        } else {
            tabs = findFragments()
        }
        AHBottomNavigationAdapter(activity, R.menu.project_bottom_menu).apply {
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
        toolbar.setNavigationOnClickListener { presenter.onBackPressed() }
    }

    private fun createNewFragments(): HashMap<String, BaseFragment> = hashMapOf(
            tabKeys[0] to ProjectInfoFragment()
    )

    private fun findFragments(): HashMap<String, BaseFragment> = hashMapOf(
            tabKeys[0] to childFragmentManager.findFragmentByTag(tabKeys[0]) as BaseFragment
    )

    private fun showTab(position: Int) {
        childFragmentManager.beginTransaction()
                .detach(tabs[tabKeys[0]])
                .attach(tabs[tabKeys[position]])
                .commit()
    }

    override fun setProjectName(name: String) {
        toolbar.title = name
    }
}
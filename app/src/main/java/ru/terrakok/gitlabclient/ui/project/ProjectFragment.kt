package ru.terrakok.gitlabclient.ui.project

import android.os.Bundle
import android.support.v4.view.ViewCompat
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter
import kotlinx.android.synthetic.main.fragment_project.*
import ru.terrakok.cicerone.android.support.SupportAppScreen
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.extension.color
import ru.terrakok.gitlabclient.extension.shareText
import ru.terrakok.gitlabclient.extension.showSnackMessage
import ru.terrakok.gitlabclient.presentation.project.ProjectPresenter
import ru.terrakok.gitlabclient.presentation.project.ProjectView
import ru.terrakok.gitlabclient.ui.global.BaseFragment

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 10.02.18.
 */
class ProjectFragment : BaseFragment(), ProjectView {
    override val layoutRes: Int = R.layout.fragment_project

    private val currentTabFragment: BaseFragment?
        get() = childFragmentManager.fragments.firstOrNull { !it.isHidden } as? BaseFragment

    private var shareUrl: String? = null

    @InjectPresenter
    lateinit var presenter: ProjectPresenter

    @ProvidePresenter
    fun providePresenter(): ProjectPresenter =
        scope.getInstance(ProjectPresenter::class.java)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        toolbar.apply {
            setNavigationOnClickListener { onBackPressed() }
            inflateMenu(R.menu.share_menu)
            setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.shareAction -> shareText(shareUrl)
                }
                true
            }
        }
        AHBottomNavigationAdapter(activity, R.menu.project_bottom_menu).apply {
            setupWithBottomNavigation(bottomBar)
        }
        with(bottomBar) {
            accentColor = context.color(R.color.colorPrimary)
            inactiveColor = context.color(R.color.silver)

            setOnTabSelectedListener { position, wasSelected ->
                if (!wasSelected) selectTab(
                    when (position) {
                        0 -> Screens.ProjectInfoContainer
                        1 -> Screens.ProjectIssuesContainer
                        2 -> Screens.ProjectMergeRequestsContainer
                        else -> Screens.ProjectLabels
                    }
                )
                true
            }
        }

        selectTab(
            when (currentTabFragment?.tag) {
                Screens.ProjectInfoContainer.screenKey -> Screens.ProjectInfoContainer
                Screens.ProjectIssuesContainer.screenKey -> Screens.ProjectIssuesContainer
                Screens.ProjectMergeRequestsContainer.screenKey -> Screens.ProjectMergeRequestsContainer
                Screens.ProjectLabels.screenKey -> Screens.ProjectLabels
                else -> Screens.ProjectInfoContainer
            }
        )
    }

    private fun selectTab(tab: SupportAppScreen) {
        val currentFragment = currentTabFragment
        val newFragment = childFragmentManager.findFragmentByTag(tab.screenKey)

        if (currentFragment != null && newFragment != null && currentFragment == newFragment) return

        childFragmentManager.beginTransaction().apply {
            if (newFragment == null) add(R.id.projectMainContainer, tab.fragment, tab.screenKey)

            currentFragment?.let {
                hide(it)
                it.userVisibleHint = false
            }
            newFragment?.let {
                show(it)
                it.userVisibleHint = true
            }
        }.commitNow()
        setToolbarElevation(tab)
    }


    private fun setToolbarElevation(tab: SupportAppScreen) {
        if (tab is Screens.ProjectLabels) {
            ViewCompat.setElevation(
                toolbar,
                resources.getDimensionPixelSize(R.dimen.toolbar_elevation).toFloat()
            )
        } else {
            ViewCompat.setElevation(toolbar, 0f)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        presenter.onBackPressed()
    }

    override fun setTitle(title: String, shareUrl: String?) {
        toolbar.title = title
        this.shareUrl = shareUrl
    }

    override fun showBlockingProgress(show: Boolean) {
        showProgressDialog(show)
    }

    override fun showMessage(message: String) {
        showSnackMessage(message)
    }
}
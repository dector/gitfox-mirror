package ru.terrakok.gitlabclient.ui.project

import android.os.Bundle
import android.util.TypedValue
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter
import kotlinx.android.synthetic.main.fragment_project.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.extension.color
import ru.terrakok.gitlabclient.extension.shareText
import ru.terrakok.gitlabclient.model.system.flow.FlowRouter
import ru.terrakok.gitlabclient.toothpick.DI
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import toothpick.Toothpick
import javax.inject.Inject

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 10.02.18.
 */
class ProjectFragment : BaseFragment(), ProjectInfoFragment.ProjectInfoToolbar {
    override val layoutRes: Int = R.layout.fragment_project

    private val currentTabFragment: BaseFragment?
        get() = childFragmentManager.fragments.firstOrNull { !it.isHidden } as? BaseFragment

    @Inject
    lateinit var router: FlowRouter

    private var shareUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Toothpick.inject(this, Toothpick.openScope(DI.PROJECT_FLOW_SCOPE))
        super.onCreate(savedInstanceState)
    }

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
                        0 -> Screens.PROJECT_INFO_SCREEN
                        1 -> Screens.PROJECT_ISSUES_CONTAINER_SCREEN
                        else -> Screens.PROJECT_MR_CONTAINER_SCREEN
                    }
                )
                true
            }
        }

        selectTab(currentTabFragment?.tag ?: Screens.PROJECT_INFO_SCREEN)
    }

    private fun selectTab(tab: String) {
        val currentFragment = currentTabFragment
        val newFragment = childFragmentManager.findFragmentByTag(tab)

        if (currentFragment != null && newFragment != null && currentFragment == newFragment) return

        childFragmentManager.beginTransaction().apply {
            if (newFragment == null) add(R.id.projectMainContainer, createTabFragment(tab), tab)

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

    private fun setToolbarElevation(tab: String) {
        if (tab == Screens.PROJECT_INFO_SCREEN) {
            toolbar.elevation = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                8f,
                resources.displayMetrics
            )
        } else {
            toolbar.elevation = 0f
        }
    }

    private fun createTabFragment(tab: String) =
        Screens.createFragment(tab) ?: throw RuntimeException("Unknown tab $tab")

    override fun onBackPressed() {
        super.onBackPressed()

        router.exit()
    }

    override fun setTitle(title: String) {
        toolbar.title = title
    }

    override fun setShareUrl(url: String?) {
        shareUrl = url
    }
}
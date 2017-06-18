package ru.terrakok.gitlabclient.ui.main

import android.os.Bundle
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.fragment_main.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.presentation.main.MainPresenter
import ru.terrakok.gitlabclient.presentation.main.MainView
import ru.terrakok.gitlabclient.ui.gitlab_issues.MyActivityFragment
import ru.terrakok.gitlabclient.ui.gitlab_issues.MyMergeRequestsFragment
import ru.terrakok.gitlabclient.ui.gitlab_issues.MyTodosFragment
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import ru.terrakok.gitlabclient.ui.my.issues.MyIssuesFragment

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 02.04.17
 */
class MainFragment : BaseFragment(), MainView {
    override val layoutRes = R.layout.fragment_main

    @InjectPresenter lateinit var presenter: MainPresenter

    private val fragments = hashMapOf(
            R.id.tab_activity to MyActivityFragment(),
            R.id.tab_issue to MyIssuesFragment(),
            R.id.tab_merge to MyMergeRequestsFragment(),
            R.id.tab_todo to MyTodosFragment()
    )

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        childFragmentManager.beginTransaction()
                .add(R.id.mainScreenContainer, fragments[R.id.tab_activity])
                .add(R.id.mainScreenContainer, fragments[R.id.tab_issue])
                .add(R.id.mainScreenContainer, fragments[R.id.tab_merge])
                .add(R.id.mainScreenContainer, fragments[R.id.tab_todo])
                .commit()

        bottomBar.setOnTabSelectListener { showTab(it) }
    }

    private fun showTab(id: Int) {
        childFragmentManager.beginTransaction()
                .detach(fragments[R.id.tab_activity])
                .detach(fragments[R.id.tab_issue])
                .detach(fragments[R.id.tab_merge])
                .detach(fragments[R.id.tab_todo])
                .attach(fragments[id])
                .commit()
    }

    override fun onBackPressed() = presenter.onBackPressed()
}
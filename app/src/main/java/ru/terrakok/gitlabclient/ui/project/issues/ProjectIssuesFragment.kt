package ru.terrakok.gitlabclient.ui.project.issues

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.layout_base_list.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.app.target.TargetHeader
import ru.terrakok.gitlabclient.entity.issue.IssueState
import ru.terrakok.gitlabclient.extension.showSnackMessage
import ru.terrakok.gitlabclient.extension.visible
import ru.terrakok.gitlabclient.presentation.project.issues.ProjectIssuesPresenter
import ru.terrakok.gitlabclient.presentation.project.issues.ProjectIssuesView
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import ru.terrakok.gitlabclient.ui.my.TargetsAdapter
import toothpick.Scope
import toothpick.config.Module

/**
 * @author Eugene Shapovalov (CraggyHaggy). Date: 27.08.18
 */
class ProjectIssuesFragment : BaseFragment(), ProjectIssuesView {
    override val layoutRes = R.layout.fragment_project_issues

    override fun installModules(scope: Scope) {
        scope.installModules(object : Module() {
            init {
                bind(IssueState::class.java)
                    .toInstance(arguments!!.getSerializable(ARG_ISSUE_STATE) as IssueState)
            }
        })
    }

    @InjectPresenter
    lateinit var presenter: ProjectIssuesPresenter

    @ProvidePresenter
    fun providePresenter(): ProjectIssuesPresenter =
        scope.getInstance(ProjectIssuesPresenter::class.java)

    private val adapter: TargetsAdapter by lazy {
        TargetsAdapter(
            { presenter.onUserClick(it) },
            { presenter.onIssueClick(it) },
            { presenter.loadNextIssuesPage() }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments?.getSerializable(ARG_ISSUE_STATE) == null) {
            throw IllegalArgumentException("Provide issue state as args.")
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = this@ProjectIssuesFragment.adapter
        }

        swipeToRefresh.setOnRefreshListener { presenter.refreshIssues() }
        emptyView.setRefreshListener { presenter.refreshIssues() }
    }

    override fun showRefreshProgress(show: Boolean) {
        postViewAction { swipeToRefresh.isRefreshing = show }
    }

    override fun showEmptyProgress(show: Boolean) {
        fullscreenProgressView.visible(show)

        // Trick for disable and hide swipeToRefresh on fullscreen progress
        swipeToRefresh.visible(!show)
        postViewAction { swipeToRefresh.isRefreshing = false }
    }

    override fun showPageProgress(show: Boolean) {
        postViewAction { adapter.showProgress(show) }
    }

    override fun showEmptyView(show: Boolean) {
        emptyView.apply { if (show) showEmptyData() else hide() }
    }

    override fun showEmptyError(show: Boolean, message: String?) {
        emptyView.apply { if (show) showEmptyError(message) else hide() }
    }

    override fun showIssues(show: Boolean, issues: List<TargetHeader>) {
        recyclerView.visible(show)
        postViewAction { adapter.setData(issues) }
    }

    override fun showMessage(message: String) {
        showSnackMessage(message)
    }

    companion object {
        private const val ARG_ISSUE_STATE = "arg issue state"

        fun create(issueState: IssueState) =
            ProjectIssuesFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_ISSUE_STATE, issueState)
                }
            }
    }
}
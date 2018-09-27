package ru.terrakok.gitlabclient.ui.project.issues

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.layout_base_list.*
import kotlinx.android.synthetic.main.layout_zero.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.app.target.TargetHeader
import ru.terrakok.gitlabclient.entity.issue.IssueState
import ru.terrakok.gitlabclient.extension.argument
import ru.terrakok.gitlabclient.extension.showSnackMessage
import ru.terrakok.gitlabclient.extension.visible
import ru.terrakok.gitlabclient.presentation.project.issues.ProjectIssuesPresenter
import ru.terrakok.gitlabclient.presentation.project.issues.ProjectIssuesView
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import ru.terrakok.gitlabclient.ui.global.ZeroViewHolder
import ru.terrakok.gitlabclient.ui.my.TargetsAdapter
import toothpick.Toothpick
import toothpick.config.Module

/**
 * @author Eugene Shapovalov (CraggyHaggy). Date: 27.08.18
 */
class ProjectIssuesFragment : BaseFragment(), ProjectIssuesView {
    override val layoutRes = R.layout.fragment_project_issues
    private val scopeName: String? by argument(ARG_SCOPE_NAME)

    @InjectPresenter
    lateinit var presenter: ProjectIssuesPresenter

    @ProvidePresenter
    fun providePresenter(): ProjectIssuesPresenter {
        val subScopeName = "ProjectIssuesScope_${hashCode()}"
        val scope = Toothpick.openScopes(scopeName, subScopeName)
        scope.installModules(object : Module() {
            init {
                bind(IssueState::class.java)
                    .toInstance(arguments!!.getSerializable(ARG_ISSUE_STATE) as IssueState)
            }
        })

        return scope.getInstance(ProjectIssuesPresenter::class.java).also {
            Toothpick.closeScope(subScopeName)
        }
    }

    private val adapter: TargetsAdapter by lazy {
        TargetsAdapter(
            { presenter.onUserClick(it) },
            { presenter.onIssueClick(it) },
            { presenter.loadNextIssuesPage() }
        )
    }
    private var zeroViewHolder: ZeroViewHolder? = null

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
        zeroViewHolder = ZeroViewHolder(zeroLayout, { presenter.refreshIssues() })
    }

    override fun showRefreshProgress(show: Boolean) {
        postViewAction { swipeToRefresh.isRefreshing = show }
    }

    override fun showEmptyProgress(show: Boolean) {
        fullscreenProgressView.visible(show)

        //trick for disable and hide swipeToRefresh on fullscreen progress
        swipeToRefresh.visible(!show)
        postViewAction { swipeToRefresh.isRefreshing = false }
    }

    override fun showPageProgress(show: Boolean) {
        postViewAction { adapter.showProgress(show) }
    }

    override fun showEmptyView(show: Boolean) {
        if (show) zeroViewHolder?.showEmptyData()
        else zeroViewHolder?.hide()
    }

    override fun showEmptyError(show: Boolean, message: String?) {
        if (show) zeroViewHolder?.showEmptyError(message)
        else zeroViewHolder?.hide()
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
        private const val ARG_SCOPE_NAME = "arg_scope_name"

        fun create(issueState: IssueState, scope: String) =
            ProjectIssuesFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_ISSUE_STATE, issueState)
                    putString(ARG_SCOPE_NAME, scope)
                }
            }
    }
}
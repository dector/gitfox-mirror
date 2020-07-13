package ru.terrakok.gitlabclient.ui.project.issues

import android.os.Bundle
import gitfox.entity.IssueState
import gitfox.entity.app.target.TargetHeader
import kotlinx.android.synthetic.main.fragment_project_issues.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.presentation.global.Paginator
import ru.terrakok.gitlabclient.presentation.project.issues.ProjectIssuesPresenter
import ru.terrakok.gitlabclient.presentation.project.issues.ProjectIssuesView
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import ru.terrakok.gitlabclient.ui.global.list.PaginalAdapter
import ru.terrakok.gitlabclient.ui.global.list.TargetHeaderConfidentialAdapterDelegate
import ru.terrakok.gitlabclient.ui.global.list.TargetHeaderPublicAdapterDelegate
import ru.terrakok.gitlabclient.ui.global.list.isSame
import ru.terrakok.gitlabclient.util.showSnackMessage
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

    private val adapter by lazy { PaginalAdapter(
            { presenter.loadNextIssuesPage() },
            { o, n ->
                if (o is TargetHeader.Public && n is TargetHeader.Public) {
                    o.isSame(n)
                } else false
            },
            TargetHeaderPublicAdapterDelegate(mvpDelegate) { presenter.onIssueClick(it) },
            TargetHeaderConfidentialAdapterDelegate()
    ) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments?.getSerializable(ARG_ISSUE_STATE) == null) {
            throw IllegalArgumentException("Provide issue state as args.")
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        paginalRenderView.init(
            { presenter.refreshIssues() },
            adapter
        )
    }

    override fun renderPaginatorState(state: Paginator.State) {
        paginalRenderView.render(state)
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

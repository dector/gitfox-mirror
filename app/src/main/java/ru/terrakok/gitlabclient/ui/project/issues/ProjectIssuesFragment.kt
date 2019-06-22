package ru.terrakok.gitlabclient.ui.project.issues

import android.os.Bundle
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_project_issues.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.app.target.TargetHeader
import ru.terrakok.gitlabclient.entity.issue.IssueState
import ru.terrakok.gitlabclient.extension.showSnackMessage
import ru.terrakok.gitlabclient.presentation.global.Paginator
import ru.terrakok.gitlabclient.presentation.project.issues.ProjectIssuesPresenter
import ru.terrakok.gitlabclient.presentation.project.issues.ProjectIssuesView
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import ru.terrakok.gitlabclient.ui.global.list.TargetHeaderConfidentialAdapterDelegate
import ru.terrakok.gitlabclient.ui.global.list.TargetHeaderPublicAdapterDelegate
import ru.terrakok.gitlabclient.ui.global.list.isSame
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
            { presenter.loadNextIssuesPage() },
            { o, n ->
                if (o is TargetHeader.Public && n is TargetHeader.Public) {
                    o.isSame(n)
                } else false
            },
            TargetHeaderPublicAdapterDelegate { presenter.onIssueClick(it) },
            TargetHeaderConfidentialAdapterDelegate()
        )
    }

    override fun renderPaginatorState(state: Paginator.State<TargetHeader>) {
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
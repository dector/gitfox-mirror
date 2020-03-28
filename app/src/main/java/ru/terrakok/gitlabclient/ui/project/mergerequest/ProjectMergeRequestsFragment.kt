package ru.terrakok.gitlabclient.ui.project.mergerequest

import android.os.Bundle
import gitfox.entity.MergeRequestState
import gitfox.entity.app.target.TargetHeader
import kotlinx.android.synthetic.main.fragment_project_merge_requests.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.presentation.global.Paginator
import ru.terrakok.gitlabclient.presentation.project.mergerequest.ProjectMergeRequestsPresenter
import ru.terrakok.gitlabclient.presentation.project.mergerequest.ProjectMergeRequestsView
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import ru.terrakok.gitlabclient.ui.global.list.PaginalAdapter
import ru.terrakok.gitlabclient.ui.global.list.TargetHeaderConfidentialAdapterDelegate
import ru.terrakok.gitlabclient.ui.global.list.TargetHeaderPublicAdapterDelegate
import ru.terrakok.gitlabclient.ui.global.list.isSame
import ru.terrakok.gitlabclient.util.showSnackMessage
import toothpick.Scope
import toothpick.config.Module

/**
 * @author Eugene Shapovalov (CraggyHaggy). Date: 28.08.18
 */
class ProjectMergeRequestsFragment : BaseFragment(), ProjectMergeRequestsView {
    override val layoutRes = R.layout.fragment_project_merge_requests

    override fun installModules(scope: Scope) {
        scope.installModules(object : Module() {
            init {
                bind(MergeRequestState::class.java)
                    .toInstance(arguments!!.getSerializable(ARG_MERGE_REQUEST_STATE) as MergeRequestState)
            }
        })
    }

    @InjectPresenter
    lateinit var presenter: ProjectMergeRequestsPresenter

    @ProvidePresenter
    fun providePresenter(): ProjectMergeRequestsPresenter =
        scope.getInstance(ProjectMergeRequestsPresenter::class.java)

    private val adapter by lazy { PaginalAdapter(
            { presenter.loadNextMergeRequestsPage() },
            { o, n ->
                if (o is TargetHeader.Public && n is TargetHeader.Public) {
                    o.isSame(n)
                } else false
            },
            TargetHeaderPublicAdapterDelegate { presenter.onMergeRequestClick(it) },
            TargetHeaderConfidentialAdapterDelegate()
    ) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments?.getSerializable(ARG_MERGE_REQUEST_STATE) == null) {
            throw IllegalArgumentException("Provide merge request state as args.")
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        paginalRenderView.init(
            { presenter.refreshMergeRequests() },
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
        private const val ARG_MERGE_REQUEST_STATE = "arg merge request state"

        fun create(mergeRequestState: MergeRequestState) =
            ProjectMergeRequestsFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_MERGE_REQUEST_STATE, mergeRequestState)
                }
            }
    }
}

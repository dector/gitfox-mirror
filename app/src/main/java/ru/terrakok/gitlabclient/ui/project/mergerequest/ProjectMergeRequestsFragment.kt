package ru.terrakok.gitlabclient.ui.project.mergerequest

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.layout_base_list.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.app.target.TargetHeader
import ru.terrakok.gitlabclient.entity.mergerequest.MergeRequestState
import ru.terrakok.gitlabclient.extension.showSnackMessage
import ru.terrakok.gitlabclient.extension.visible
import ru.terrakok.gitlabclient.presentation.project.mergerequest.ProjectMergeRequestsPresenter
import ru.terrakok.gitlabclient.presentation.project.mergerequest.ProjectMergeRequestsView
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import ru.terrakok.gitlabclient.ui.my.TargetsAdapter
import toothpick.Scope
import toothpick.config.Module

/**
 * @author Eugene Shapovalov (CraggyHaggy). Date: 28.08.18
 */
class ProjectMergeRequestsFragment : BaseFragment(), ProjectMergeRequestsView {
    override val layoutRes = R.layout.fragment_my_merge_requests

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

    private val adapter: TargetsAdapter by lazy {
        TargetsAdapter(
            { presenter.onUserClick(it) },
            { presenter.onMergeRequestClick(it) },
            { presenter.loadNextMergeRequestsPage() }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments?.getSerializable(ARG_MERGE_REQUEST_STATE) == null) {
            throw IllegalArgumentException("Provide merge request state as args.")
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = this@ProjectMergeRequestsFragment.adapter
        }

        swipeToRefresh.setOnRefreshListener { presenter.refreshMergeRequests() }
        emptyView.setRefreshListener { presenter.refreshMergeRequests() }
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

    override fun showMergeRequests(show: Boolean, mergeRequests: List<TargetHeader>) {
        recyclerView.visible(show)
        postViewAction { adapter.setData(mergeRequests) }
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
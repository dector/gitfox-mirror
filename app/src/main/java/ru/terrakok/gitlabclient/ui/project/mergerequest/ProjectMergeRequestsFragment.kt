package ru.terrakok.gitlabclient.ui.project.mergerequest

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.layout_base_list.*
import kotlinx.android.synthetic.main.layout_zero.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.app.target.TargetHeader
import ru.terrakok.gitlabclient.entity.mergerequest.MergeRequestState
import ru.terrakok.gitlabclient.extension.showSnackMessage
import ru.terrakok.gitlabclient.extension.visible
import ru.terrakok.gitlabclient.presentation.project.mergerequest.ProjectMergeRequestsPresenter
import ru.terrakok.gitlabclient.presentation.project.mergerequest.ProjectMergeRequestsView
import ru.terrakok.gitlabclient.toothpick.DI
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import ru.terrakok.gitlabclient.ui.global.ZeroViewHolder
import ru.terrakok.gitlabclient.ui.my.TargetsAdapter
import toothpick.Toothpick
import toothpick.config.Module

/**
 * @author Eugene Shapovalov (CraggyHaggy). Date: 28.08.18
 */
class ProjectMergeRequestsFragment : BaseFragment(), ProjectMergeRequestsView {
    override val layoutRes = R.layout.fragment_my_merge_requests

    @InjectPresenter
    lateinit var presenter: ProjectMergeRequestsPresenter

    @ProvidePresenter
    fun providePresenter(): ProjectMergeRequestsPresenter {
        val scopeName = "ProjectMergeRequestsScope_${hashCode()}"
        val scope = Toothpick.openScopes(DI.PROJECT_FLOW_SCOPE, scopeName)
        scope.installModules(object : Module() {
            init {
                bind(MergeRequestState::class.java)
                    .toInstance(arguments!!.getSerializable(ARG_MERGE_REQUEST_STATE) as MergeRequestState)
            }
        })

        return scope.getInstance(ProjectMergeRequestsPresenter::class.java).also {
            Toothpick.closeScope(scopeName)
        }
    }

    private val adapter: TargetsAdapter by lazy {
        TargetsAdapter(
            { presenter.onUserClick(it) },
            { presenter.onMergeRequestClick(it) },
            { presenter.loadNextMergeRequestsPage() }
        )
    }
    private var zeroViewHolder: ZeroViewHolder? = null

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
        zeroViewHolder = ZeroViewHolder(zeroLayout, { presenter.refreshMergeRequests() })
    }

    override fun showRefreshProgress(show: Boolean) {
        swipeToRefresh.post { swipeToRefresh.isRefreshing = show }
    }

    override fun showEmptyProgress(show: Boolean) {
        fullscreenProgressView.visible(show)

        //trick for disable and hide swipeToRefresh on fullscreen progress
        swipeToRefresh.visible(!show)
        swipeToRefresh.post { swipeToRefresh.isRefreshing = false }
    }

    override fun showPageProgress(show: Boolean) {
        recyclerView.post { adapter.showProgress(show) }
    }

    override fun showEmptyView(show: Boolean) {
        if (show) zeroViewHolder?.showEmptyData()
        else zeroViewHolder?.hide()
    }

    override fun showEmptyError(show: Boolean, message: String?) {
        if (show) zeroViewHolder?.showEmptyError(message)
        else zeroViewHolder?.hide()
    }

    override fun showMergeRequests(show: Boolean, mergeRequests: List<TargetHeader>) {
        recyclerView.visible(show)
        recyclerView.post { adapter.setData(mergeRequests) }
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
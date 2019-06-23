package ru.terrakok.gitlabclient.ui.project.members

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.layout_base_list.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.Member
import ru.terrakok.gitlabclient.extension.showSnackMessage
import ru.terrakok.gitlabclient.extension.visible
import ru.terrakok.gitlabclient.presentation.project.members.ProjectMembersPresenter
import ru.terrakok.gitlabclient.presentation.project.members.ProjectMembersView
import ru.terrakok.gitlabclient.ui.global.BaseFragment

/**
 * @author Valentin Logvinovitch (glvvl) on 28.02.19.
 */
class ProjectMembersFragment : BaseFragment(), ProjectMembersView {

    override val layoutRes = R.layout.fragment_project_members

    @InjectPresenter
    lateinit var presenter: ProjectMembersPresenter

    @ProvidePresenter
    fun providePresenter(): ProjectMembersPresenter =
            scope.getInstance(ProjectMembersPresenter::class.java)

    private val adapter by lazy {
        MembersAdapter(
            { presenter.onMemberClick(it) },
            { presenter.loadNextMembersPage() }
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = this@ProjectMembersFragment.adapter
        }

        swipeToRefresh.setOnRefreshListener { presenter.refreshMembers() }
        emptyView.setRefreshListener { presenter.refreshMembers() }
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

    override fun showMembers(show: Boolean, members: List<Member>) {
        recyclerView.visible(show)
        postViewAction { adapter.setData(members) }
    }

    override fun showMessage(message: String) {
        showSnackMessage(message)
    }

    override fun onBackPressed() {
        presenter.onBackPressed()
    }
}
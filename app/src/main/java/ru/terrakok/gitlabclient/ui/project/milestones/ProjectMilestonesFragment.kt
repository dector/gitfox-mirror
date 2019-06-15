package ru.terrakok.gitlabclient.ui.project.milestones

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.layout_base_list.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.milestone.Milestone
import ru.terrakok.gitlabclient.extension.showSnackMessage
import ru.terrakok.gitlabclient.extension.visible
import ru.terrakok.gitlabclient.presentation.project.milestones.ProjectMilestonesPresenter
import ru.terrakok.gitlabclient.presentation.project.milestones.ProjectMilestonesView
import ru.terrakok.gitlabclient.ui.global.BaseFragment

/**
 * @author Valentin Logvinovitch (glvvl) on 17.12.18.
 */
class ProjectMilestonesFragment : BaseFragment(), ProjectMilestonesView {

    override val layoutRes = R.layout.fragment_project_milestones

    @InjectPresenter
    lateinit var presenter: ProjectMilestonesPresenter

    @ProvidePresenter
    fun providePresenter(): ProjectMilestonesPresenter =
        scope.getInstance(ProjectMilestonesPresenter::class.java)

    private val adapter: MilestonesAdapter by lazy {
        MilestonesAdapter(
            { presenter.onMilestoneClicked(it) },
            { presenter.loadNextMilestonesPage() }
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = this@ProjectMilestonesFragment.adapter
        }

        swipeToRefresh.setOnRefreshListener { presenter.refreshMilestones() }
        emptyView.setRefreshListener { presenter.refreshMilestones() }
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

    override fun showMilestones(show: Boolean, milestones: List<Milestone>) {
        recyclerView.visible(show)
        postViewAction { adapter.setData(milestones) }
    }

    override fun showMessage(message: String) {
        showSnackMessage(message)
    }
}
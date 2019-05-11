package ru.terrakok.gitlabclient.ui.project.milestones

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.layout_base_list.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.milestone.Milestone
import ru.terrakok.gitlabclient.entity.milestone.MilestoneState
import ru.terrakok.gitlabclient.extension.showSnackMessage
import ru.terrakok.gitlabclient.extension.visible
import ru.terrakok.gitlabclient.presentation.project.milestones.ProjectMilestonesPresenter
import ru.terrakok.gitlabclient.presentation.project.milestones.ProjectMilestonesView
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import toothpick.Scope
import toothpick.config.Module

/**
 * @author Valentin Logvinovitch (glvvl) on 17.12.18.
 */
class ProjectMilestonesFragment : BaseFragment(), ProjectMilestonesView {

    override val layoutRes = R.layout.fragment_project_milestones

    override fun installModules(scope: Scope) {
        scope.installModules(object : Module() {
            init {
                bind(MilestoneState::class.java)
                    .toInstance(arguments!!.getSerializable(ARG_MILESTONE_STATE) as MilestoneState)
            }
        })
    }

    @InjectPresenter
    lateinit var presenter: ProjectMilestonesPresenter

    @ProvidePresenter
    fun providePresenter(): ProjectMilestonesPresenter =
        scope.getInstance(ProjectMilestonesPresenter::class.java)

    private val adapter: MilestonesAdapter by lazy {
        MilestonesAdapter(
            { presenter.onMilestoneClick(it) },
            { presenter.loadNextMilestonesPage() }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments?.getSerializable(ARG_MILESTONE_STATE) == null) {
            throw IllegalArgumentException("Provide milestone state as args.")
        }
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

        //trick for disable and hide swipeToRefresh on fullscreen progress
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

    companion object {
        private const val ARG_MILESTONE_STATE = "arg milestone state"

        fun create(milestoneState: MilestoneState) =
            ProjectMilestonesFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_MILESTONE_STATE, milestoneState)
                }
            }
    }
}
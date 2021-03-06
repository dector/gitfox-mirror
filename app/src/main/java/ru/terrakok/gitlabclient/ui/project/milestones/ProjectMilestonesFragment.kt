package ru.terrakok.gitlabclient.ui.project.milestones

import android.os.Bundle
import gitfox.entity.Milestone
import kotlinx.android.synthetic.main.fragment_project_milestones.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.presentation.global.Paginator
import ru.terrakok.gitlabclient.presentation.project.milestones.ProjectMilestonesPresenter
import ru.terrakok.gitlabclient.presentation.project.milestones.ProjectMilestonesView
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import ru.terrakok.gitlabclient.ui.global.list.MilestonesAdapterDelegate
import ru.terrakok.gitlabclient.ui.global.list.PaginalAdapter
import ru.terrakok.gitlabclient.ui.global.list.isSame
import ru.terrakok.gitlabclient.util.showSnackMessage

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

    private val adapter by lazy { PaginalAdapter(
            { presenter.loadNextMilestonesPage() },
            { o, n ->
                if (o is Milestone && n is Milestone) {
                    o.isSame(n)
                } else false
            },
            MilestonesAdapterDelegate { presenter.onMilestoneClicked(it) }
    ) }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        paginalRenderView.init(
            { presenter.refreshMilestones() },
            adapter
        )
    }

    override fun renderPaginatorState(state: Paginator.State) {
        paginalRenderView.render(state)
    }

    override fun showMessage(message: String) {
        showSnackMessage(message)
    }
}

package ru.terrakok.gitlabclient.ui.project.info

import android.os.Bundle
import gitfox.entity.app.target.TargetHeader
import kotlinx.android.synthetic.main.fragment_project_events.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.presentation.global.Paginator
import ru.terrakok.gitlabclient.presentation.project.events.ProjectEventsPresenter
import ru.terrakok.gitlabclient.presentation.project.events.ProjectEventsView
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import ru.terrakok.gitlabclient.ui.global.list.PaginalAdapter
import ru.terrakok.gitlabclient.ui.global.list.TargetHeaderConfidentialAdapterDelegate
import ru.terrakok.gitlabclient.ui.global.list.TargetHeaderPublicAdapterDelegate
import ru.terrakok.gitlabclient.ui.global.list.isSame
import ru.terrakok.gitlabclient.util.showSnackMessage

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 13.06.17
 */
class ProjectEventsFragment : BaseFragment(), ProjectEventsView {
    override val layoutRes = R.layout.fragment_project_events

    @InjectPresenter
    lateinit var presenter: ProjectEventsPresenter

    @ProvidePresenter
    fun providePresenter(): ProjectEventsPresenter =
        scope.getInstance(ProjectEventsPresenter::class.java)

    private val adapter by lazy { PaginalAdapter(
            { presenter.loadNextEventsPage() },
            { o, n ->
                if (o is TargetHeader.Public && n is TargetHeader.Public) {
                    o.isSame(n)
                } else false
            },
            TargetHeaderPublicAdapterDelegate { presenter.onItemClick(it) },
            TargetHeaderConfidentialAdapterDelegate()
    ) }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        paginalRenderView.init(
            { presenter.refreshEvents() },
            adapter
        )
    }

    override fun renderPaginatorState(state: Paginator.State) {
        paginalRenderView.render(state)
    }

    override fun showMessage(message: String) {
        showSnackMessage(message)
    }

    override fun onBackPressed() {
        presenter.onBackPressed()
    }
}

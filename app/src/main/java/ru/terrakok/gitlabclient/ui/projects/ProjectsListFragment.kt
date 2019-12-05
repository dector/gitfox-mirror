package ru.terrakok.gitlabclient.ui.projects

import android.os.Bundle
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_projects.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.di.PrimitiveWrapper
import ru.terrakok.gitlabclient.di.ProjectListMode
import ru.terrakok.gitlabclient.entity.Project
import ru.terrakok.gitlabclient.presentation.global.Paginator
import ru.terrakok.gitlabclient.presentation.projects.ProjectsListPresenter
import ru.terrakok.gitlabclient.presentation.projects.ProjectsListView
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import ru.terrakok.gitlabclient.ui.global.list.PaginalAdapter
import ru.terrakok.gitlabclient.ui.global.list.ProjectAdapterDelegate
import ru.terrakok.gitlabclient.ui.global.list.isSame
import ru.terrakok.gitlabclient.util.showSnackMessage
import toothpick.Scope
import toothpick.config.Module

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 29.03.17
 */
class ProjectsListFragment : BaseFragment(), ProjectsListView {
    override val layoutRes = R.layout.fragment_projects

    override fun installModules(scope: Scope) {
        scope.installModules(object : Module() {
            init {
                bind(PrimitiveWrapper::class.java)
                    .withName(ProjectListMode::class.java)
                    .toInstance(PrimitiveWrapper(arguments?.getInt(ARG_MODE)))
            }
        })
    }

    @InjectPresenter
    lateinit var presenter: ProjectsListPresenter

    @ProvidePresenter
    fun createPresenter(): ProjectsListPresenter =
        scope.getInstance(ProjectsListPresenter::class.java)

    private val adapter by lazy { PaginalAdapter(
            { presenter.loadNextProjectsPage() },
            { o, n ->
                if (o is Project && n is Project) {
                    o.isSame(n)
                } else false
            },
            ProjectAdapterDelegate { presenter.onProjectClicked(it.id) }
    ) }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        paginalRenderView.init(
            { presenter.refreshProjects() },
            adapter
        )
    }

    override fun renderPaginatorState(state: Paginator.State) {
        paginalRenderView.render(state)
    }

    override fun showMessage(message: String) {
        showSnackMessage(message)
    }

    override fun onBackPressed() = presenter.onBackPressed()

    companion object {
        private const val ARG_MODE = "plf_mode"

        fun create(mode: Int) = ProjectsListFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_MODE, mode)
            }
        }
    }
}
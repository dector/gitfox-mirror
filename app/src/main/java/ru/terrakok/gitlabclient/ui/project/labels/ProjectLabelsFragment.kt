package ru.terrakok.gitlabclient.ui.project.labels

import android.os.Bundle
import gitfox.entity.Label
import kotlinx.android.synthetic.main.fragment_project_labels.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.presentation.global.Paginator
import ru.terrakok.gitlabclient.presentation.project.labels.ProjectLabelsPresenter
import ru.terrakok.gitlabclient.presentation.project.labels.ProjectLabelsView
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import ru.terrakok.gitlabclient.ui.global.list.PaginalAdapter
import ru.terrakok.gitlabclient.util.showSnackMessage

/**
 * @author Maxim Myalkin (MaxMyalkin) on 15.12.2018.
 */
class ProjectLabelsFragment : BaseFragment(), ProjectLabelsView {

    override val layoutRes: Int = R.layout.fragment_project_labels

    @InjectPresenter
    lateinit var presenter: ProjectLabelsPresenter

    @ProvidePresenter
    fun providePresenter() = scope.getInstance(ProjectLabelsPresenter::class.java)

    private val adapter by lazy { PaginalAdapter(
            { presenter.loadNextLabelsPage() },
            { o, n ->
                if (o is Label && n is Label) {
                    o.isSame(n)
                } else false
            },
            ProjectLabelAdapterDelegate()
    ) }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        paginalRenderView.init(
            { presenter.refreshProjectLabels() },
            adapter
        )
    }

    override fun onBackPressed() {
        presenter.onBackPressed()
    }

    override fun renderPaginatorState(state: Paginator.State) {
        paginalRenderView.render(state)
    }

    override fun showMessage(message: String) {
        showSnackMessage(message)
    }
}

package ru.terrakok.gitlabclient.ui.project.labels

import android.os.Bundle
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_project_labels.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.Label
import ru.terrakok.gitlabclient.extension.showSnackMessage
import ru.terrakok.gitlabclient.presentation.global.Paginator
import ru.terrakok.gitlabclient.presentation.project.labels.ProjectLabelsPresenter
import ru.terrakok.gitlabclient.presentation.project.labels.ProjectLabelsView
import ru.terrakok.gitlabclient.ui.global.BaseFragment

/**
 * @author Maxim Myalkin (MaxMyalkin) on 15.12.2018.
 */
class ProjectLabelsFragment : BaseFragment(), ProjectLabelsView {

    override val layoutRes: Int = R.layout.fragment_project_labels

    @InjectPresenter
    lateinit var presenter: ProjectLabelsPresenter

    @ProvidePresenter
    fun providePresenter() = scope.getInstance(ProjectLabelsPresenter::class.java)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        paginalRenderView.init(
            { presenter.refreshProjectLabels() },
            { presenter.loadNextLabelsPage() },
            { o, n ->
                if (o is Label && n is Label) {
                    o.isSame(n)
                } else false
            },
            ProjectLabelAdapterDelegate()
        )
    }

    override fun onBackPressed() {
        presenter.onBackPressed()
    }

    override fun renderPaginatorState(state: Paginator.State<Label>) {
        paginalRenderView.render(state)
    }

    override fun showMessage(message: String) {
        showSnackMessage(message)
    }
}
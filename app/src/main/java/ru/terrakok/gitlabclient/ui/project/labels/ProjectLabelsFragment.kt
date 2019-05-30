package ru.terrakok.gitlabclient.ui.project.labels

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.layout_base_list.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.Label
import ru.terrakok.gitlabclient.extension.showSnackMessage
import ru.terrakok.gitlabclient.extension.visible
import ru.terrakok.gitlabclient.presentation.project.labels.ProjectLabelsPresenter
import ru.terrakok.gitlabclient.presentation.project.labels.ProjectLabelsView
import ru.terrakok.gitlabclient.ui.global.BaseFragment

/**
 * @author Maxim Myalkin (MaxMyalkin) on 15.12.2018.
 */
class ProjectLabelsFragment : BaseFragment(), ProjectLabelsView {

    override val layoutRes: Int = R.layout.fragment_project_labels

    private val adapter by lazy { ProjectLabelsAdapter { presenter.loadNextLabelsPage() } }

    @InjectPresenter
    lateinit var presenter: ProjectLabelsPresenter

    @ProvidePresenter
    fun providePresenter() = scope.getInstance(ProjectLabelsPresenter::class.java)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = this@ProjectLabelsFragment.adapter
        }

        swipeToRefresh.setOnRefreshListener { presenter.refreshProjectLabels() }
        emptyView.setRefreshListener { presenter.refreshProjectLabels() }
    }

    override fun onBackPressed() {
        presenter.onBackPressed()
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

    override fun showLabels(show: Boolean, list: List<Label>) {
        recyclerView.visible(show)
        postViewAction { adapter.setData(list) }
    }

    override fun showMessage(message: String) {
        showSnackMessage(message)
    }

}
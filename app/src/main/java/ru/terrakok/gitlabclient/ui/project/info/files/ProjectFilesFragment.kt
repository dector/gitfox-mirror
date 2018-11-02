package ru.terrakok.gitlabclient.ui.project.info.files

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.layout_base_list.*
import kotlinx.android.synthetic.main.layout_zero.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.app.ProjectFile
import ru.terrakok.gitlabclient.extension.argument
import ru.terrakok.gitlabclient.extension.showSnackMessage
import ru.terrakok.gitlabclient.extension.visible
import ru.terrakok.gitlabclient.presentation.project.files.ProjectFilesPresenter
import ru.terrakok.gitlabclient.presentation.project.files.ProjectFilesView
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import ru.terrakok.gitlabclient.ui.global.ZeroViewHolder
import toothpick.Toothpick

/**
 * @author Eugene Shapovalov (CraggyHaggy). Date: 02.11.18
 */
class ProjectFilesFragment : BaseFragment(), ProjectFilesView {
    override val layoutRes = R.layout.fragment_project_issues
    private val scopeName: String? by argument(ARG_SCOPE_NAME)

    @InjectPresenter
    lateinit var presenter: ProjectFilesPresenter

    @ProvidePresenter
    fun providePresenter(): ProjectFilesPresenter =
        Toothpick
            .openScope(scopeName)
            .getInstance(ProjectFilesPresenter::class.java)

    private val adapter: ProjectFilesAdapter by lazy {
        ProjectFilesAdapter(
            { presenter.onFileClick(it) },
            { presenter.loadNextIssuesPage() }
        )
    }
    private var zeroViewHolder: ZeroViewHolder? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = this@ProjectFilesFragment.adapter
        }

        swipeToRefresh.setOnRefreshListener { presenter.refreshFiles() }
        zeroViewHolder = ZeroViewHolder(zeroLayout, { presenter.refreshFiles() })
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
        if (show) zeroViewHolder?.showEmptyData()
        else zeroViewHolder?.hide()
    }

    override fun showEmptyError(show: Boolean, message: String?) {
        if (show) zeroViewHolder?.showEmptyError(message)
        else zeroViewHolder?.hide()
    }

    override fun showFiles(show: Boolean, files: List<ProjectFile>) {
        recyclerView.visible(show)
        postViewAction { adapter.setData(files) }
    }

    override fun showMessage(message: String) {
        showSnackMessage(message)
    }

    companion object {
        private const val ARG_SCOPE_NAME = "arg_scope_name"

        fun create(scope: String) =
            ProjectFilesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_SCOPE_NAME, scope)
                }
            }
    }
}
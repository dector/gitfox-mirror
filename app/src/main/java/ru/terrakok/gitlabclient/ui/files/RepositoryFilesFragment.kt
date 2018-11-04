package ru.terrakok.gitlabclient.ui.files

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_repository_files.*
import kotlinx.android.synthetic.main.layout_base_list.*
import kotlinx.android.synthetic.main.layout_zero.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.app.RepositoryFile
import ru.terrakok.gitlabclient.extension.showSnackMessage
import ru.terrakok.gitlabclient.extension.visible
import ru.terrakok.gitlabclient.presentation.files.RepositoryFilesPresenter
import ru.terrakok.gitlabclient.presentation.files.RepositoryFilesView
import ru.terrakok.gitlabclient.toothpick.DI
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import ru.terrakok.gitlabclient.ui.global.ZeroViewHolder
import toothpick.Toothpick

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 02.11.18.
 */
class RepositoryFilesFragment : BaseFragment(), RepositoryFilesView {
    override val layoutRes = R.layout.fragment_repository_files

    @InjectPresenter
    lateinit var presenter: RepositoryFilesPresenter

    @ProvidePresenter
    fun providePresenter(): RepositoryFilesPresenter =
        Toothpick
            .openScope(DI.REPOSITORY_FILES_FLOW_SCOPE)
            .getInstance(RepositoryFilesPresenter::class.java)

    private val adapter: RepositoryFilesAdapter by lazy {
        RepositoryFilesAdapter(
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
            adapter = this@RepositoryFilesFragment.adapter
        }
        toolbar.apply {
            title = "Files"
            subtitle = "develop"
            setNavigationOnClickListener { presenter.onBackPressed() }
        }

        swipeToRefresh.setOnRefreshListener { presenter.refreshFiles() }
        zeroViewHolder = ZeroViewHolder(zeroLayout, { presenter.refreshFiles() })
    }

    override fun onBackPressed() {
        super.onBackPressed()
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
        if (show) zeroViewHolder?.showEmptyData()
        else zeroViewHolder?.hide()
    }

    override fun showEmptyError(show: Boolean, message: String?) {
        if (show) zeroViewHolder?.showEmptyError(message)
        else zeroViewHolder?.hide()
    }

    override fun showFiles(show: Boolean, files: List<RepositoryFile>) {
        recyclerView.visible(show)
        postViewAction { adapter.setData(files) }
    }

    override fun showMessage(message: String) {
        showSnackMessage(message)
    }
}
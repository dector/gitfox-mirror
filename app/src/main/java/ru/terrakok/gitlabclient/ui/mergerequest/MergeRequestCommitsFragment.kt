package ru.terrakok.gitlabclient.ui.mergerequest

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.layout_base_list.*
import kotlinx.android.synthetic.main.layout_zero.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.app.CommitWithAvatarUrl
import ru.terrakok.gitlabclient.extension.showSnackMessage
import ru.terrakok.gitlabclient.extension.visible
import ru.terrakok.gitlabclient.presentation.mergerequest.commits.MergeRequestCommitsPresenter
import ru.terrakok.gitlabclient.presentation.mergerequest.commits.MergeRequestCommitsView
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import ru.terrakok.gitlabclient.ui.global.ZeroViewHolder
import ru.terrakok.gitlabclient.ui.global.list.SimpleDividerDecorator
import ru.terrakok.gitlabclient.ui.global.list.TargetCommitsAdapter

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 20.10.18.
 */
class MergeRequestCommitsFragment : BaseFragment(), MergeRequestCommitsView {

    override val layoutRes = R.layout.fragment_mr_commits

    private val adapter by lazy { TargetCommitsAdapter({ presenter.loadNextCommitsPage() }) }
    private var zeroViewHolder: ZeroViewHolder? = null

    @InjectPresenter
    lateinit var presenter: MergeRequestCommitsPresenter

    @ProvidePresenter
    fun providePresenter() =
        scope.getInstance(MergeRequestCommitsPresenter::class.java)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        with(recyclerView) {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(SimpleDividerDecorator(context))
            adapter = this@MergeRequestCommitsFragment.adapter
        }

        swipeToRefresh.setOnRefreshListener { presenter.refreshCommits() }
        zeroViewHolder = ZeroViewHolder(zeroLayout, { presenter.refreshCommits() })
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

    override fun showCommits(show: Boolean, commits: List<CommitWithAvatarUrl>) {
        recyclerView.visible(show)
        postViewAction { adapter.setData(commits) }
    }

    override fun showMessage(message: String) {
        showSnackMessage(message)
    }
}
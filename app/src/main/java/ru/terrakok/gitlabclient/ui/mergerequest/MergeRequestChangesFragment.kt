package ru.terrakok.gitlabclient.ui.mergerequest

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.layout_base_list.*
import kotlinx.android.synthetic.main.layout_zero.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.mergerequest.MergeRequestChange
import ru.terrakok.gitlabclient.extension.showSnackMessage
import ru.terrakok.gitlabclient.extension.visible
import ru.terrakok.gitlabclient.presentation.mergerequest.changes.MergeRequestChangesPresenter
import ru.terrakok.gitlabclient.presentation.mergerequest.changes.MergeRequestChangesView
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import ru.terrakok.gitlabclient.ui.global.ZeroViewHolder
import ru.terrakok.gitlabclient.ui.global.list.SimpleDividerDecorator

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 25.10.18.
 */
class MergeRequestChangesFragment : BaseFragment(), MergeRequestChangesView {

    override val layoutRes = R.layout.fragment_mr_changes

    private val adapter by lazy { MergeRequestChangeAdapter() }
    private var zeroViewHolder: ZeroViewHolder? = null

    @InjectPresenter
    lateinit var presenter: MergeRequestChangesPresenter

    @ProvidePresenter
    fun providePresenter() =
        scope.getInstance(MergeRequestChangesPresenter::class.java)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        with(recyclerView) {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(SimpleDividerDecorator(context))
            adapter = this@MergeRequestChangesFragment.adapter
        }

        swipeToRefresh.setOnRefreshListener { presenter.refreshChanges() }
        zeroViewHolder = ZeroViewHolder(zeroLayout, { presenter.refreshChanges() })
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

    override fun showEmptyView(show: Boolean) {
        if (show) zeroViewHolder?.showEmptyData()
        else zeroViewHolder?.hide()
    }

    override fun showEmptyError(show: Boolean, message: String?) {
        if (show) zeroViewHolder?.showEmptyError(message)
        else zeroViewHolder?.hide()
    }

    override fun showChanges(show: Boolean, changes: List<MergeRequestChange>) {
        recyclerView.visible(show)
        postViewAction { adapter.setData(changes) }
    }

    override fun showMessage(message: String) {
        showSnackMessage(message)
    }
}
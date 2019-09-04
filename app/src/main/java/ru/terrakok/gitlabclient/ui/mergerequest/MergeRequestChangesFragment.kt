package ru.terrakok.gitlabclient.ui.mergerequest

import android.os.Bundle
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import kotlinx.android.synthetic.main.fragment_mr_changes.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.mergerequest.MergeRequestChange
import ru.terrakok.gitlabclient.presentation.mergerequest.changes.MergeRequestChangesPresenter
import ru.terrakok.gitlabclient.presentation.mergerequest.changes.MergeRequestChangesView
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import ru.terrakok.gitlabclient.ui.global.list.MergeRequestChangeAdapterDelegate
import ru.terrakok.gitlabclient.ui.global.list.SimpleDividerDecorator
import ru.terrakok.gitlabclient.ui.global.list.isSame
import ru.terrakok.gitlabclient.util.showSnackMessage
import ru.terrakok.gitlabclient.util.visible

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 25.10.18.
 */
class MergeRequestChangesFragment : BaseFragment(), MergeRequestChangesView {

    override val layoutRes = R.layout.fragment_mr_changes

    private val adapter by lazy {
        object : AsyncListDifferDelegationAdapter<MergeRequestChange>(
            object : DiffUtil.ItemCallback<MergeRequestChange>() {
                override fun areItemsTheSame(oldItem: MergeRequestChange, newItem: MergeRequestChange) = oldItem.isSame(newItem)
                override fun areContentsTheSame(oldItem: MergeRequestChange, newItem: MergeRequestChange) = oldItem == newItem
                override fun getChangePayload(oldItem: MergeRequestChange, newItem: MergeRequestChange) = Any()
            }
        ) {
            init {
                items = mutableListOf()
                delegatesManager.addDelegate(
                    MergeRequestChangeAdapterDelegate { presenter.onMergeRequestChangeClick(it) }
                )
            }
        }
    }

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
        emptyView.setRefreshListener { presenter.refreshChanges() }
    }

    override fun showRefreshProgress(show: Boolean) {
        postViewAction { swipeToRefresh.isRefreshing = show }
    }

    override fun showEmptyProgress(show: Boolean) {
        fullscreenProgressView.visible(show)

        // Trick for disable and hide swipeToRefresh on fullscreen progress
        swipeToRefresh.visible(!show)
        postViewAction { swipeToRefresh.isRefreshing = false }
    }

    override fun showEmptyView(show: Boolean) {
        emptyView.apply { if (show) showEmptyData() else hide() }
    }

    override fun showEmptyError(show: Boolean, message: String?) {
        emptyView.apply { if (show) showEmptyError(message) else hide() }
    }

    override fun showChanges(show: Boolean, changes: List<MergeRequestChange>) {
        recyclerView.visible(show)
        postViewAction { adapter.items = changes }
    }

    override fun showMessage(message: String) {
        showSnackMessage(message)
    }

    override fun showFullscreenProgress(show: Boolean) {
        showProgressDialog(show)
    }
}
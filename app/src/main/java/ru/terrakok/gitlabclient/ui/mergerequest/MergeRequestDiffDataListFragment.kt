package ru.terrakok.gitlabclient.ui.mergerequest

import android.os.Bundle
import androidx.recyclerview.widget.DiffUtil
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.DiffData
import ru.terrakok.gitlabclient.presentation.mergerequest.changes.MergeRequestDiffDataListPresenter
import ru.terrakok.gitlabclient.presentation.mergerequest.changes.MergeRequestDiffDataListView
import ru.terrakok.gitlabclient.ui.global.BaseFragment

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 25.10.18.
 */
class MergeRequestDiffDataListFragment : BaseFragment(), MergeRequestDiffDataListView {

    override val layoutRes = R.layout.fragment_mr_diff_data_list

    private val adapter by lazy {
        object : AsyncListDifferDelegationAdapter<MergeRequestChange>(
            object : DiffUtil.ItemCallback<MergeRequestChange>() {
                override fun areItemsTheSame(
                    oldItem: MergeRequestChange,
                    newItem: MergeRequestChange
                ) = oldItem.isSame(newItem)

                override fun areContentsTheSame(
                    oldItem: MergeRequestChange,
                    newItem: MergeRequestChange
                ) = oldItem == newItem

                override fun getChangePayload(
                    oldItem: MergeRequestChange,
                    newItem: MergeRequestChange
                ) = Any()
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
    lateinit var presenter: MergeRequestDiffDataListPresenter

    @ProvidePresenter
    fun providePresenter() =
            scope.getInstance(MergeRequestDiffDataListPresenter::class.java)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        with(recyclerView) {
            addSystemBottomPadding()
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(SimpleDividerDecorator(context))
            adapter = this@MergeRequestDiffDataListFragment.adapter
        }

        swipeToRefresh.setOnRefreshListener { presenter.refreshDiffDataList() }
        emptyView.setRefreshListener { presenter.refreshDiffDataList() }
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

    override fun showDiffDataList(show: Boolean, diffDataList: List<DiffData>) {
        recyclerView.visible(show)
        postViewAction { adapter.items = diffDataList }
    }

    override fun showMessage(message: String) {
        showSnackMessage(message)
    }

    override fun showFullscreenProgress(show: Boolean) {
        showProgressDialog(show)
    }
}
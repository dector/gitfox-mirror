package ru.terrakok.gitlabclient.ui.my.mergerequests

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter
import kotlinx.android.synthetic.main.layout_base_list.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.mergerequest.MergeRequest
import ru.terrakok.gitlabclient.entity.mergerequest.MergeRequestState
import ru.terrakok.gitlabclient.extension.visible
import ru.terrakok.gitlabclient.presentation.my.mergerequests.MyMergeRequestListPresenter
import ru.terrakok.gitlabclient.presentation.my.mergerequests.MyMergeRequestListView
import ru.terrakok.gitlabclient.toothpick.DI
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import ru.terrakok.gitlabclient.ui.global.list.ListItem
import ru.terrakok.gitlabclient.ui.global.list.MergeRequestAdapterDelegate
import ru.terrakok.gitlabclient.ui.global.list.ProgressAdapterDelegate
import toothpick.Toothpick
import toothpick.config.Module

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 13.06.17
 */
class MyMergeRequestsFragment : BaseFragment(), MyMergeRequestListView {

    companion object {
        private val ARG_STATE = "arg_state"

        fun newInstance(state: MergeRequestState) = MyMergeRequestsFragment().apply {
            arguments = Bundle().apply { putSerializable(ARG_STATE, state) }
        }
    }

    override val layoutRes = R.layout.fragment_my_merge_requests
    private val adapter = MergeRequestListAdapter()

    @InjectPresenter lateinit var presenter: MyMergeRequestListPresenter

    @ProvidePresenter
    fun providePresenter(): MyMergeRequestListPresenter {
        val scopeName = "MyMergeRequestListScope_${hashCode()}"
        val scope = Toothpick.openScopes(DI.MAIN_ACTIVITY_SCOPE, scopeName)
        scope.installModules(object : Module() {
            init {
                val state = arguments.getSerializable(ARG_STATE) as MergeRequestState
                bind(MergeRequestState::class.java)
                        .toInstance(state)
            }
        })

        return scope.getInstance(MyMergeRequestListPresenter::class.java).also {
            Toothpick.closeScope(scopeName)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = this@MyMergeRequestsFragment.adapter
        }

        swipeToRefresh.setOnRefreshListener { presenter.refreshMergeRequests() }
    }

    override fun showRefreshProgress(show: Boolean) {
        swipeToRefresh.post { swipeToRefresh.isRefreshing = show }
    }

    override fun showEmptyProgress(show: Boolean) {
        fullscreenProgressView.visible(show)

        //trick for disable and hide swipeToRefresh on fullscreen progress
        swipeToRefresh.visible(!show)
        swipeToRefresh.post { swipeToRefresh.isRefreshing = false }
    }

    override fun showPageProgress(show: Boolean) {
        recyclerView.post { adapter.showProgress(show) }
    }

    override fun showEmptyView(show: Boolean) {
        //todo
    }

    override fun showEmptyError(show: Boolean, message: String?) {
        //todo
        if (show && message != null) showSnackMessage(message)
    }

    override fun showMergeRequests(show: Boolean, mergeRequests: List<MergeRequest>) {
        recyclerView.visible(show)
        recyclerView.post { adapter.setData(mergeRequests) }
    }

    override fun showMessage(message: String) {
        showSnackMessage(message)
    }

    private inner class MergeRequestListAdapter : ListDelegationAdapter<MutableList<ListItem>>() {
        init {
            items = mutableListOf()
            delegatesManager.addDelegate(MergeRequestAdapterDelegate({ presenter.onMergeRequestClick(it) }))
            delegatesManager.addDelegate(ProgressAdapterDelegate())
        }

        fun setData(mergeRequests: List<MergeRequest>) {
            val progress = isProgress()

            items.clear()
            items.addAll(mergeRequests.map { ListItem.MergeRequestItem(it) })
            if (progress) items.add(ListItem.ProgressItem())

            notifyDataSetChanged()
        }

        fun showProgress(isVisible: Boolean) {
            val currentProgress = isProgress()

            if (isVisible && !currentProgress) items.add(ListItem.ProgressItem())
            else if (!isVisible && currentProgress) items.remove(items.last())

            notifyDataSetChanged()
        }

        private fun isProgress() = items.isNotEmpty() && items.last() is ListItem.ProgressItem

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int, payloads: MutableList<Any?>?) {
            super.onBindViewHolder(holder, position, payloads)

            if (position == items.size - 10) presenter.loadNextMergeRequestsPage()
        }
    }
}
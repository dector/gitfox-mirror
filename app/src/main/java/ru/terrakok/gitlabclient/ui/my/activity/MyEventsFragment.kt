package ru.terrakok.gitlabclient.ui.my.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter
import kotlinx.android.synthetic.main.fragment_my_activity.*
import kotlinx.android.synthetic.main.layout_base_list.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.app.FullEventInfo
import ru.terrakok.gitlabclient.extension.visible
import ru.terrakok.gitlabclient.presentation.my.events.MyEventsPresenter
import ru.terrakok.gitlabclient.presentation.my.events.MyEventsView
import ru.terrakok.gitlabclient.toothpick.DI
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import ru.terrakok.gitlabclient.ui.global.list.EventAdapterDelegate
import ru.terrakok.gitlabclient.ui.global.list.ListItem
import ru.terrakok.gitlabclient.ui.global.list.ProgressAdapterDelegate
import toothpick.Toothpick

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 13.06.17
 */
class MyEventsFragment : BaseFragment(), MyEventsView {
    override val layoutRes = R.layout.fragment_my_activity

    private val adapter = EventsAdapter()

    @InjectPresenter lateinit var presenter: MyEventsPresenter

    @ProvidePresenter
    fun providePresenter(): MyEventsPresenter =
        Toothpick
                .openScope(DI.MAIN_ACTIVITY_SCOPE)
                .getInstance(MyEventsPresenter::class.java)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = this@MyEventsFragment.adapter
        }

        swipeToRefresh.setOnRefreshListener { presenter.refreshEvents() }
        toolbar.setNavigationOnClickListener { presenter.onMenuClick() }
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

    override fun showEvents(show: Boolean, events: List<FullEventInfo>) {
        recyclerView.visible(show)
        recyclerView.post { adapter.setData(events) }
    }

    override fun showMessage(message: String) {
        showSnackMessage(message)
    }

    inner class EventsAdapter : ListDelegationAdapter<MutableList<ListItem>>() {
        init {
            items = mutableListOf()
            delegatesManager.addDelegate(EventAdapterDelegate({ presenter.onEventClick(it) }))
            delegatesManager.addDelegate(ProgressAdapterDelegate())
        }

        fun setData(events: List<FullEventInfo>) {
            val progress = isProgress()

            items.clear()
            items.addAll(events.map { ListItem.EventItem(it) })
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

            if (position == items.size - 10) presenter.loadNextEventsPage()
        }
    }
}
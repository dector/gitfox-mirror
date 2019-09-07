package ru.terrakok.gitlabclient.ui.global.view.custom

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import kotlinx.android.synthetic.main.view_paginal_render.view.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.di.DI
import ru.terrakok.gitlabclient.model.system.ResourceManager
import ru.terrakok.gitlabclient.presentation.global.Paginator
import ru.terrakok.gitlabclient.ui.global.list.ProgressAdapterDelegate
import ru.terrakok.gitlabclient.ui.global.list.ProgressItem
import ru.terrakok.gitlabclient.util.addSystemBottomPadding
import ru.terrakok.gitlabclient.util.inflate
import ru.terrakok.gitlabclient.util.userMessage
import ru.terrakok.gitlabclient.util.visible
import toothpick.Toothpick
import javax.inject.Inject

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 2019-06-22.
 */
class PaginalRenderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    @Inject
    lateinit var resourceManager: ResourceManager

    private var refreshCallback: (() -> Unit)? = null
    private var nextPageCallback: (() -> Unit)? = null
    private var itemDiff: ((old: Any, new: Any) -> Boolean)? = null

    private var adapter: PaginalAdapter? = null

    init {
        Toothpick.inject(this, Toothpick.openScope(DI.APP_SCOPE))
        inflate(R.layout.view_paginal_render, true)
        swipeToRefresh.setOnRefreshListener { refreshCallback?.invoke() }
        emptyView.setRefreshListener { refreshCallback?.invoke() }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            addSystemBottomPadding()
        }
    }

    fun init(
        refreshCallback: () -> Unit,
        nextPageCallback: () -> Unit,
        itemDiff: (old: Any, new: Any) -> Boolean,
        vararg delegate: AdapterDelegate<MutableList<Any>>
    ) {
        this.refreshCallback = refreshCallback
        this.nextPageCallback = nextPageCallback
        this.itemDiff = itemDiff
        adapter = PaginalAdapter(*delegate)
        recyclerView.adapter = adapter
    }

    fun render(state: Paginator.State) {
        post {
            when (state) {
                is Paginator.State.Empty -> {
                    swipeToRefresh.isRefreshing = false
                    fullscreenProgressView.visible(false)
                    adapter?.fullData = true
                    adapter?.update(emptyList(), false)
                    emptyView.showEmptyData()
                    swipeToRefresh.visible(true)
                }
                is Paginator.State.EmptyProgress -> {
                    swipeToRefresh.isRefreshing = false
                    fullscreenProgressView.visible(true)
                    adapter?.fullData = false
                    adapter?.update(emptyList(), false)
                    emptyView.hide()
                    swipeToRefresh.visible(false)
                }
                is Paginator.State.EmptyError -> {
                    swipeToRefresh.isRefreshing = false
                    fullscreenProgressView.visible(false)
                    adapter?.fullData = false
                    adapter?.update(emptyList(), false)
                    emptyView.showEmptyError(state.error.userMessage(resourceManager))
                    swipeToRefresh.visible(true)
                }
                is Paginator.State.Data<*> -> {
                    swipeToRefresh.isRefreshing = false
                    fullscreenProgressView.visible(false)
                    adapter?.fullData = false
                    adapter?.update(state.data as List<Any>, false)
                    emptyView.hide()
                    swipeToRefresh.visible(true)
                }
                is Paginator.State.Refresh<*> -> {
                    swipeToRefresh.isRefreshing = true
                    fullscreenProgressView.visible(false)
                    adapter?.fullData = false
                    adapter?.update(state.data as List<Any>, false)
                    emptyView.hide()
                    swipeToRefresh.visible(true)
                }
                is Paginator.State.NewPageProgress<*> -> {
                    swipeToRefresh.isRefreshing = false
                    fullscreenProgressView.visible(false)
                    adapter?.fullData = false
                    adapter?.update(state.data as List<Any>, true)
                    emptyView.hide()
                    swipeToRefresh.visible(true)
                }
                is Paginator.State.FullData<*> -> {
                    swipeToRefresh.isRefreshing = false
                    fullscreenProgressView.visible(false)
                    adapter?.fullData = true
                    adapter?.update(state.data as List<Any>, false)
                    emptyView.hide()
                    swipeToRefresh.visible(true)
                }
            }
        }
    }

    private inner class PaginalAdapter(vararg delegate: AdapterDelegate<MutableList<Any>>) :
        AsyncListDifferDelegationAdapter<Any>(
            object : DiffUtil.ItemCallback<Any>() {
                override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
                    if (oldItem === newItem) return true
                    return itemDiff?.invoke(oldItem, newItem) ?: false
                }

                override fun getChangePayload(oldItem: Any, newItem: Any) = Any() //disable default blink animation

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(oldItem: Any, newItem: Any) = oldItem == newItem
            }
        ) {
        var fullData = false

        init {
            items = mutableListOf()
            delegatesManager.addDelegate(ProgressAdapterDelegate())
            delegate.forEach { delegatesManager.addDelegate(it) }
        }

        fun update(data: List<Any>, isPageProgress: Boolean) {
            items = mutableListOf<Any>().apply {
                addAll(data)
                if (isPageProgress) add(ProgressItem)
            }
        }

        override fun onBindViewHolder(
            holder: RecyclerView.ViewHolder,
            position: Int,
            payloads: MutableList<Any?>
        ) {
            super.onBindViewHolder(holder, position, payloads)
            if (!fullData && position >= items.size - 10) nextPageCallback?.invoke()
        }
    }
}
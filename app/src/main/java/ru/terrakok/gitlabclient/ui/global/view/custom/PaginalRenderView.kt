package ru.terrakok.gitlabclient.ui.global.view.custom

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import kotlinx.android.synthetic.main.view_paginal_render.view.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.extension.inflate
import ru.terrakok.gitlabclient.extension.visible
import ru.terrakok.gitlabclient.presentation.global.Paginator
import ru.terrakok.gitlabclient.ui.global.list.ProgressAdapterDelegate
import ru.terrakok.gitlabclient.ui.global.list.ProgressItem

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 2019-06-22.
 */
class PaginalRenderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var refreshCallback: (() -> Unit)? = null
    private var nextPageCallback: (() -> Unit)? = null
    private var itemDiff: ((old: Any, new: Any) -> Boolean)? = null

    private var adapter: PaginalAdapter? = null

    init {
        inflate(R.layout.view_paginal_render, true)
        swipeToRefresh.setOnRefreshListener { refreshCallback?.invoke() }
        emptyView.setRefreshListener { refreshCallback?.invoke() }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
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

    fun <T : Any> render(state: Paginator.State<T>) {
        post {
            if (state.refreshProgress) {
                if (state.items.isEmpty()) {
                    fullscreenProgressView.visible(true)
                } else {
                    swipeToRefresh.isRefreshing = true
                }
            } else {
                fullscreenProgressView.visible(false)
                swipeToRefresh.isRefreshing = false
            }

            adapter?.update(state.items, state.pageProgress)
            if (state.error != null) {
                if (state.items.isEmpty()) {
                    emptyView.showEmptyError(state.error.toString())
                } else {
                    Snackbar.make(this, state.error.toString(), Snackbar.LENGTH_SHORT).show()
                }
            } else {
                if (state.items.isEmpty() && !state.refreshProgress) {
                    emptyView.showEmptyData()
                } else {
                    emptyView.hide()
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
            if (position >= items.size - 10) nextPageCallback?.invoke()
        }
    }
}
package ru.terrakok.gitlabclient.ui.global.view.custom

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import javax.inject.Inject
import kotlinx.android.synthetic.main.view_paginal_render.view.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.di.DI
import ru.terrakok.gitlabclient.model.system.ResourceManager
import ru.terrakok.gitlabclient.presentation.global.Paginator
import ru.terrakok.gitlabclient.ui.global.list.PaginalAdapter
import ru.terrakok.gitlabclient.util.addSystemBottomPadding
import ru.terrakok.gitlabclient.util.inflate
import ru.terrakok.gitlabclient.util.userMessage
import ru.terrakok.gitlabclient.util.visible
import toothpick.Toothpick

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
        adapter: PaginalAdapter
    ) {
        this.refreshCallback = refreshCallback
        this.adapter = adapter
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
}

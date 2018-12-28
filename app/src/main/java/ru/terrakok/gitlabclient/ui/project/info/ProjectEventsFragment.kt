package ru.terrakok.gitlabclient.ui.project.info

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.layout_base_list.*
import kotlinx.android.synthetic.main.layout_zero.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.app.target.TargetHeader
import ru.terrakok.gitlabclient.extension.showSnackMessage
import ru.terrakok.gitlabclient.extension.visible
import ru.terrakok.gitlabclient.presentation.project.events.ProjectEventsPresenter
import ru.terrakok.gitlabclient.presentation.project.events.ProjectEventsView
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import ru.terrakok.gitlabclient.ui.global.ZeroViewHolder
import ru.terrakok.gitlabclient.ui.my.TargetsAdapter

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 13.06.17
 */
class ProjectEventsFragment : BaseFragment(), ProjectEventsView {
    override val layoutRes = R.layout.fragment_project_events

    @InjectPresenter
    lateinit var presenter: ProjectEventsPresenter

    private val adapter: TargetsAdapter by lazy {
        TargetsAdapter(
            mvpDelegate,
            { presenter.onUserClick(it) },
            { presenter.onItemClick(it) },
            { presenter.loadNextEventsPage() }
        )
    }
    private var zeroViewHolder: ZeroViewHolder? = null

    @ProvidePresenter
    fun providePresenter(): ProjectEventsPresenter =
        scope.getInstance(ProjectEventsPresenter::class.java)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = this@ProjectEventsFragment.adapter
        }

        swipeToRefresh.setOnRefreshListener { presenter.refreshEvents() }
        zeroViewHolder = ZeroViewHolder(zeroLayout, { presenter.refreshEvents() })
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

    override fun showEvents(show: Boolean, events: List<TargetHeader>) {
        recyclerView.visible(show)
        postViewAction { adapter.setData(events) }
    }

    override fun showMessage(message: String) {
        showSnackMessage(message)
    }

    override fun onBackPressed() {
        presenter.onBackPressed()
    }
}
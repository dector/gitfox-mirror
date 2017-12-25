package ru.terrakok.gitlabclient.ui.my.mergerequests

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.layout_base_list.*
import kotlinx.android.synthetic.main.layout_zero.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.app.target.TargetHeader
import ru.terrakok.gitlabclient.extension.visible
import ru.terrakok.gitlabclient.presentation.my.mergerequests.MyMergeRequestListView
import ru.terrakok.gitlabclient.presentation.my.mergerequests.MyMergeRequestsPresenter
import ru.terrakok.gitlabclient.toothpick.DI
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import ru.terrakok.gitlabclient.ui.global.ZeroViewHolder
import ru.terrakok.gitlabclient.ui.my.TargetsAdapter
import toothpick.Toothpick
import toothpick.config.Module

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 13.06.17
 */
class MyMergeRequestsFragment : BaseFragment(), MyMergeRequestListView {

    companion object {
        private val ARG_MODE_CREATED_BY_ME = "arg_mode_created_by_me"

        fun newInstance(createdByMe: Boolean) = MyMergeRequestsFragment().apply {
            arguments = Bundle().apply { putBoolean(ARG_MODE_CREATED_BY_ME, createdByMe) }
        }
    }

    override val layoutRes = R.layout.fragment_my_merge_requests

    @InjectPresenter lateinit var presenter: MyMergeRequestsPresenter

    private val adapter: TargetsAdapter by lazy {
        TargetsAdapter(
                { presenter.onMergeRequestClick(it) },
                { presenter.loadNextMergeRequestsPage() }
        )
    }
    private var zeroViewHolder: ZeroViewHolder? = null

    @ProvidePresenter
    fun providePresenter(): MyMergeRequestsPresenter {
        val scopeName = "MyMergeRequestsScope_${hashCode()}"
        val scope = Toothpick.openScopes(DI.MAIN_ACTIVITY_SCOPE, scopeName)
        scope.installModules(object : Module() {
            init {
                bind(MyMergeRequestsPresenter.InitParams::class.java)
                        .toInstance(MyMergeRequestsPresenter.InitParams(
                                arguments?.getBoolean(ARG_MODE_CREATED_BY_ME) ?: true
                        ))
            }
        })

        return scope.getInstance(MyMergeRequestsPresenter::class.java).also {
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
        zeroViewHolder = ZeroViewHolder(zeroLayout, { presenter.refreshMergeRequests() })
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
        if (show) zeroViewHolder?.showEmptyData()
        else zeroViewHolder?.hide()
    }

    override fun showEmptyError(show: Boolean, message: String?) {
        if (show) zeroViewHolder?.showEmptyError(message)
        else zeroViewHolder?.hide()
    }

    override fun showMergeRequests(show: Boolean, mergeRequests: List<TargetHeader>) {
        recyclerView.visible(show)
        recyclerView.post { adapter.setData(mergeRequests) }
    }

    override fun showMessage(message: String) {
        showSnackMessage(message)
    }
}
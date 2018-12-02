package ru.terrakok.gitlabclient.ui.my.issues

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
import ru.terrakok.gitlabclient.presentation.my.issues.MyIssuesPresenter
import ru.terrakok.gitlabclient.presentation.my.issues.MyIssuesView
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import ru.terrakok.gitlabclient.ui.global.ZeroViewHolder
import ru.terrakok.gitlabclient.ui.my.TargetsAdapter
import toothpick.Scope
import toothpick.config.Module

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 13.06.17
 */
class MyIssuesFragment : BaseFragment(), MyIssuesView {

    override val layoutRes = R.layout.fragment_my_issues

    @InjectPresenter
    lateinit var presenter: MyIssuesPresenter

    private val adapter: TargetsAdapter by lazy {
        TargetsAdapter(
            { presenter.onUserClick(it) },
            { presenter.onIssueClick(it) },
            { presenter.loadNextIssuesPage() }
        )
    }
    private var zeroViewHolder: ZeroViewHolder? = null

    override val scopeModuleInstaller = { scope: Scope ->
        scope.installModules(object : Module() {
            init {
                bind(MyIssuesPresenter.Filter::class.java)
                    .toInstance(
                        MyIssuesPresenter.Filter(
                            arguments?.getBoolean(ARG_MODE_CREATED_BY_ME) ?: true,
                            arguments?.getBoolean(ARG_MODE_ONLY_OPENED) ?: false
                        )
                    )
            }
        })
    }

    @ProvidePresenter
    fun providePresenter(): MyIssuesPresenter =
        scope.getInstance(MyIssuesPresenter::class.java)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = this@MyIssuesFragment.adapter
        }

        swipeToRefresh.setOnRefreshListener { presenter.refreshIssues() }
        zeroViewHolder = ZeroViewHolder(zeroLayout, { presenter.refreshIssues() })
    }

    fun showOnlyOpened(onlyOpened: Boolean) {
        presenter.applyNewFilter(
            MyIssuesPresenter.Filter(
                arguments?.getBoolean(ARG_MODE_CREATED_BY_ME) ?: true,
                onlyOpened
            )
        )
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

    override fun showIssues(show: Boolean, issues: List<TargetHeader>) {
        recyclerView.visible(show)
        postViewAction { adapter.setData(issues) }
    }

    override fun showMessage(message: String) {
        showSnackMessage(message)
    }

    companion object {
        private const val ARG_MODE_CREATED_BY_ME = "arg_mode_created_by_me"
        private const val ARG_MODE_ONLY_OPENED = "arg_mode_only opened"

        fun create(createdByMe: Boolean, onlyOpened: Boolean) =
            MyIssuesFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(ARG_MODE_CREATED_BY_ME, createdByMe)
                    putBoolean(ARG_MODE_ONLY_OPENED, onlyOpened)
                }
            }
    }
}
package ru.terrakok.gitlabclient.ui.my.issues

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter
import kotlinx.android.synthetic.main.fragment_my_issues.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.common.Issue
import ru.terrakok.gitlabclient.presentation.my.issues.MyIssuesPresenter
import ru.terrakok.gitlabclient.presentation.my.issues.MyIssuesView
import ru.terrakok.gitlabclient.toothpick.DI
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import ru.terrakok.gitlabclient.ui.global.list.ListItem
import ru.terrakok.gitlabclient.ui.global.list.ProgressAdapterDelegate
import toothpick.Toothpick
import toothpick.config.Module

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 13.06.17
 */
class MyIssuesFragment : BaseFragment(), MyIssuesView {

    companion object {
        private val ARG_MODE_IS_OPENED = "arg_mode_is_opened"

        fun newInstance(isOpened: Boolean) = MyIssuesFragment().apply {
            arguments = Bundle().apply { putBoolean(ARG_MODE_IS_OPENED, isOpened) }
        }
    }

    override val layoutRes = R.layout.fragment_my_issues

    private val adapter = IssuesAdapter()

    @InjectPresenter lateinit var presenter: MyIssuesPresenter

    @ProvidePresenter
    fun providePresenter(): MyIssuesPresenter {
        val scopeName = "MyIssuesScope_${hashCode()}"
        val scope = Toothpick.openScopes(DI.APP_SCOPE, scopeName)
        scope.installModules(object : Module() {
            init {
                bind(MyIssuesPresenter.InitParams::class.java)
                        .toInstance(MyIssuesPresenter.InitParams(arguments.getBoolean(ARG_MODE_IS_OPENED)))
            }
        })

        return scope.getInstance(MyIssuesPresenter::class.java).also {
            Toothpick.closeScope(scopeName)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = this@MyIssuesFragment.adapter
        }

        swipeToRefresh.setOnRefreshListener { presenter.refreshIssues() }
    }

    override fun showProgress(show: Boolean) {
        swipeToRefresh.post { swipeToRefresh.isRefreshing = show }
    }

    override fun showPageProgress(show: Boolean) {
        recyclerView.post { adapter.showProgress(show) }
    }

    override fun showIssues(issues: List<Issue>) {
        recyclerView.post { adapter.setData(issues) }
    }

    override fun showMessage(message: String) {
        showSnackMessage(message)
    }

    inner class IssuesAdapter : ListDelegationAdapter<MutableList<ListItem>>() {
        init {
            items = mutableListOf()
            delegatesManager.addDelegate(IssueAdapterDelegate({ presenter.onIssueClick(it) }))
            delegatesManager.addDelegate(ProgressAdapterDelegate())
        }

        fun setData(issues: List<Issue>) {
            val progress = isProgress()

            items.clear()
            items.addAll(issues.map { ListItem.IssueItem(it) })
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

            if (position == items.size - 10) presenter.loadNextIssuesPage()
        }
    }
}
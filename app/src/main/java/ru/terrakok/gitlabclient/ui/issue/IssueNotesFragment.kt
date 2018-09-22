package ru.terrakok.gitlabclient.ui.issue

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.layout_base_list.*
import kotlinx.android.synthetic.main.layout_zero.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.extension.showSnackMessage
import ru.terrakok.gitlabclient.extension.visible
import ru.terrakok.gitlabclient.presentation.global.NoteWithFormattedBody
import ru.terrakok.gitlabclient.presentation.issue.notes.IssueNotesPresenter
import ru.terrakok.gitlabclient.presentation.issue.notes.IssueNotesView
import ru.terrakok.gitlabclient.toothpick.DI
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import ru.terrakok.gitlabclient.ui.global.ZeroViewHolder
import ru.terrakok.gitlabclient.ui.global.list.SimpleDividerDecorator
import ru.terrakok.gitlabclient.ui.global.list.TargetNotesAdapter
import toothpick.Toothpick

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 15.02.18.
 */
class IssueNotesFragment : BaseFragment(), IssueNotesView {

    override val layoutRes = R.layout.fragment_issue_notes

    private val adapter by lazy { TargetNotesAdapter({ presenter.loadNextIssuesPage() }) }
    private var zeroViewHolder: ZeroViewHolder? = null

    @InjectPresenter
    lateinit var presenter: IssueNotesPresenter

    @ProvidePresenter
    fun providePresenter() =
        Toothpick.openScope(DI.ISSUE_FLOW_SCOPE)
            .getInstance(IssueNotesPresenter::class.java)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        with(recyclerView) {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(SimpleDividerDecorator(context))
            adapter = this@IssueNotesFragment.adapter
        }

        swipeToRefresh.setOnRefreshListener { presenter.refreshNotes() }
        zeroViewHolder = ZeroViewHolder(zeroLayout, { presenter.refreshNotes() })
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

    override fun showNotes(show: Boolean, notes: List<NoteWithFormattedBody>) {
        recyclerView.visible(show)
        postViewAction { adapter.setData(notes) }
    }

    override fun showMessage(message: String) {
        showSnackMessage(message)
    }
}
package ru.terrakok.gitlabclient.ui.mergerequest

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.layout_base_list.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.extension.showSnackMessage
import ru.terrakok.gitlabclient.extension.visible
import ru.terrakok.gitlabclient.presentation.global.NoteWithFormattedBody
import ru.terrakok.gitlabclient.presentation.mergerequest.notes.MergeRequestNotesPresenter
import ru.terrakok.gitlabclient.presentation.mergerequest.notes.MergeRequestNotesView
import ru.terrakok.gitlabclient.toothpick.DI
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import ru.terrakok.gitlabclient.ui.global.list.SimpleDividerDecorator
import ru.terrakok.gitlabclient.ui.global.list.TargetNotesAdapter
import toothpick.Toothpick

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 15.02.18.
 */
class MergeRequestNotesFragment : BaseFragment(), MergeRequestNotesView {

    override val layoutRes = R.layout.fragment_mr_notes

    private val adapter by lazy { TargetNotesAdapter({ presenter.loadNextMergeRequestsPage() }) }

    @InjectPresenter
    lateinit var presenter: MergeRequestNotesPresenter

    @ProvidePresenter
    fun providePresenter() =
        Toothpick.openScope(DI.MERGE_REQUEST_FLOW_SCOPE)
            .getInstance(MergeRequestNotesPresenter::class.java)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        with(recyclerView) {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(SimpleDividerDecorator(context))
            adapter = this@MergeRequestNotesFragment.adapter
        }

        swipeToRefresh.setOnRefreshListener { presenter.refreshNotes() }
    }

    override fun showRefreshProgress(show: Boolean) {
        swipeToRefresh.post { swipeToRefresh.isRefreshing = show }
    }

    override fun showEmptyProgress(show: Boolean) {
        swipeToRefresh.post { swipeToRefresh.isRefreshing = false }
    }

    override fun showPageProgress(show: Boolean) {
        recyclerView.post { adapter.showProgress(show) }
    }

    override fun showEmptyView(show: Boolean) {
    }

    override fun showEmptyError(show: Boolean, message: String?) {
    }

    override fun showNotes(show: Boolean, notes: List<NoteWithFormattedBody>) {
        recyclerView.visible(show)
        recyclerView.post { adapter.setData(notes) }
    }

    override fun showMessage(message: String) {
        showSnackMessage(message)
    }
}
package ru.terrakok.gitlabclient.ui.issue

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter
import kotlinx.android.synthetic.main.layout_base_list.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.presentation.global.NoteWithFormattedBody
import ru.terrakok.gitlabclient.presentation.issue.notes.IssueNotesPresenter
import ru.terrakok.gitlabclient.presentation.issue.notes.IssueNotesView
import ru.terrakok.gitlabclient.toothpick.DI
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import ru.terrakok.gitlabclient.ui.global.list.SimpleDividerDecorator
import ru.terrakok.gitlabclient.ui.global.list.SystemNoteAdapterDelegate
import ru.terrakok.gitlabclient.ui.global.list.UserNoteAdapterDelegate
import toothpick.Toothpick

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 15.02.18.
 */
class IssueNotesFragment : BaseFragment(), IssueNotesView {
    override val layoutRes = R.layout.fragment_issue_notes

    private val adapter by lazy { NotesAdapter() }

    @InjectPresenter
    lateinit var presenter: IssueNotesPresenter

    @ProvidePresenter
    fun providePresenter() =
        Toothpick.openScope(DI.ISSUE_SCOPE)
            .getInstance(IssueNotesPresenter::class.java)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        with(recyclerView) {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(SimpleDividerDecorator(context))
            adapter = this@IssueNotesFragment.adapter
        }

        swipeToRefresh.setOnRefreshListener { presenter.refresh() }
    }

    override fun showNotes(notes: List<NoteWithFormattedBody>) {
        adapter.setData(notes)
    }

    override fun showProgress(show: Boolean) {
        swipeToRefresh.isRefreshing = show
    }

    override fun showMessage(message: String) {
        showSnackMessage(message)
    }

    private inner class NotesAdapter : ListDelegationAdapter<MutableList<Any>>() {

        init {
            items = mutableListOf()
            delegatesManager.addDelegate(UserNoteAdapterDelegate({}))
            delegatesManager.addDelegate(SystemNoteAdapterDelegate({}))
        }

        fun setData(data: List<NoteWithFormattedBody>) {
            items.clear()
            items.addAll(data)
            notifyDataSetChanged()
        }
    }
}
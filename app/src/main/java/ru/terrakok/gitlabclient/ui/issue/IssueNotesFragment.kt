package ru.terrakok.gitlabclient.ui.issue

import android.os.Bundle
import android.support.transition.Fade
import android.support.transition.TransitionManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_issue_notes.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.extension.showSnackMessage
import ru.terrakok.gitlabclient.extension.visible
import ru.terrakok.gitlabclient.presentation.global.NoteWithFormattedBody
import ru.terrakok.gitlabclient.presentation.issue.notes.IssueNotesPresenter
import ru.terrakok.gitlabclient.presentation.issue.notes.IssueNotesView
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import ru.terrakok.gitlabclient.ui.global.list.SimpleDividerDecorator
import ru.terrakok.gitlabclient.ui.global.list.TargetNotesAdapter

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 15.02.18.
 */
class IssueNotesFragment : BaseFragment(), IssueNotesView {

    override val layoutRes = R.layout.fragment_issue_notes

    private val adapter by lazy { TargetNotesAdapter() }
    private val fadeFabScrollToBottom by lazy {
        Fade().apply {
            addTarget(fabScrollToBottom)
        }
    }

    @InjectPresenter
    lateinit var presenter: IssueNotesPresenter

    @ProvidePresenter
    fun providePresenter(): IssueNotesPresenter =
        scope.getInstance(IssueNotesPresenter::class.java)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        with(recyclerView) {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(SimpleDividerDecorator(context))
            adapter = this@IssueNotesFragment.adapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val visiblePosition =
                        (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                    setFabScrollVisible(visiblePosition < this@IssueNotesFragment.adapter.itemCount - 2)
                }
            })
        }
        fabScrollToBottom.setOnClickListener {
            recyclerView.scrollToPosition(adapter.itemCount - 1)
            setFabScrollVisible(false)
        }
        newNoteView.init {  presenter.onSendClicked(it) }
    }

    private fun setFabScrollVisible(visible: Boolean) {
        TransitionManager.beginDelayedTransition(noteContainer, fadeFabScrollToBottom)
        fabScrollToBottom.visible(visible)
    }

    override fun showEmptyProgress(show: Boolean) {
        fullscreenProgressView.visible(show)
        newNoteView.visible(!show)
    }

    override fun showBlockingProgress(show: Boolean) {
        showProgressDialog(show)
    }

    override fun showNotes(notes: List<NoteWithFormattedBody>, scrollToEnd: Boolean) {
        adapter.setData(notes)
        if (scrollToEnd) {
            recyclerView.scrollToPosition(adapter.itemCount - 1)
            newNoteView.clearInput()
        }
    }

    override fun showMessage(message: String) {
        showSnackMessage(message)
    }
}
package ru.terrakok.gitlabclient.ui.issue

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.transition.Fade
import android.support.transition.TransitionManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.WindowManager
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_issue_notes.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.extension.showSnackMessage
import ru.terrakok.gitlabclient.extension.visible
import ru.terrakok.gitlabclient.presentation.global.NoteWithFormattedBody
import ru.terrakok.gitlabclient.presentation.issue.notes.IssueNotesPresenter
import ru.terrakok.gitlabclient.presentation.issue.notes.IssueNotesView
import ru.terrakok.gitlabclient.toothpick.DI
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import ru.terrakok.gitlabclient.ui.global.list.SimpleDividerDecorator
import ru.terrakok.gitlabclient.ui.global.list.TargetNotesAdapter
import toothpick.Toothpick

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
    fun providePresenter() =
        Toothpick.openScope(DI.ISSUE_FLOW_SCOPE)
            .getInstance(IssueNotesPresenter::class.java)

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (context as Activity).window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }

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
        noteInputLayout.setOnSendClickListener { presenter.onSendClicked(it) }
    }

    private fun setFabScrollVisible(visible: Boolean) {
        TransitionManager.beginDelayedTransition(noteContainer, fadeFabScrollToBottom)
        fabScrollToBottom.visible(visible)
    }

    override fun onDetach() {
        super.onDetach()

        (context as Activity).window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED)
    }

    override fun showEmptyProgress(show: Boolean) {
        fullscreenProgressView.visible(show)
        noteInputLayout.visible(!show)
    }

    override fun showBlockingProgress(show: Boolean) {
        showProgressDialog(show)
    }

    override fun showNotes(notes: List<NoteWithFormattedBody>, afterCreate: Boolean) {
        adapter.setData(notes)
        if (afterCreate) {
            recyclerView.scrollToPosition(adapter.itemCount - 1)
            noteInputLayout.clearInput()
        }
    }

    override fun showMessage(message: String) {
        showSnackMessage(message)
    }
}
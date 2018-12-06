package ru.terrakok.gitlabclient.ui.mergerequest

import android.os.Bundle
import android.support.transition.Fade
import android.support.transition.TransitionManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_issue_notes.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.extension.showSnackMessage
import ru.terrakok.gitlabclient.extension.visible
import ru.terrakok.gitlabclient.presentation.global.NoteWithProjectId
import ru.terrakok.gitlabclient.presentation.mergerequest.notes.MergeRequestNotesPresenter
import ru.terrakok.gitlabclient.presentation.mergerequest.notes.MergeRequestNotesView
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import ru.terrakok.gitlabclient.ui.global.NewNoteViewController
import ru.terrakok.gitlabclient.ui.global.list.SimpleDividerDecorator
import ru.terrakok.gitlabclient.ui.global.list.TargetNotesAdapter

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 15.02.18.
 */
class MergeRequestNotesFragment : BaseFragment(), MergeRequestNotesView {

    override val layoutRes = R.layout.fragment_mr_notes

    private val adapter by lazy { TargetNotesAdapter(mvpDelegate) }
    private val fadeFabScrollToBottom by lazy {
        Fade().apply {
            addTarget(fabScrollToBottom)
        }
    }
    private lateinit var newNoteViewController: NewNoteViewController

    @InjectPresenter
    lateinit var presenter: MergeRequestNotesPresenter

    @ProvidePresenter
    fun providePresenter() =
        scope.getInstance(MergeRequestNotesPresenter::class.java)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        with(recyclerView) {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(SimpleDividerDecorator(context))
            adapter = this@MergeRequestNotesFragment.adapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val visiblePosition =
                        (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                    setFabScrollVisible(visiblePosition < this@MergeRequestNotesFragment.adapter.itemCount - 2)
                }
            })
        }
        fabScrollToBottom.setOnClickListener {
            recyclerView.scrollToPosition(adapter.itemCount - 1)
            setFabScrollVisible(false)
        }
        newNoteViewController = NewNoteViewController(noteInputLayout as ViewGroup, { presenter.onSendClicked(it) })
    }

    private fun setFabScrollVisible(visible: Boolean) {
        TransitionManager.beginDelayedTransition(noteContainer, fadeFabScrollToBottom)
        fabScrollToBottom.visible(visible)
    }

    override fun showEmptyProgress(show: Boolean) {
        fullscreenProgressView.visible(show)
        noteInputLayout.visible(!show)
    }

    override fun showBlockingProgress(show: Boolean) {
        showProgressDialog(show)
    }

    override fun showNotes(notes: List<NoteWithProjectId>, scrollToEnd: Boolean) {
        adapter.setData(notes)
        if (scrollToEnd) {
            recyclerView.scrollToPosition(adapter.itemCount - 1)
            newNoteViewController.clearInput()
        }
    }

    override fun showMessage(message: String) {
        showSnackMessage(message)
    }
}
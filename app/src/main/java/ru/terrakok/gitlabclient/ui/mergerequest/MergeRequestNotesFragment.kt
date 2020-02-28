package ru.terrakok.gitlabclient.ui.mergerequest

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Fade
import androidx.transition.TransitionManager
import kotlinx.android.synthetic.main.fragment_mr_notes.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.presentation.global.NoteWithFormattedBody
import ru.terrakok.gitlabclient.presentation.mergerequest.notes.MergeRequestNotesPresenter
import ru.terrakok.gitlabclient.presentation.mergerequest.notes.MergeRequestNotesView
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import ru.terrakok.gitlabclient.ui.global.list.SimpleDividerDecorator
import ru.terrakok.gitlabclient.ui.global.list.TargetNotesAdapter
import ru.terrakok.gitlabclient.util.addSystemBottomPadding
import ru.terrakok.gitlabclient.util.showSnackMessage
import ru.terrakok.gitlabclient.util.visible

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 15.02.18.
 */
class MergeRequestNotesFragment : BaseFragment(), MergeRequestNotesView {

    override val layoutRes = R.layout.fragment_mr_notes

    private val adapter by lazy { TargetNotesAdapter() }
    private val fadeFabScrollToBottom by lazy {
        Fade().apply {
            addTarget(fabScrollToBottom)
        }
    }

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
        newNoteView.addSystemBottomPadding()
        newNoteView.init { presenter.onSendClicked(it) }
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

    override fun showNotes(notes: List<NoteWithFormattedBody>, scrollToPosition: Int?) {
        adapter.items = notes
        scrollToPosition?.let { recyclerView.scrollToPosition(it) }
    }

    override fun showMessage(message: String) {
        showSnackMessage(message)
    }

    override fun clearInput() {
        newNoteView.clearInput()
    }
}

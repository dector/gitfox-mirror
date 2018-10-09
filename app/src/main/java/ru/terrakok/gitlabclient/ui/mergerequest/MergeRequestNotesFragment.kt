package ru.terrakok.gitlabclient.ui.mergerequest

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
import ru.terrakok.gitlabclient.presentation.mergerequest.notes.MergeRequestNotesPresenter
import ru.terrakok.gitlabclient.presentation.mergerequest.notes.MergeRequestNotesView
import ru.terrakok.gitlabclient.toothpick.DI
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import ru.terrakok.gitlabclient.ui.global.ZeroViewHolder
import ru.terrakok.gitlabclient.ui.global.list.SimpleDividerDecorator
import ru.terrakok.gitlabclient.ui.global.list.TargetNotesAdapter
import toothpick.Toothpick

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 15.02.18.
 */
class MergeRequestNotesFragment : BaseFragment(), MergeRequestNotesView {

    override val layoutRes = R.layout.fragment_mr_notes

    private val adapter by lazy { TargetNotesAdapter() }

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
        swipeToRefresh.isEnabled = false
    }

    override fun showEmptyProgress(show: Boolean) {
        fullscreenProgressView.visible(show)
    }

    override fun showNotes(notes: List<NoteWithFormattedBody>) {
        adapter.setData(notes)
    }

    override fun showMessage(message: String) {
        showSnackMessage(message)
    }
}
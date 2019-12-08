package ru.terrakok.gitlabclient.ui.mergerequest

import android.os.Bundle
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_mr_commits.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.app.CommitWithShortUser
import ru.terrakok.gitlabclient.presentation.global.Paginator
import ru.terrakok.gitlabclient.presentation.mergerequest.commits.MergeRequestCommitsPresenter
import ru.terrakok.gitlabclient.presentation.mergerequest.commits.MergeRequestCommitsView
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import ru.terrakok.gitlabclient.ui.global.list.CommitAdapterDelegate
import ru.terrakok.gitlabclient.ui.global.list.PaginalAdapter
import ru.terrakok.gitlabclient.ui.global.list.isSame
import ru.terrakok.gitlabclient.util.showSnackMessage

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 20.10.18.
 */
class MergeRequestCommitsFragment : BaseFragment(), MergeRequestCommitsView {

    override val layoutRes = R.layout.fragment_mr_commits

    @InjectPresenter
    lateinit var presenter: MergeRequestCommitsPresenter

    @ProvidePresenter
    fun providePresenter() =
            scope.getInstance(MergeRequestCommitsPresenter::class.java)

    private val adapter by lazy { PaginalAdapter(
            { presenter.loadNextCommitsPage() },
            { o, n ->
                if (o is CommitWithShortUser && n is CommitWithShortUser) {
                    o.isSame(n)
                } else false
            },
            CommitAdapterDelegate({ presenter.onCommitClicked(it) })
    ) }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        paginalRenderView.init(
            { presenter.refreshCommits() },
            adapter
        )
    }

    override fun renderPaginatorState(state: Paginator.State) {
        paginalRenderView.render(state)
    }

    override fun showMessage(message: String) {
        showSnackMessage(message)
    }
}
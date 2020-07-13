package ru.terrakok.gitlabclient.ui.commit

import android.os.Bundle
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import gitfox.entity.Commit
import gitfox.entity.DiffData
import kotlinx.android.synthetic.main.fragment_commit.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.di.CommitId
import ru.terrakok.gitlabclient.di.PrimitiveWrapper
import ru.terrakok.gitlabclient.di.ProjectId
import ru.terrakok.gitlabclient.presentation.commit.CommitPresenter
import ru.terrakok.gitlabclient.presentation.commit.CommitView
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import ru.terrakok.gitlabclient.ui.global.list.DiffDataAdapterDelegate
import ru.terrakok.gitlabclient.ui.global.list.SimpleDividerDecorator
import ru.terrakok.gitlabclient.ui.global.list.isSame
import ru.terrakok.gitlabclient.util.addSystemTopPadding
import ru.terrakok.gitlabclient.util.argument
import ru.terrakok.gitlabclient.util.showSnackMessage
import ru.terrakok.gitlabclient.util.visible
import toothpick.Scope
import toothpick.config.Module

/**
 * @author Valentin Logvinovitch (glvvl) on 18.06.19.
 */
class CommitFragment : BaseFragment(), CommitView {

    override val layoutRes = R.layout.fragment_commit

    private val projectId by argument(ARG_PROJECT_ID, 0L)
    private val commitId by argument(ARG_COMMIT_ID, "")

    override fun installModules(scope: Scope) {
        super.installModules(scope)
        scope.installModules(
            object : Module() {
                init {
                    bind(PrimitiveWrapper::class.java)
                            .withName(ProjectId::class.java)
                            .toInstance(PrimitiveWrapper(projectId))
                    bind(String::class.java)
                            .withName(CommitId::class.java)
                            .toInstance(commitId)
                }
            }
        )
    }

    @InjectPresenter
    lateinit var presenter: CommitPresenter

    @ProvidePresenter
    fun providePresenter(): CommitPresenter =
            scope.getInstance(CommitPresenter::class.java)

    private val adapter by lazy {
        object : AsyncListDifferDelegationAdapter<DiffData>(
            object : DiffUtil.ItemCallback<DiffData>() {
                override fun areItemsTheSame(
                    oldItem: DiffData,
                    newItem: DiffData
                ) = oldItem.isSame(newItem)

                override fun areContentsTheSame(
                    oldItem: DiffData,
                    newItem: DiffData
                ) = oldItem == newItem

                override fun getChangePayload(
                    oldItem: DiffData,
                    newItem: DiffData
                ) = Any()
            }
        ) {
            init {
                items = mutableListOf()
                delegatesManager.addDelegate(DiffDataAdapterDelegate {
                    presenter.onDiffDataClicked(it)
                })
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        toolbar.apply {
            setNavigationOnClickListener { presenter.onBackPressed() }
            addSystemTopPadding()
        }
        with(recyclerView) {
            addSystemTopPadding()
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(SimpleDividerDecorator(context))
            adapter = this@CommitFragment.adapter
        }

        swipeToRefresh.setOnRefreshListener { presenter.refreshDiffDataList() }
        emptyView.setRefreshListener { presenter.refreshDiffDataList() }
    }

    override fun showCommitInfo(commit: Commit) {
        toolbar.apply {
            title = commit.title
            subtitle = commit.authorName
        }
    }

    override fun showDiffDataList(show: Boolean, diffDataList: List<DiffData>) {
        recyclerView.visible(show)
        postViewAction { adapter.items = diffDataList }
    }

    override fun showEmptyError(show: Boolean, message: String?) {
        emptyView.apply { if (show) showEmptyError(message) else hide() }
    }

    override fun showEmptyView(show: Boolean) {
        emptyView.apply { if (show) showEmptyData() else hide() }
    }

    override fun showEmptyProgress(show: Boolean) {
        fullscreenProgressView.visible(show)

        // Trick for disable and hide swipeToRefresh on fullscreen progress
        swipeToRefresh.visible(!show)
        postViewAction { swipeToRefresh.isRefreshing = false }
    }

    override fun showRefreshProgress(show: Boolean) {
        postViewAction { swipeToRefresh.isRefreshing = show }
    }

    override fun showBlockingProgress(show: Boolean) {
        showProgressDialog(show)
    }

    override fun showMessage(message: String) {
        showSnackMessage(message)
    }

    override fun onBackPressed() {
        presenter.onBackPressed()
    }

    companion object {
        private const val ARG_PROJECT_ID = "arg_project_id"
        private const val ARG_COMMIT_ID = "arg_commit_id"

        fun create(commitId: String, projectId: Long) =
                CommitFragment().apply {
                    arguments = Bundle().apply {
                        putLong(ARG_PROJECT_ID, projectId)
                        putString(ARG_COMMIT_ID, commitId)
                    }
                }
    }
}

package ru.terrakok.gitlabclient.ui.commit

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_commit.*
import kotlinx.android.synthetic.main.layout_base_list.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.di.CommitId
import ru.terrakok.gitlabclient.di.PrimitiveWrapper
import ru.terrakok.gitlabclient.di.ProjectId
import ru.terrakok.gitlabclient.entity.Commit
import ru.terrakok.gitlabclient.entity.DiffData
import ru.terrakok.gitlabclient.extension.argument
import ru.terrakok.gitlabclient.extension.showSnackMessage
import ru.terrakok.gitlabclient.extension.visible
import ru.terrakok.gitlabclient.presentation.commit.CommitPresenter
import ru.terrakok.gitlabclient.presentation.commit.CommitView
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import ru.terrakok.gitlabclient.ui.global.list.SimpleDividerDecorator
import ru.terrakok.gitlabclient.ui.mergerequest.DiffDataListAdapter
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

    private val adapter by lazy { DiffDataListAdapter({ presenter.onDiffDataClicked(it) }) }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        toolbar.setNavigationOnClickListener { presenter.onBackPressed() }
        with(recyclerView) {
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
        postViewAction { adapter.setData(diffDataList) }
    }

    override fun showEmptyError(show: Boolean, message: String?) {
        emptyView.apply { if (show) showEmptyError(message) else hide() }
    }

    override fun showEmptyView(show: Boolean) {
        emptyView.apply { if (show) showEmptyData() else hide() }
    }

    override fun showEmptyProgress(show: Boolean) {
        fullscreenProgressView.visible(show)

        //trick for disable and hide swipeToRefresh on fullscreen progress
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
package ru.terrakok.gitlabclient.ui.project.files

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.PopupMenu
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_project_files.*
import kotlinx.android.synthetic.main.layout_base_list.*
import kotlinx.android.synthetic.main.layout_zero.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.Branch
import ru.terrakok.gitlabclient.entity.app.ProjectFile
import ru.terrakok.gitlabclient.extension.showSnackMessage
import ru.terrakok.gitlabclient.extension.visible
import ru.terrakok.gitlabclient.presentation.project.files.ProjectFileDestination
import ru.terrakok.gitlabclient.presentation.project.files.ProjectFilesPresenter
import ru.terrakok.gitlabclient.presentation.project.files.ProjectFilesView
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import ru.terrakok.gitlabclient.ui.global.ZeroViewHolder
import toothpick.Scope
import toothpick.config.Module

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 02.11.18.
 */
class ProjectFilesFragment : BaseFragment(), ProjectFilesView {
    override val layoutRes = R.layout.fragment_project_files

    @InjectPresenter
    lateinit var presenter: ProjectFilesPresenter

    override val scopeModuleInstaller = { scope: Scope ->
        scope.installModules(object : Module() {
            init {
                bind(ProjectFileDestination::class.java)
                    .toInstance(projectFileDestination)
            }
        })
    }

    @ProvidePresenter
    fun providePresenter(): ProjectFilesPresenter =
        scope.getInstance(ProjectFilesPresenter::class.java)

    private val adapter: ProjectFilesAdapter by lazy {
        ProjectFilesAdapter(
            { presenter.onFileClick(it) },
            { presenter.loadNextFilesPage() }
        )
    }
    private var zeroViewHolder: ZeroViewHolder? = null

    private lateinit var projectFileDestination: ProjectFileDestination

    override fun onCreate(savedInstanceState: Bundle?) {
        projectFileDestination = ProjectFileDestination()
        if (savedInstanceState != null) {
            projectFileDestination.restoreState(savedInstanceState)
        }
        super.onCreate(savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = this@ProjectFilesFragment.adapter
        }
        toolbar.apply {
            inflateMenu(R.menu.project_files_menu)
            setNavigationOnClickListener { presenter.onBackPressed() }
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.showBranchesAction -> {
                        presenter.onShowBranchesClick()
                        true
                    }
                    else -> false
                }
            }
        }

        swipeToRefresh.setOnRefreshListener { presenter.refreshFiles() }
        zeroViewHolder = ZeroViewHolder(zeroLayout, { presenter.refreshFiles() })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        projectFileDestination.saveState(outState)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        presenter.onBackPressed()
    }

    override fun setPath(path: String) {
        toolbar.title = path
    }

    override fun setBranch(branchName: String) {
        toolbar.subtitle = branchName
    }

    override fun showRefreshProgress(show: Boolean) {
        postViewAction { swipeToRefresh.isRefreshing = show }
    }

    override fun showEmptyProgress(show: Boolean) {
        fullscreenProgressView.visible(show)

        //trick for disable and hide swipeToRefresh on fullscreen progress
        swipeToRefresh.visible(!show)
        postViewAction { swipeToRefresh.isRefreshing = false }
    }

    override fun showPageProgress(show: Boolean) {
        postViewAction { adapter.showProgress(show) }
    }

    override fun showEmptyView(show: Boolean) {
        if (show) zeroViewHolder?.showEmptyData()
        else zeroViewHolder?.hide()
    }

    override fun showEmptyError(show: Boolean, message: String?) {
        if (show) zeroViewHolder?.showEmptyError(message)
        else zeroViewHolder?.hide()
    }

    override fun showFiles(show: Boolean, files: List<ProjectFile>) {
        recyclerView.visible(show)
        postViewAction { adapter.setData(files) }
    }

    override fun showBlockingProgress(show: Boolean) {
        showProgressDialog(show)
    }

    override fun showBranches(branches: List<Branch>) {
        PopupMenu(toolbar.context, toolbar).apply {
            for (branch in branches) {
                menu.add(branch.name)
            }
            setOnMenuItemClickListener {
                presenter.onBranchClick(it.title.toString())
                true
            }
            show()
        }
    }

    override fun showBranchSelection(show: Boolean) {
        toolbar.menu.findItem(R.id.showBranchesAction).isVisible = show
    }

    override fun showMessage(message: String) {
        showSnackMessage(message)
    }
}
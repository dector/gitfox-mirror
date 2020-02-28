package ru.terrakok.gitlabclient.ui.project.files

import android.os.Bundle
import android.text.TextUtils
import android.widget.PopupMenu
import kotlinx.android.synthetic.main.fragment_project_files.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.Branch
import ru.terrakok.gitlabclient.entity.app.ProjectFile
import ru.terrakok.gitlabclient.presentation.global.Paginator
import ru.terrakok.gitlabclient.presentation.project.files.ProjectFileDestination
import ru.terrakok.gitlabclient.presentation.project.files.ProjectFilesPresenter
import ru.terrakok.gitlabclient.presentation.project.files.ProjectFilesView
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import ru.terrakok.gitlabclient.ui.global.list.PaginalAdapter
import ru.terrakok.gitlabclient.ui.global.list.ProjectFileAdapterDelegate
import ru.terrakok.gitlabclient.ui.global.list.isSame
import ru.terrakok.gitlabclient.util.addSystemTopPadding
import ru.terrakok.gitlabclient.util.setTitleEllipsize
import ru.terrakok.gitlabclient.util.showSnackMessage
import toothpick.Scope
import toothpick.config.Module

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 02.11.18.
 */
class ProjectFilesFragment : BaseFragment(), ProjectFilesView {
    override val layoutRes = R.layout.fragment_project_files

    @InjectPresenter
    lateinit var presenter: ProjectFilesPresenter

    override fun installModules(scope: Scope) {
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

    private lateinit var projectFileDestination: ProjectFileDestination
    private val adapter by lazy { PaginalAdapter({ presenter.loadNextFilesPage() },
            { o, n ->
                if (o is ProjectFile && n is ProjectFile) {
                    o.isSame(n)
                } else false
            },
            ProjectFileAdapterDelegate { presenter.onFileClick(it) }
    ) }

    override fun onCreate(savedInstanceState: Bundle?) {
        projectFileDestination = ProjectFileDestination()
        if (savedInstanceState != null) {
            projectFileDestination.restoreState(savedInstanceState)
        }
        super.onCreate(savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        toolbar.apply {
            addSystemTopPadding()
            inflateMenu(R.menu.project_files_menu)
            setNavigationOnClickListener { presenter.onNavigationCloseClicked() }
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.showBranchesAction -> {
                        presenter.onShowBranchesClick()
                        true
                    }
                    else -> false
                }
            }
            setTitleEllipsize(TextUtils.TruncateAt.START)
        }
        paginalRenderView.init(
            { presenter.refreshFiles() },
            adapter
        )
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

    override fun renderPaginatorState(state: Paginator.State) {
        paginalRenderView.render(state)
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

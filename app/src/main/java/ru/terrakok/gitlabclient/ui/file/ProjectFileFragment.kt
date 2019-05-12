package ru.terrakok.gitlabclient.ui.file

import android.os.Bundle
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_mr.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.extension.argument
import ru.terrakok.gitlabclient.presentation.file.ProjectFilePresenter
import ru.terrakok.gitlabclient.presentation.file.ProjectFileView
import ru.terrakok.gitlabclient.toothpick.DI
import ru.terrakok.gitlabclient.toothpick.PrimitiveWrapper
import ru.terrakok.gitlabclient.toothpick.qualifier.BranchName
import ru.terrakok.gitlabclient.toothpick.qualifier.FilePath
import ru.terrakok.gitlabclient.toothpick.qualifier.ProjectId
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import toothpick.Scope
import toothpick.config.Module

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 22.11.18.
 */
class ProjectFileFragment : BaseFragment(), ProjectFileView {

    private val projectId by argument(ARG_PROJECT_ID, 0L)
    private val filePath by argument<String>(ARG_FILE_PATH, null)
    private val branchName by argument<String>(ARG_BRANCH_NAME, null)

    override val layoutRes = R.layout.fragment_project_file

    override val parentScopeName = DI.SERVER_SCOPE

    override val scopeModuleInstaller = { scope: Scope ->
        scope.installModules(
            object : Module() {
                init {
                    bind(PrimitiveWrapper::class.java)
                        .withName(ProjectId::class.java)
                        .toInstance(PrimitiveWrapper(projectId))
                    bind(String::class.java)
                        .withName(FilePath::class.java)
                        .toInstance(filePath)
                    bind(String::class.java)
                        .withName(BranchName::class.java)
                        .toInstance(branchName)
                }
            }
        )
    }

    @InjectPresenter
    lateinit var presenter: ProjectFilePresenter

    @ProvidePresenter
    fun providePresenter() = scope.getInstance(ProjectFilePresenter::class.java)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        presenter.onBackPressed()
    }

    companion object {
        private const val ARG_PROJECT_ID = "arg_project_id"
        private const val ARG_FILE_PATH = "arg_file_path"
        private const val ARG_BRANCH_NAME = "arg_branch_name"

        fun create(projectId: Long, filePath: String, branchName: String) =
            ProjectFileFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_PROJECT_ID, projectId)
                    putString(ARG_FILE_PATH, filePath)
                    putString(ARG_BRANCH_NAME, branchName)
                }
            }
    }
}
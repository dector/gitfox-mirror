package ru.terrakok.gitlabclient.ui.file

import android.os.Bundle
import com.arellomobile.mvp.MvpView
import ru.terrakok.cicerone.Router
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.extension.argument
import ru.terrakok.gitlabclient.extension.setLaunchScreen
import ru.terrakok.gitlabclient.toothpick.DI
import ru.terrakok.gitlabclient.toothpick.PrimitiveWrapper
import ru.terrakok.gitlabclient.toothpick.module.FlowNavigationModule
import ru.terrakok.gitlabclient.toothpick.qualifier.BranchName
import ru.terrakok.gitlabclient.toothpick.qualifier.FileName
import ru.terrakok.gitlabclient.toothpick.qualifier.FilePath
import ru.terrakok.gitlabclient.toothpick.qualifier.ProjectId
import ru.terrakok.gitlabclient.ui.global.FlowFragment
import toothpick.Scope
import toothpick.Toothpick
import toothpick.config.Module
import javax.inject.Inject

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 22.11.18.
 */
class ProjectFileFlowFragment : FlowFragment(), MvpView {

    private val projectId by argument(ARG_PROJECT_ID, 0L)
    private val fileName by argument<String>(ARG_FILE_NAME, null)
    private val filePath by argument<String>(ARG_FILE_PATH, null)
    private val branchName by argument<String>(ARG_BRANCH_NAME, null)

    override val parentScopeName = DI.SERVER_SCOPE

    override val scopeModuleInstaller = { scope: Scope ->
        scope.installModules(
            FlowNavigationModule(scope.getInstance(Router::class.java)),
            object : Module() {
                init {
                    bind(PrimitiveWrapper::class.java)
                        .withName(ProjectId::class.java)
                        .toInstance(PrimitiveWrapper(projectId))
                    bind(String::class.java)
                        .withName(FileName::class.java)
                        .toInstance(fileName)
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

    @Inject
    lateinit var router: Router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Toothpick.inject(this, scope)
        if (childFragmentManager.fragments.isEmpty()) {
            navigator.setLaunchScreen(Screens.Auth)
        }
    }

    override fun onExit() {
        router.exit()
    }

    companion object {
        private const val ARG_PROJECT_ID = "arg_project_id"
        private const val ARG_FILE_NAME = "arg_file_name"
        private const val ARG_FILE_PATH = "arg_file_path"
        private const val ARG_BRANCH_NAME = "arg_branch_name"
        fun create(projectId: Long, fileName: String, filePath: String, branchName: String) =
            ProjectFileFlowFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_PROJECT_ID, projectId)
                    putString(ARG_FILE_NAME, fileName)
                    putString(ARG_FILE_PATH, filePath)
                    putString(ARG_BRANCH_NAME, branchName)
                }
            }
    }
}
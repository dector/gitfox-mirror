package ru.terrakok.gitlabclient.ui.files

import android.os.Bundle
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import ru.terrakok.cicerone.Router
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.extension.argument
import ru.terrakok.gitlabclient.extension.setLaunchScreen
import ru.terrakok.gitlabclient.presentation.files.ProjectFilesFlowPresenter
import ru.terrakok.gitlabclient.toothpick.DI
import ru.terrakok.gitlabclient.toothpick.PrimitiveWrapper
import ru.terrakok.gitlabclient.toothpick.module.FlowNavigationModule
import ru.terrakok.gitlabclient.toothpick.qualifier.ProjectId
import ru.terrakok.gitlabclient.ui.global.FlowFragment
import toothpick.Toothpick
import toothpick.config.Module

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 04.11.18.
 */
class ProjectFilesFlowFragment : FlowFragment(), MvpView {

    private val projectId by argument(ARG_PROJECT_ID, 0L)

    @InjectPresenter
    lateinit var presenter: ProjectFilesFlowPresenter

    @ProvidePresenter
    fun providePresenter(): ProjectFilesFlowPresenter =
        Toothpick
            .openScope(DI.PROJECT_FILES_FLOW_SCOPE)
            .getInstance(ProjectFilesFlowPresenter::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        prepareScope(isFirstLaunch(savedInstanceState))
        super.onCreate(savedInstanceState)
        if (childFragmentManager.fragments.isEmpty()) {
            navigator.setLaunchScreen(Screens.ProjectFiles)
        }
    }

    private fun prepareScope(firstTime: Boolean) {
        val scope = Toothpick.openScopes(DI.SERVER_SCOPE, DI.PROJECT_FILES_FLOW_SCOPE)
        if (firstTime) {
            scope.installModules(
                FlowNavigationModule(scope.getInstance(Router::class.java)),
                object : Module() {
                    init {
                        bind(PrimitiveWrapper::class.java)
                            .withName(ProjectId::class.java)
                            .toInstance(PrimitiveWrapper(projectId))
                    }
                }
            )
        }
        Toothpick.inject(this, scope)
    }

    override fun onExit() {
        presenter.onExit()
    }

    companion object {
        private const val ARG_PROJECT_ID = "arg_project_id"
        fun create(projectId: Long) =
            ProjectFilesFlowFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_PROJECT_ID, projectId)
                }
            }
    }
}
package ru.terrakok.gitlabclient.ui.project

import android.os.Bundle
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.extension.argument
import ru.terrakok.gitlabclient.extension.setLaunchScreen
import ru.terrakok.gitlabclient.model.system.flow.AppRouter
import ru.terrakok.gitlabclient.presentation.project.ProjectFlowPresenter
import ru.terrakok.gitlabclient.toothpick.DI
import ru.terrakok.gitlabclient.toothpick.PrimitiveWrapper
import ru.terrakok.gitlabclient.toothpick.module.FlowNavigationModule
import ru.terrakok.gitlabclient.toothpick.qualifier.ProjectId
import ru.terrakok.gitlabclient.ui.global.FlowFragment
import toothpick.Toothpick
import toothpick.config.Module

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 25.11.17.
 */
class ProjectFlowFragment : FlowFragment(), MvpView {

    private val projectId by argument(ARG_PROJECT_ID, 0L)
    private val scopeName: String? by argument(ARG_SCOPE_NAME)

    @InjectPresenter
    lateinit var presenter: ProjectFlowPresenter

    @ProvidePresenter
    fun providePresenter() =
        Toothpick.openScope(scopeName)
            .getInstance(ProjectFlowPresenter::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        initScope()
        super.onCreate(savedInstanceState)
        if (childFragmentManager.fragments.isEmpty()) {
            navigator.setLaunchScreen(Screens.PROJECT_MAIN_FLOW, scopeName)
        }
    }

    private fun initScope() {
        val scope = Toothpick.openScopes(DI.SERVER_SCOPE, scopeName)
        scope.installModules(
            FlowNavigationModule(scope.getInstance(AppRouter::class.java)),
            object : Module() {
                init {
                    bind(PrimitiveWrapper::class.java)
                        .withName(ProjectId::class.java)
                        .toInstance(PrimitiveWrapper(projectId))
                }
            }
        )
        Toothpick.inject(this, scope)
    }

    override fun onExit() {
        presenter.onExit()
    }

    companion object {
        private const val ARG_PROJECT_ID = "arg_project_id"
        private const val ARG_SCOPE_NAME = "arg_scope_name"
        fun create(projectId: Long, scope: String) =
            ProjectFlowFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_PROJECT_ID, projectId)
                    putString(ARG_SCOPE_NAME, scope)
                }
            }
    }
}
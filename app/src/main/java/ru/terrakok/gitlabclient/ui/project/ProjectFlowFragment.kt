package ru.terrakok.gitlabclient.ui.project

import android.os.Bundle
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import ru.terrakok.cicerone.Router
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.extension.argument
import ru.terrakok.gitlabclient.extension.objectScopeName
import ru.terrakok.gitlabclient.extension.setLaunchScreen
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
    private val scopeName: String
        get() {
            var scopeName = arguments?.getString(ARG_SCOPE_NAME)
            if (scopeName == null) {
                scopeName = this@ProjectFlowFragment.objectScopeName()
                arguments = (arguments ?: Bundle()).apply { putString(ARG_SCOPE_NAME, scopeName) }
            }
            return scopeName
        }

    @InjectPresenter
    lateinit var presenter: ProjectFlowPresenter

    @ProvidePresenter
    fun providePresenter() =
        Toothpick.openScope(scopeName)
            .getInstance(ProjectFlowPresenter::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        prepareScope(savedInstanceState == null)
        super.onCreate(savedInstanceState)
        if (childFragmentManager.fragments.isEmpty()) {
            navigator.setLaunchScreen(Screens.ProjectMainFlow(scopeName))
        }
    }

    private fun prepareScope(firstTime: Boolean) {
        val scope = Toothpick.openScopes(DI.SERVER_SCOPE, scopeName)
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
        private const val ARG_SCOPE_NAME = "arg_scope_name"
        fun create(projectId: Long) =
            ProjectFlowFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_PROJECT_ID, projectId)
                }
            }
    }
}
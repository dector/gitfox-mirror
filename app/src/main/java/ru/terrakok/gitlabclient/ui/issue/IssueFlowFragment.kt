package ru.terrakok.gitlabclient.ui.issue

import android.os.Bundle
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import ru.terrakok.cicerone.Router
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.extension.argument
import ru.terrakok.gitlabclient.extension.hideKeyboard
import ru.terrakok.gitlabclient.extension.setLaunchScreen
import ru.terrakok.gitlabclient.presentation.issue.IssueFlowPresenter
import ru.terrakok.gitlabclient.toothpick.DI
import ru.terrakok.gitlabclient.toothpick.PrimitiveWrapper
import ru.terrakok.gitlabclient.toothpick.module.FlowNavigationModule
import ru.terrakok.gitlabclient.toothpick.qualifier.IssueId
import ru.terrakok.gitlabclient.toothpick.qualifier.ProjectId
import ru.terrakok.gitlabclient.ui.global.FlowFragment
import toothpick.Scope
import toothpick.Toothpick
import toothpick.config.Module

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 25.11.17.
 */
class IssueFlowFragment : FlowFragment(), MvpView {

    private val issueId by argument(ARG_ISSUE_ID, 0L)
    private val projectId by argument(ARG_PROJECT_ID, 0L)

    override val parentScopeName = DI.SERVER_SCOPE

    override val scopeModuleInstaller = { scope: Scope ->
        scope.installModules(
            FlowNavigationModule(scope.getInstance(Router::class.java)),
            object : Module() {
                init {
                    bind(PrimitiveWrapper::class.java)
                        .withName(IssueId::class.java)
                        .toInstance(PrimitiveWrapper(issueId))
                    bind(PrimitiveWrapper::class.java)
                        .withName(ProjectId::class.java)
                        .toInstance(PrimitiveWrapper(projectId))
                }
            }
        )
    }

    @InjectPresenter
    lateinit var presenter: IssueFlowPresenter

    @ProvidePresenter
    fun providePresenter() =
        scope.getInstance(IssueFlowPresenter::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Toothpick.inject(this, scope)
        if (childFragmentManager.fragments.isEmpty()) {
            navigator.setLaunchScreen(Screens.Issue)
        }
    }

    override fun onExit() {
        activity?.hideKeyboard()
        presenter.onExit()
    }

    companion object {
        private const val ARG_PROJECT_ID = "arg_project_id"
        private const val ARG_ISSUE_ID = "arg_issue_id"
        fun create(projectId: Long, issueId: Long) =
            IssueFlowFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_PROJECT_ID, projectId)
                    putLong(ARG_ISSUE_ID, issueId)
                }
            }
    }
}
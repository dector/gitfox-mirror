package ru.terrakok.gitlabclient.ui.issue

import android.content.Context
import android.content.Intent
import android.os.Bundle
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.model.system.flow.FlowNavigator
import ru.terrakok.gitlabclient.toothpick.DI
import ru.terrakok.gitlabclient.toothpick.PrimitiveWrapper
import ru.terrakok.gitlabclient.toothpick.qualifier.IssueId
import ru.terrakok.gitlabclient.toothpick.qualifier.ProjectId
import ru.terrakok.gitlabclient.ui.global.BaseActivity
import toothpick.Toothpick
import toothpick.config.Module

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 25.11.17.
 */
class IssueActivity : BaseActivity() {
    override val layoutRes = R.layout.activity_container
    private val issueId get() = intent.getLongExtra(ARG_ISSUE_ID, 0)
    private val projectId get() = intent.getLongExtra(ARG_PROJECT_ID, 0)

    override fun onCreate(savedInstanceState: Bundle?) {
        Toothpick.inject(this, Toothpick.openScope(DI.APP_SCOPE))
        super.onCreate(savedInstanceState)

        Toothpick.openScopes(DI.SERVER_SCOPE, DI.ISSUE_SCOPE).apply {
            installModules(object : Module() {
                init {
                    bind(PrimitiveWrapper::class.java)
                            .withName(IssueId::class.java)
                            .toInstance(PrimitiveWrapper(issueId))
                    bind(PrimitiveWrapper::class.java)
                            .withName(ProjectId::class.java)
                            .toInstance(PrimitiveWrapper(projectId))
                }
            })
        }

        if (savedInstanceState == null) {
            navigator.setLaunchScreen(Screens.ISSUE_INFO_SCREEN, null)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        if (isFinishing) Toothpick.closeScope(DI.ISSUE_SCOPE)
    }

    override val navigator = object : FlowNavigator(this, R.id.container) {

        override fun createFragment(screenKey: String?, data: Any?) = when(screenKey) {
            Screens.ISSUE_INFO_SCREEN -> IssueInfoFragment()
            else -> null
        }
    }

    companion object {
        private val ARG_PROJECT_ID = "arg_project_id"
        private val ARG_ISSUE_ID = "arg_issue_id"
        fun getStartIntent(projectId: Long, issueId: Long, context: Context) =
                Intent(context, IssueActivity::class.java).apply {
                    putExtra(ARG_PROJECT_ID, projectId)
                    putExtra(ARG_ISSUE_ID, issueId)
                }
    }
}
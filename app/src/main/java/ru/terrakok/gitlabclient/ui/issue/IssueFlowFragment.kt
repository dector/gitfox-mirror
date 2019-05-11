package ru.terrakok.gitlabclient.ui.issue

import android.os.Bundle
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.di.IssueId
import ru.terrakok.gitlabclient.di.PrimitiveWrapper
import ru.terrakok.gitlabclient.di.ProjectId
import ru.terrakok.gitlabclient.extension.argument
import ru.terrakok.gitlabclient.ui.global.FlowFragment
import toothpick.Scope
import toothpick.config.Module

class IssueFlowFragment : FlowFragment() {

    private val issueId by argument(ARG_ISSUE_ID, 0L)
    private val projectId by argument(ARG_PROJECT_ID, 0L)

    override fun installModules(scope: Scope) {
        super.installModules(scope)
        scope.installModules(
            object : Module() {
                init {
                    bind(PrimitiveWrapper::class.java)
                        .withName(ProjectId::class.java)
                        .toInstance(PrimitiveWrapper(projectId))
                    bind(PrimitiveWrapper::class.java)
                        .withName(IssueId::class.java)
                        .toInstance(PrimitiveWrapper(issueId))
                }
            }
        )
    }

    override fun getLaunchScreen() = Screens.MainIssue

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
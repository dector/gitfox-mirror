package ru.terrakok.gitlabclient.ui.issue

import android.os.Bundle
import gitfox.entity.app.target.TargetAction
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.di.IssueId
import ru.terrakok.gitlabclient.di.PrimitiveWrapper
import ru.terrakok.gitlabclient.di.ProjectId
import ru.terrakok.gitlabclient.ui.global.FlowFragment
import ru.terrakok.gitlabclient.util.argument
import toothpick.Scope
import toothpick.config.Module

class IssueFlowFragment : FlowFragment() {

    private val issueId by argument(ARG_ISSUE_ID, 0L)
    private val projectId by argument(ARG_PROJECT_ID, 0L)
    private val noteId by argument<Long?>(ARG_NOTE_ID)

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
                    bind(TargetAction::class.java)
                        .toInstance(
                            noteId?.let { TargetAction.CommentedOn(it) }
                                ?: TargetAction.Undefined
                        )
                }
            }
        )
    }

    override fun getLaunchScreen() = Screens.MainIssue

    companion object {
        private const val ARG_PROJECT_ID = "arg_project_id"
        private const val ARG_ISSUE_ID = "arg_issue_id"
        private const val ARG_NOTE_ID = "arg_note_id"

        fun create(projectId: Long, issueId: Long, targetAction: TargetAction) =
            IssueFlowFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_PROJECT_ID, projectId)
                    putLong(ARG_ISSUE_ID, issueId)
                    (targetAction as? TargetAction.CommentedOn)?.let {
                        putLong(ARG_NOTE_ID, it.noteId)
                    }
                }
            }
    }
}

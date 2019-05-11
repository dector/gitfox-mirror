package ru.terrakok.gitlabclient.ui.global

import android.os.Bundle
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.di.*
import toothpick.config.Module

object FlowFactory {

    private const val ARG_TYPE = "arg_type"
    private const val ARG_PROJECT_ID = "arg_project_id"
    private const val ARG_ISSUE_ID = "arg_issue_id"
    private const val ARG_MR_ID = "arg_mr_id"
    private const val ARG_USER_ID = "arg_user_id"

    private enum class Type {
        AUTH,
        PROJECT,
        ISSUE,
        MERGE_REQUEST,
        USER
    }

    fun createAuthFlowFragment() =
        FlowFragment().apply {
            arguments = Bundle().apply {
                putSerializable(ARG_TYPE, Type.AUTH)
            }
        }

    fun createUserFlowFragment(userId: Long) =
        FlowFragment().apply {
            arguments = Bundle().apply {
                putSerializable(ARG_TYPE, Type.USER)
                putLong(ARG_USER_ID, userId)
            }
        }

    fun createIssueFlowFragment(projectId: Long, issueId: Long) =
        FlowFragment().apply {
            arguments = Bundle().apply {
                putSerializable(ARG_TYPE, Type.ISSUE)
                putLong(ARG_PROJECT_ID, projectId)
                putLong(ARG_ISSUE_ID, issueId)
            }
        }

    fun createMergeRequestFlowFragment(projectId: Long, mrId: Long) =
        FlowFragment().apply {
            arguments = Bundle().apply {
                putSerializable(ARG_TYPE, Type.MERGE_REQUEST)
                putLong(ARG_PROJECT_ID, projectId)
                putLong(ARG_MR_ID, mrId)
            }
        }

    fun createProjectFlowFragment(projectId: Long) =
        FlowFragment().apply {
            arguments = Bundle().apply {
                putSerializable(ARG_TYPE, Type.PROJECT)
                putLong(ARG_PROJECT_ID, projectId)
            }
        }

    fun createFlowModule(arguments: Bundle) = object : Module() {
        init {
            when (arguments.getSerializable(ARG_TYPE) as Type) {
                Type.USER -> {
                    bind(PrimitiveWrapper::class.java)
                        .withName(UserId::class.java)
                        .toInstance(PrimitiveWrapper(arguments.getLong(ARG_USER_ID)))
                }
                Type.ISSUE -> {
                    bind(PrimitiveWrapper::class.java)
                        .withName(ProjectId::class.java)
                        .toInstance(PrimitiveWrapper(arguments.getLong(ARG_PROJECT_ID)))
                    bind(PrimitiveWrapper::class.java)
                        .withName(IssueId::class.java)
                        .toInstance(PrimitiveWrapper(arguments.getLong(ARG_ISSUE_ID)))
                }
                Type.MERGE_REQUEST -> {
                    bind(PrimitiveWrapper::class.java)
                        .withName(ProjectId::class.java)
                        .toInstance(PrimitiveWrapper(arguments.getLong(ARG_PROJECT_ID)))
                    bind(PrimitiveWrapper::class.java)
                        .withName(MergeRequestId::class.java)
                        .toInstance(PrimitiveWrapper(arguments.getLong(ARG_MR_ID)))
                }
                Type.PROJECT -> {
                    bind(PrimitiveWrapper::class.java)
                        .withName(ProjectId::class.java)
                        .toInstance(PrimitiveWrapper(arguments.getLong(ARG_PROJECT_ID)))
                }
            }
        }
    }

    fun getFlowRootScreen(arguments: Bundle) =
        when (arguments.getSerializable(ARG_TYPE) as Type) {
            Type.AUTH -> Screens.Auth
            Type.USER -> Screens.UserInfo
            Type.ISSUE -> Screens.Issue
            Type.MERGE_REQUEST -> Screens.MergeRequest
            Type.PROJECT -> Screens.ProjectMainFlow
        }
}
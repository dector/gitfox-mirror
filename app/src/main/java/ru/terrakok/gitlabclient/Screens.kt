package ru.terrakok.gitlabclient

import android.content.Context
import android.content.Intent
import ru.terrakok.gitlabclient.ui.auth.AuthActivity
import ru.terrakok.gitlabclient.ui.issue.IssueActivity
import ru.terrakok.gitlabclient.ui.launch.MainActivity
import ru.terrakok.gitlabclient.ui.mergerequest.MergeRequestActivity
import ru.terrakok.gitlabclient.ui.project.ProjectActivity
import ru.terrakok.gitlabclient.ui.user.UserActivity

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 26.03.17.
 */
object Screens {
    const val AUTH_FLOW = "auth flow"
    const val AUTH_SCREEN = "auth screen"

    const val MAIN_FLOW = "main flow"
    const val MAIN_SCREEN = "main screen"
    const val PROJECTS_SCREEN = "projects screen"
    const val ABOUT_SCREEN = "about screen"
    const val APP_LIBRARIES_SCREEN = "app libraries screen"

    const val PROJECT_FLOW = "project flow"
    const val PROJECT_INFO_SCREEN = "project info screen"

    const val USER_FLOW = "user flow"
    const val USER_INFO_SCREEN = "user info screen"

    const val MR_FLOW = "mr flow"
    const val MR_SCREEN = "mr screen"

    const val ISSUE_FLOW = "issue flow"
    const val ISSUE_INFO_SCREEN = "issue info screen"

    fun getFlowIntent(context: Context, flowKey: String, data: Any?): Intent? = when (flowKey) {
        Screens.AUTH_FLOW -> AuthActivity.getStartIntent(context)
        Screens.MAIN_FLOW -> MainActivity.getStartIntent(context)
        Screens.PROJECT_FLOW -> ProjectActivity.getStartIntent(data as Long, context)
        Screens.USER_FLOW -> UserActivity.getStartIntent(data as Long, context)
        Screens.MR_FLOW -> {
            val (projectId, mrId) = data as Pair<Long, Long>
            MergeRequestActivity.getStartIntent(projectId, mrId, context)
        }
        Screens.ISSUE_FLOW -> {
            val (projectId, issueId) = data as Pair<Long, Long>
            IssueActivity.getStartIntent(projectId, issueId, context)
        }
        else -> null
    }
}
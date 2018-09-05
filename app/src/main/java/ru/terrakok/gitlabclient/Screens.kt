package ru.terrakok.gitlabclient

import android.content.Intent
import android.net.Uri
import android.support.v4.app.Fragment
import ru.terrakok.gitlabclient.ui.about.AboutFragment
import ru.terrakok.gitlabclient.ui.auth.AuthFlowFragment
import ru.terrakok.gitlabclient.ui.auth.AuthFragment
import ru.terrakok.gitlabclient.ui.issue.IssueFlowFragment
import ru.terrakok.gitlabclient.ui.issue.IssueFragment
import ru.terrakok.gitlabclient.ui.launch.DrawerFlowFragment
import ru.terrakok.gitlabclient.ui.libraries.LibrariesFragment
import ru.terrakok.gitlabclient.ui.main.MainFragment
import ru.terrakok.gitlabclient.ui.mergerequest.MergeRequestFlowFragment
import ru.terrakok.gitlabclient.ui.mergerequest.MergeRequestFragment
import ru.terrakok.gitlabclient.ui.project.ProjectFlowFragment
import ru.terrakok.gitlabclient.ui.project.ProjectFragment
import ru.terrakok.gitlabclient.ui.projects.ProjectsContainerFragment
import ru.terrakok.gitlabclient.ui.user.UserFlowFragment
import ru.terrakok.gitlabclient.ui.user.info.UserInfoFragment

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 26.03.17.
 */
object Screens {
    const val DRAWER_FLOW = "drawer flow"
    const val MAIN_SCREEN = "main screen"
    const val PROJECTS_SCREEN = "projects screen"
    const val ABOUT_SCREEN = "about screen"

    const val APP_LIBRARIES_FLOW = "app libraries flow"

    const val AUTH_FLOW = "auth flow"
    const val AUTH_SCREEN = "auth screen"

    const val PROJECT_FLOW = "project flow"
    const val PROJECT_SCREEN = "project screen"

    const val USER_FLOW = "user flow"
    const val USER_INFO_SCREEN = "user info screen"

    const val MR_FLOW = "mr flow"
    const val MR_SCREEN = "mr screen"

    const val ISSUE_FLOW = "issue flow"
    const val ISSUE_SCREEN = "issue screen"

    const val EXTERNAL_BROWSER_FLOW = "external_browser_flow"
    const val SHARE_FLOW = "share_flow"
    const val CALL_FLOW = "call_flow"

    fun createIntent(flowKey: String, data: Any?) = when (flowKey) {
        Screens.EXTERNAL_BROWSER_FLOW -> Intent(Intent.ACTION_VIEW, Uri.parse(data as String))
        Screens.SHARE_FLOW -> Intent.createChooser(
            Intent(Intent.ACTION_SEND).apply {
                putExtra(Intent.EXTRA_TEXT, data as String)
                type = "text/plain"
            },
            data as String
        )
        Screens.CALL_FLOW -> Intent(Intent.ACTION_DIAL, Uri.parse("tel:$data"))
        else -> null
    }

    fun createFragment(screenKey: String, data: Any? = null): Fragment? =
        when (screenKey) {
            Screens.DRAWER_FLOW -> DrawerFlowFragment()
            Screens.MAIN_SCREEN -> MainFragment()
            Screens.PROJECTS_SCREEN -> ProjectsContainerFragment()
            Screens.ABOUT_SCREEN -> AboutFragment()

            Screens.APP_LIBRARIES_FLOW -> LibrariesFragment()

            Screens.AUTH_FLOW -> AuthFlowFragment()
            Screens.AUTH_SCREEN -> AuthFragment()

            Screens.USER_FLOW -> UserFlowFragment.create(data as Long)
            Screens.USER_INFO_SCREEN -> UserInfoFragment()

            Screens.PROJECT_FLOW -> ProjectFlowFragment.create(data as Long)
            Screens.PROJECT_SCREEN -> ProjectFragment()

            Screens.MR_FLOW -> {
                val (projectId, mrId) = data as Pair<Long, Long>
                MergeRequestFlowFragment.create(projectId, mrId)
            }
            Screens.MR_SCREEN -> MergeRequestFragment()

            Screens.ISSUE_FLOW -> {
                val (projectId, issueId) = data as Pair<Long, Long>
                IssueFlowFragment.create(projectId, issueId)
            }
            Screens.ISSUE_SCREEN -> IssueFragment()
            else -> null
        }
}
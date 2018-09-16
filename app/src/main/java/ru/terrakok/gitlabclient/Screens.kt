package ru.terrakok.gitlabclient

import android.content.Intent
import android.net.Uri
import ru.terrakok.gitlabclient.entity.issue.IssueState
import ru.terrakok.gitlabclient.entity.mergerequest.MergeRequestState
import ru.terrakok.gitlabclient.ui.about.AboutFragment
import ru.terrakok.gitlabclient.ui.auth.AuthFlowFragment
import ru.terrakok.gitlabclient.ui.auth.AuthFragment
import ru.terrakok.gitlabclient.ui.drawer.DrawerFlowFragment
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import ru.terrakok.gitlabclient.ui.issue.IssueFlowFragment
import ru.terrakok.gitlabclient.ui.issue.IssueFragment
import ru.terrakok.gitlabclient.ui.issue.IssueInfoFragment
import ru.terrakok.gitlabclient.ui.issue.IssueNotesFragment
import ru.terrakok.gitlabclient.ui.libraries.LibrariesFragment
import ru.terrakok.gitlabclient.ui.main.MainFlowFragment
import ru.terrakok.gitlabclient.ui.mergerequest.MergeRequestFlowFragment
import ru.terrakok.gitlabclient.ui.mergerequest.MergeRequestFragment
import ru.terrakok.gitlabclient.ui.mergerequest.MergeRequestInfoFragment
import ru.terrakok.gitlabclient.ui.mergerequest.MergeRequestNotesFragment
import ru.terrakok.gitlabclient.ui.my.activity.MyEventsFragment
import ru.terrakok.gitlabclient.ui.my.issues.MyIssuesContainerFragment
import ru.terrakok.gitlabclient.ui.my.issues.MyIssuesFragment
import ru.terrakok.gitlabclient.ui.my.mergerequests.MyMergeRequestsContainerFragment
import ru.terrakok.gitlabclient.ui.my.mergerequests.MyMergeRequestsFragment
import ru.terrakok.gitlabclient.ui.my.todos.MyTodosContainerFragment
import ru.terrakok.gitlabclient.ui.my.todos.MyTodosFragment
import ru.terrakok.gitlabclient.ui.project.ProjectFlowFragment
import ru.terrakok.gitlabclient.ui.project.ProjectFragment
import ru.terrakok.gitlabclient.ui.project.ProjectInfoFragment
import ru.terrakok.gitlabclient.ui.project.issues.ProjectIssuesContainerFragment
import ru.terrakok.gitlabclient.ui.project.issues.ProjectIssuesFragment
import ru.terrakok.gitlabclient.ui.project.mergerequest.ProjectMergeRequestsContainerFragment
import ru.terrakok.gitlabclient.ui.project.mergerequest.ProjectMergeRequestsFragment
import ru.terrakok.gitlabclient.ui.projects.ProjectsContainerFragment
import ru.terrakok.gitlabclient.ui.projects.ProjectsListFragment
import ru.terrakok.gitlabclient.ui.user.UserFlowFragment
import ru.terrakok.gitlabclient.ui.user.info.UserInfoFragment

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 26.03.17.
 */
object Screens {
    const val DRAWER_FLOW = "drawer flow"

    const val MAIN_FLOW = "main flow"
    const val MY_EVENTS_SCREEN = "my events screen"

    const val MY_ISSUES_CONTAINER_SCREEN = "my issues container screen"
    const val MY_ISSUES_SCREEN = "my issues screen"

    const val MY_MR_CONTAINER_SCREEN = "my mr container screen"
    const val MY_MR_SCREEN = "my mr screen"

    const val MY_TODOS_CONTAINER_SCREEN = "my todo container screen"
    const val MY_TODOS_SCREEN = "my todo screen"

    const val PROJECTS_CONTAINER_SCREEN = "projects container screen"
    const val PROJECTS_SCREEN = "projects screen"

    const val ABOUT_SCREEN = "about screen"

    const val APP_LIBRARIES_FLOW = "app libraries flow"

    const val AUTH_FLOW = "auth flow"
    const val AUTH_SCREEN = "auth screen"

    const val PROJECT_FLOW = "project flow"
    const val PROJECT_MAIN_FLOW = "project main flow"
    const val PROJECT_INFO_SCREEN = "project info screen"
    const val PROJECT_ISSUES_CONTAINER_SCREEN = "project issues container screen"
    const val PROJECT_ISSUES_SCREEN = "project issues screen"
    const val PROJECT_MR_CONTAINER_SCREEN = "project mr container screen"
    const val PROJECT_MR_SCREEN = "project mr screen"

    const val USER_FLOW = "user flow"
    const val USER_INFO_SCREEN = "user info screen"

    const val MR_FLOW = "mr flow"
    const val MR_SCREEN = "mr screen"
    const val MR_INFO_SCREEN = "mr info screen"
    const val MR_NOTES_SCREEN = "mr notes screen"

    const val ISSUE_FLOW = "issue flow"
    const val ISSUE_SCREEN = "issue screen"
    const val ISSUE_INFO_SCREEN = "issue info screen"
    const val ISSUE_NOTES_SCREEN = "issue notes screen"

    const val EXTERNAL_BROWSER_FLOW = "external browser flow"
    const val SHARE_FLOW = "share flow"
    const val CALL_FLOW = "call flow"

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

    fun createFragment(screenKey: String, data: Any? = null): BaseFragment? =
        when (screenKey) {
            Screens.DRAWER_FLOW -> DrawerFlowFragment()

            Screens.MAIN_FLOW -> MainFlowFragment()
            Screens.MY_EVENTS_SCREEN -> MyEventsFragment()

            Screens.MY_ISSUES_CONTAINER_SCREEN -> MyIssuesContainerFragment()
            Screens.MY_ISSUES_SCREEN -> {
                val (createdByMe, onlyOpened) = data as Pair<Boolean, Boolean>
                MyIssuesFragment.create(createdByMe, onlyOpened)
            }

            Screens.MY_MR_CONTAINER_SCREEN -> MyMergeRequestsContainerFragment()
            Screens.MY_MR_SCREEN -> {
                val (createdByMe, onlyOpened) = data as Pair<Boolean, Boolean>
                MyMergeRequestsFragment.create(createdByMe, onlyOpened)
            }

            Screens.MY_TODOS_CONTAINER_SCREEN -> MyTodosContainerFragment()
            Screens.MY_TODOS_SCREEN -> MyTodosFragment.create(data as Boolean)

            Screens.PROJECTS_CONTAINER_SCREEN -> ProjectsContainerFragment()
            Screens.PROJECTS_SCREEN -> ProjectsListFragment.create(data as Int)

            Screens.ABOUT_SCREEN -> AboutFragment()

            Screens.APP_LIBRARIES_FLOW -> LibrariesFragment()

            Screens.AUTH_FLOW -> AuthFlowFragment()
            Screens.AUTH_SCREEN -> AuthFragment()

            Screens.USER_FLOW -> UserFlowFragment.create(data as Long)
            Screens.USER_INFO_SCREEN -> UserInfoFragment()

            Screens.PROJECT_FLOW -> ProjectFlowFragment.create(data as Long)
            Screens.PROJECT_MAIN_FLOW -> ProjectFragment()
            Screens.PROJECT_INFO_SCREEN -> ProjectInfoFragment()
            Screens.PROJECT_ISSUES_CONTAINER_SCREEN -> ProjectIssuesContainerFragment()
            Screens.PROJECT_ISSUES_SCREEN -> ProjectIssuesFragment.create(data as IssueState)
            Screens.PROJECT_MR_CONTAINER_SCREEN -> ProjectMergeRequestsContainerFragment()
            Screens.PROJECT_MR_SCREEN -> ProjectMergeRequestsFragment.create(data as MergeRequestState)

            Screens.MR_FLOW -> {
                val (projectId, mrId) = data as Pair<Long, Long>
                MergeRequestFlowFragment.create(projectId, mrId)
            }
            Screens.MR_SCREEN -> MergeRequestFragment()
            Screens.MR_INFO_SCREEN -> MergeRequestInfoFragment()
            Screens.MR_NOTES_SCREEN -> MergeRequestNotesFragment()

            Screens.ISSUE_FLOW -> {
                val (projectId, issueId) = data as Pair<Long, Long>
                IssueFlowFragment.create(projectId, issueId)
            }
            Screens.ISSUE_SCREEN -> IssueFragment()
            Screens.ISSUE_INFO_SCREEN -> IssueInfoFragment()
            Screens.ISSUE_NOTES_SCREEN -> IssueNotesFragment()
            else -> null
        }
}
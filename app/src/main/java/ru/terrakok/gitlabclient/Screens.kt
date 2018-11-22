package ru.terrakok.gitlabclient

import android.content.Context
import android.content.Intent
import android.net.Uri
import ru.terrakok.cicerone.android.support.SupportAppScreen
import ru.terrakok.gitlabclient.entity.issue.IssueState
import ru.terrakok.gitlabclient.entity.mergerequest.MergeRequestState
import ru.terrakok.gitlabclient.ui.about.AboutFragment
import ru.terrakok.gitlabclient.ui.auth.AuthFlowFragment
import ru.terrakok.gitlabclient.ui.auth.AuthFragment
import ru.terrakok.gitlabclient.ui.drawer.DrawerFlowFragment
import ru.terrakok.gitlabclient.ui.file.ProjectFileFlowFragment
import ru.terrakok.gitlabclient.ui.file.ProjectFileFragment
import ru.terrakok.gitlabclient.ui.issue.IssueFlowFragment
import ru.terrakok.gitlabclient.ui.issue.IssueFragment
import ru.terrakok.gitlabclient.ui.issue.IssueInfoFragment
import ru.terrakok.gitlabclient.ui.issue.IssueNotesFragment
import ru.terrakok.gitlabclient.ui.libraries.LibrariesFragment
import ru.terrakok.gitlabclient.ui.main.MainFlowFragment
import ru.terrakok.gitlabclient.ui.mergerequest.*
import ru.terrakok.gitlabclient.ui.my.activity.MyEventsFragment
import ru.terrakok.gitlabclient.ui.my.issues.MyIssuesContainerFragment
import ru.terrakok.gitlabclient.ui.my.issues.MyIssuesFragment
import ru.terrakok.gitlabclient.ui.my.mergerequests.MyMergeRequestsContainerFragment
import ru.terrakok.gitlabclient.ui.my.mergerequests.MyMergeRequestsFragment
import ru.terrakok.gitlabclient.ui.my.todos.MyTodosContainerFragment
import ru.terrakok.gitlabclient.ui.my.todos.MyTodosFragment
import ru.terrakok.gitlabclient.ui.privacypolicy.PrivacyPolicyFragment
import ru.terrakok.gitlabclient.ui.project.ProjectFlowFragment
import ru.terrakok.gitlabclient.ui.project.ProjectFragment
import ru.terrakok.gitlabclient.ui.project.info.ProjectEventsFragment
import ru.terrakok.gitlabclient.ui.project.info.ProjectInfoContainerFragment
import ru.terrakok.gitlabclient.ui.project.info.ProjectInfoFragment
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
    object DrawerFlow : SupportAppScreen() {
        override fun getFragment() = DrawerFlowFragment()
    }

    object MainFlow : SupportAppScreen() {
        override fun getFragment() = MainFlowFragment()
    }

    object MyEvents : SupportAppScreen() {
        override fun getFragment() = MyEventsFragment()
    }

    object MyIssuesContainer : SupportAppScreen() {
        override fun getFragment() = MyIssuesContainerFragment()
    }

    data class MyIssues(
        val createdByMe: Boolean,
        val onlyOpened: Boolean
    ) : SupportAppScreen() {
        override fun getFragment() = MyIssuesFragment.create(createdByMe, onlyOpened)
    }

    object MyMrContainer : SupportAppScreen() {
        override fun getFragment() = MyMergeRequestsContainerFragment()
    }

    data class MyMergeRequests(
        val createdByMe: Boolean,
        val onlyOpened: Boolean
    ) : SupportAppScreen() {
        override fun getFragment() = MyMergeRequestsFragment.create(createdByMe, onlyOpened)
    }

    object MyTodosContainer : SupportAppScreen() {
        override fun getFragment() = MyTodosContainerFragment()
    }

    data class MyTodos(
        val isPending: Boolean
    ) : SupportAppScreen() {
        override fun getFragment() = MyTodosFragment.create(isPending)
    }

    object ProjectsContainer : SupportAppScreen() {
        override fun getFragment() = ProjectsContainerFragment()
    }

    data class Projects(
        val mode: Int
    ) : SupportAppScreen() {
        override fun getFragment() = ProjectsListFragment.create(mode)
    }

    object About : SupportAppScreen() {
        override fun getFragment() = AboutFragment()
    }

    object Libraries : SupportAppScreen() {
        override fun getFragment() = LibrariesFragment()
    }

    object AuthFlow : SupportAppScreen() {
        override fun getFragment() = AuthFlowFragment()
    }

    object Auth : SupportAppScreen() {
        override fun getFragment() = AuthFragment()
    }

    data class ProjectFlow(
        val projectId: Long
    ) : SupportAppScreen() {
        override fun getFragment() = ProjectFlowFragment.create(projectId)
    }

    data class ProjectMainFlow(
        val scope: String
    ) : SupportAppScreen() {
        override fun getFragment() = ProjectFragment.create(scope)
    }

    data class ProjectInfoContainer(
        val scope: String
    ) : SupportAppScreen() {
        override fun getFragment() = ProjectInfoContainerFragment.create(scope)
    }

    data class ProjectInfo(
        val scope: String
    ) : SupportAppScreen() {
        override fun getFragment() = ProjectInfoFragment.create(scope)
    }

    data class ProjectEvents(
        val scope: String
    ) : SupportAppScreen() {
        override fun getFragment() = ProjectEventsFragment.create(scope)
    }

    data class ProjectIssuesContainer(
        val scope: String
    ) : SupportAppScreen() {
        override fun getFragment() = ProjectIssuesContainerFragment.create(scope)
    }

    data class ProjectIssues(
        val issueState: IssueState,
        val scope: String
    ) : SupportAppScreen() {
        override fun getFragment() = ProjectIssuesFragment.create(issueState, scope)
    }

    data class ProjectMergeRequestsContainer(
        val scope: String
    ) : SupportAppScreen() {
        override fun getFragment() = ProjectMergeRequestsContainerFragment.create(scope)
    }

    data class ProjectMergeRequests(
        val mrState: MergeRequestState,
        val scope: String
    ) : SupportAppScreen() {
        override fun getFragment() = ProjectMergeRequestsFragment.create(mrState, scope)
    }

    data class UserFlow(
        val userId: Long
    ) : SupportAppScreen() {
        override fun getFragment() = UserFlowFragment.create(userId)
    }

    object UserInfo : SupportAppScreen() {
        override fun getFragment() = UserInfoFragment()
    }

    data class MergeRequestFlow(
        val projectId: Long,
        val mrId: Long
    ) : SupportAppScreen() {
        override fun getFragment() = MergeRequestFlowFragment.create(projectId, mrId)
    }

    object MergeRequest : SupportAppScreen() {
        override fun getFragment() = MergeRequestFragment()
    }

    object MergeRequestInfo : SupportAppScreen() {
        override fun getFragment() = MergeRequestInfoFragment()
    }

    object MergeRequestCommits : SupportAppScreen() {
        override fun getFragment() = MergeRequestCommitsFragment()
    }

    object MergeRequestNotes : SupportAppScreen() {
        override fun getFragment() = MergeRequestNotesFragment()
    }

    object MergeRequestChanges : SupportAppScreen() {
        override fun getFragment() = MergeRequestChangesFragment()
    }

    data class IssueFlow(
        val projectId: Long,
        val issueId: Long
    ) : SupportAppScreen() {
        override fun getFragment() = IssueFlowFragment.create(projectId, issueId)
    }

    object Issue : SupportAppScreen() {
        override fun getFragment() = IssueFragment()
    }

    object IssueInfo : SupportAppScreen() {
        override fun getFragment() = IssueInfoFragment()
    }

    object IssueNotes : SupportAppScreen() {
        override fun getFragment() = IssueNotesFragment()
    }

    object PrivacyPolicy : SupportAppScreen() {
        override fun getFragment() = PrivacyPolicyFragment()
    }

    data class ProjectFileFlow(
        val projectId: Long,
        val fileName: String,
        val filePath: String,
        val branchName: String
    ) : SupportAppScreen() {
        override fun getFragment() = ProjectFileFlowFragment.create(projectId, fileName, filePath, branchName)
    }

    object ProjectFile : SupportAppScreen() {
        override fun getFragment() = ProjectFileFragment()
    }

    data class ExternalBrowserFlow(
        val url: String
    ) : SupportAppScreen() {
        override fun getActivityIntent(context: Context?) =
            Intent(Intent.ACTION_VIEW, Uri.parse(url))
    }

    data class ShareFlow(
        val text: String
    ) : SupportAppScreen() {
        override fun getActivityIntent(context: Context?) =
            Intent.createChooser(
                Intent(Intent.ACTION_SEND).apply {
                    putExtra(Intent.EXTRA_TEXT, text)
                    type = "text/plain"
                },
                text
            )
    }
}
package ru.terrakok.gitlabclient

import android.content.Context
import android.content.Intent
import android.net.Uri
import ru.terrakok.cicerone.android.support.SupportAppScreen
import ru.terrakok.gitlabclient.entity.issue.IssueState
import ru.terrakok.gitlabclient.entity.mergerequest.MergeRequestState
import ru.terrakok.gitlabclient.entity.milestone.MilestoneState
import ru.terrakok.gitlabclient.ui.about.AboutFragment
import ru.terrakok.gitlabclient.ui.auth.AuthFlowFragment
import ru.terrakok.gitlabclient.ui.auth.AuthFragment
import ru.terrakok.gitlabclient.ui.drawer.DrawerFlowFragment
import ru.terrakok.gitlabclient.ui.global.StubFragment
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
import ru.terrakok.gitlabclient.ui.project.files.ProjectFilesFragment
import ru.terrakok.gitlabclient.ui.project.info.ProjectEventsFragment
import ru.terrakok.gitlabclient.ui.project.info.ProjectInfoContainerFragment
import ru.terrakok.gitlabclient.ui.project.info.ProjectInfoFragment
import ru.terrakok.gitlabclient.ui.project.issues.ProjectIssuesContainerFragment
import ru.terrakok.gitlabclient.ui.project.issues.ProjectIssuesFragment
import ru.terrakok.gitlabclient.ui.project.labels.ProjectLabelsFragment
import ru.terrakok.gitlabclient.ui.project.mergerequest.ProjectMergeRequestsContainerFragment
import ru.terrakok.gitlabclient.ui.project.mergerequest.ProjectMergeRequestsFragment
import ru.terrakok.gitlabclient.ui.project.milestones.ProjectMilestonesContainerFragment
import ru.terrakok.gitlabclient.ui.project.milestones.ProjectMilestonesFragment
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

    object ProjectMainFlow : SupportAppScreen() {
        override fun getFragment() = ProjectFragment()
    }

    object ProjectInfoContainer : SupportAppScreen() {
        override fun getFragment() = ProjectInfoContainerFragment()
    }

    object ProjectInfo : SupportAppScreen() {
        override fun getFragment() = ProjectInfoFragment()
    }

    object ProjectEvents : SupportAppScreen() {
        override fun getFragment() = ProjectEventsFragment()
    }

    object ProjectIssuesContainer : SupportAppScreen() {
        override fun getFragment() = ProjectIssuesContainerFragment()
    }

    data class ProjectIssues(
        val issueState: IssueState
    ) : SupportAppScreen() {
        override fun getFragment() = ProjectIssuesFragment.create(issueState)
    }

    object ProjectMergeRequestsContainer : SupportAppScreen() {
        override fun getFragment() = ProjectMergeRequestsContainerFragment()
    }

    data class ProjectMergeRequests(
        val mrState: MergeRequestState
    ) : SupportAppScreen() {
        override fun getFragment() = ProjectMergeRequestsFragment.create(mrState)
    }

    object ProjectLabels : SupportAppScreen() {
        override fun getFragment() = ProjectLabelsFragment()
    }

    object ProjectMilestonesContainer : SupportAppScreen() {
        override fun getFragment() = ProjectMilestonesContainerFragment()
    }

    data class ProjectMilestones(
        val milestoneState: MilestoneState
    ) : SupportAppScreen() {
        override fun getFragment() = ProjectMilestonesFragment.create(milestoneState)
    }

    object ProjectFiles : SupportAppScreen() {
        override fun getFragment() = ProjectFilesFragment()
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

    data class MilestoneFlow(
        val milestoneId: Long
    ) : SupportAppScreen() {
        //todo: implement milestone flow.
        override fun getFragment() = StubFragment()
    }
}
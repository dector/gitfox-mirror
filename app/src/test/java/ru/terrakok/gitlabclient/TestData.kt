package ru.terrakok.gitlabclient

import org.threeten.bp.LocalDateTime
import org.threeten.bp.Month
import ru.terrakok.gitlabclient.entity.*
import ru.terrakok.gitlabclient.entity.app.session.UserAccount
import ru.terrakok.gitlabclient.entity.app.target.*
import ru.terrakok.gitlabclient.entity.event.EventAction
import ru.terrakok.gitlabclient.entity.event.EventTargetType
import ru.terrakok.gitlabclient.entity.mergerequest.MergeRequest
import ru.terrakok.gitlabclient.entity.mergerequest.MergeRequestState
import ru.terrakok.gitlabclient.entity.milestone.Milestone
import ru.terrakok.gitlabclient.entity.milestone.MilestoneState
import ru.terrakok.gitlabclient.entity.target.TargetState
import ru.terrakok.gitlabclient.entity.target.TargetType
import ru.terrakok.gitlabclient.entity.target.issue.Issue
import ru.terrakok.gitlabclient.entity.todo.Todo
import ru.terrakok.gitlabclient.entity.todo.TodoAction
import ru.terrakok.gitlabclient.entity.todo.TodoState

/**
 * @author Vitaliy Belyaev on 01.06.2019.
 */
object TestData {

    fun getCommit() = Commit(
        "dsf233fef2fes34",
        "dsf233",
        "commit title",
        "Mr Maintainer",
        null,
        LocalDateTime.of(2007, Month.JULY, 14, 11, 0),
        null,
        null,
        null,
        LocalDateTime.of(2007, Month.JUNE, 23, 11, 0),
        "commit message",
        listOf("wqdwqe23s3e", "fesfesf232")
    )

    fun getNote() = Note(
        13L,
        "note test body",
        Author(11L, "state", "url", "name", "avatar", "username"),
        LocalDateTime.of(2007, Month.DECEMBER, 14, 11, 0),
        null,
        false,
        435L,
        EventTargetType.ISSUE,
        333L)

    fun getProject(projectId: Long) = Project(
        id = projectId,
        description = null,
        defaultBranch = "test_br",
        visibility = Visibility.PUBLIC,
        sshUrlToRepo = null,
        httpUrlToRepo = null,
        webUrl = "https://gitlab.com/terrakok/gitlab-client",
        tagList = null,
        owner = null,
        name = "",
        nameWithNamespace = "",
        path = "test path",
        pathWithNamespace = "",
        issuesEnabled = false,
        openIssuesCount = 0L,
        mergeRequestsEnabled = false,
        jobsEnabled = false,
        wikiEnabled = false,
        snippetsEnabled = false,
        containerRegistryEnabled = false,
        createdAt = null,
        lastActivityAt = null,
        creatorId = 0L,
        namespace = null,
        permissions = null,
        archived = false,
        avatarUrl = null,
        sharedRunnersEnabled = false,
        forksCount = 0L,
        starCount = 0L,
        runnersToken = null,
        publicJobs = false,
        sharedWithGroups = null,
        onlyAllowMergeIfPipelineSucceeds = false,
        onlyAllowMergeIfAllDiscussionsAreResolved = false,
        requestAccessEnabled = false,
        readmeUrl = "https://gitlab.com/terrakok/gitlab-client/blob/test_br/README.md"
    )

    fun getMergeRequest() = MergeRequest(
        321123L,
        5555L,
        LocalDateTime.of(2007, Month.DECEMBER, 14, 11, 0),
        null,
        "target branch",
        "source branch",
        9999L,
        "MR title",
        MergeRequestState.OPENED,
        2,
        1,
        Author(11L, "state", "url", "name", "avatar", "username"),
        null,
        8888,
        7777,
        "description",
        false,
        null,
        true,
        null,
        "sha",
        null,
        3,
        true,
        false,
        null,
        null,
        listOf("label 1", "label 2"),
        null,
        null,
        null,
        null,
        null
    )

    fun getExpectedTargetHeader(mr: MergeRequest, project: Project): TargetHeader {
        val badges = mutableListOf<TargetBadge>()
        badges.add(
            TargetBadge.Status(
                when (mr.state) {
                    MergeRequestState.OPENED -> TargetBadgeStatus.OPENED
                    MergeRequestState.CLOSED -> TargetBadgeStatus.CLOSED
                    MergeRequestState.MERGED -> TargetBadgeStatus.MERGED
                }
            )
        )
        badges.add(TargetBadge.Text(project.name, AppTarget.PROJECT, project.id))
        badges.add(TargetBadge.Text(mr.author.username, AppTarget.USER, mr.author.id))
        badges.add(TargetBadge.Icon(TargetBadgeIcon.COMMENTS, mr.userNotesCount))
        badges.add(TargetBadge.Icon(TargetBadgeIcon.UP_VOTES, mr.upvotes))
        badges.add(TargetBadge.Icon(TargetBadgeIcon.DOWN_VOTES, mr.downvotes))
        mr.labels.forEach { label -> badges.add(TargetBadge.Text(label)) }

        return TargetHeader.Public(
            mr.author,
            TargetHeaderIcon.NONE,
            TargetHeaderTitle.Event(
                mr.author.name,
                EventAction.CREATED,
                "${AppTarget.MERGE_REQUEST} !${mr.iid}",
                project.name
            ),
            mr.title ?: "",
            mr.createdAt,
            AppTarget.MERGE_REQUEST,
            mr.id,
            TargetInternal(mr.projectId, mr.iid),
            badges,
            TargetAction.Undefined
        )
    }

    fun getFile() = File(
        "file name",
        "file path",
        500L,
        "encoding",
        "file content",
        "file branch",
        "blob id",
        "commit id",
        "last commit id"
    )

    fun getRepositoryTreeNode() = RepositoryTreeNode(
        "some id",
        "some name",
        RepositoryTreeNodeType.TREE,
        "some path",
        "mode"
    )

    fun getBranch() = Branch(
        "some name",
        merged = false,
        protected = false,
        default = true,
        developersCanPush = true,
        developersCanMerge = true,
        canPush = false)

    fun getMilestone() = Milestone(
        123L,
        321L,
        333L,
        "milestone description",
        MilestoneState.ACTIVE,
        null,
        null,
        null,
        "milestone title",
        null,
        "url of milestone"
    )

    fun getUserAccount() = UserAccount(
        13L,
        "token",
        "user_server_path",
        "user_avatar_url",
        "user_name",
        true
    )

    fun getTodo() = Todo(
        3332L,
        getProject(123L),
        Author(12L, null, "url", "Mr", null, "commitman"),
        TodoAction.ASSIGNED,
        TargetType.ISSUE,
        Issue(),
        "target url",
        "body",
        TodoState.PENDING,
        LocalDateTime.of(2007, Month.DECEMBER, 14, 11, 0)
    )

    fun getTargetHeaderForTodo(todo: Todo): TargetHeader {
        val target = todo.target

        val appTarget = when (todo.targetType) {
            TargetType.MERGE_REQUEST -> AppTarget.MERGE_REQUEST
            TargetType.ISSUE -> AppTarget.ISSUE
        }
        val targetName = when (todo.targetType) {
            TargetType.MERGE_REQUEST -> "${AppTarget.MERGE_REQUEST} !${target.iid}"
            TargetType.ISSUE -> "${AppTarget.ISSUE} #${target.iid}"
        }
        val badges = mutableListOf<TargetBadge>()
        badges.add(
            TargetBadge.Status(
                when (target.state) {
                    TargetState.OPENED -> TargetBadgeStatus.OPENED
                    TargetState.CLOSED -> TargetBadgeStatus.CLOSED
                    TargetState.MERGED -> TargetBadgeStatus.MERGED
                }
            )
        )
        badges.add(TargetBadge.Text(todo.author.username, AppTarget.USER, todo.author.id))
        badges.add(TargetBadge.Text(todo.project.name, AppTarget.PROJECT, todo.project.id))

        return TargetHeader.Public(
            todo.author,
            TargetHeaderIcon.NONE,
            TargetHeaderTitle.Todo(
                todo.author.name,
                null,
                todo.actionName,
                targetName,
                todo.project.nameWithNamespace,
                false,
                false
            ),
            todo.body,
            todo.createdAt,
            appTarget,
            target.id,
            TargetInternal(target.projectId, target.iid),
            badges,
            TargetAction.Undefined
        )
    }
}
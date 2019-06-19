package ru.terrakok.gitlabclient

import com.google.gson.GsonBuilder
import org.threeten.bp.LocalDateTime
import org.threeten.bp.Month
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime
import ru.terrakok.gitlabclient.entity.*
import ru.terrakok.gitlabclient.entity.app.session.UserAccount
import ru.terrakok.gitlabclient.entity.app.target.*
import ru.terrakok.gitlabclient.entity.event.EventAction
import ru.terrakok.gitlabclient.entity.event.EventTargetType
import ru.terrakok.gitlabclient.entity.mergerequest.MergeRequest
import ru.terrakok.gitlabclient.entity.mergerequest.MergeRequestMergeStatus
import ru.terrakok.gitlabclient.entity.mergerequest.MergeRequestState
import ru.terrakok.gitlabclient.entity.milestone.Milestone
import ru.terrakok.gitlabclient.entity.milestone.MilestoneState
import ru.terrakok.gitlabclient.entity.target.TargetState
import ru.terrakok.gitlabclient.entity.target.TargetType
import ru.terrakok.gitlabclient.entity.todo.Todo
import ru.terrakok.gitlabclient.entity.todo.TodoAction
import ru.terrakok.gitlabclient.model.data.server.deserializer.TodoDeserializer
import ru.terrakok.gitlabclient.model.data.server.deserializer.ZonedDateTimeDeserializer

/**
 * @author Vitaliy Belyaev on 01.06.2019.
 */
object TestData {

    val todoJson =
            """
      {
        "id": 102,
        "project": {
          "id": 2,
          "name": "Gitlab Ce",
          "name_with_namespace": "Gitlab Org / Gitlab Ce",
          "path": "gitlab-ce",
          "path_with_namespace": "gitlab-org/gitlab-ce"
        },
        "author": {
          "name": "Administrator",
          "username": "root",
          "id": 1,
          "state": "active",
          "avatar_url": "http://www.gravatar.com/avatar/e64c7d89f26bd1972efa854d13d7dd61?s=80&d=identicon",
          "web_url": "https://gitlab.example.com/root"
        },
        "action_name": "marked",
        "target_type": "MergeRequest",
        "target": {
          "id": 34,
          "iid": 7,
          "project_id": 2,
          "title": "Dolores in voluptatem tenetur praesentium omnis repellendus voluptatem quaerat.",
          "description": "Et ea et omnis illum cupiditate. Dolor aspernatur tenetur ducimus facilis est nihil. Quo esse cupiditate molestiae illo corrupti qui quidem dolor.",
          "state": "opened",
          "created_at": "2016-06-17T07:49:24.419Z",
          "updated_at": "2016-06-17T07:52:43.484Z",
          "target_branch": "tutorials_git_tricks",
          "source_branch": "DNSBL_docs",
          "upvotes": 0,
          "downvotes": 0,
          "author": {
            "name": "Maxie Medhurst",
            "username": "craig_rutherford",
            "id": 12,
            "state": "active",
            "avatar_url": "http://www.gravatar.com/avatar/a0d477b3ea21970ce6ffcbb817b0b435?s=80&d=identicon",
            "web_url": "https://gitlab.example.com/craig_rutherford"
          },
          "assignee": {
            "name": "Administrator",
            "username": "root",
            "id": 1,
            "state": "active",
            "avatar_url": "http://www.gravatar.com/avatar/e64c7d89f26bd1972efa854d13d7dd61?s=80&d=identicon",
            "web_url": "https://gitlab.example.com/root"
          },
          "source_project_id": 2,
          "target_project_id": 2,
          "labels": [],
          "work_in_progress": false,
          "milestone": {
            "id": 32,
            "iid": 2,
            "project_id": 2,
            "title": "v1.0",
            "description": "Assumenda placeat ea voluptatem voluptate qui.",
            "state": "active",
            "created_at": "2016-06-17T07:47:34.163Z",
            "updated_at": "2016-06-17T07:47:34.163Z",
            "due_date": null
          },
          "merge_when_pipeline_succeeds": false,
          "merge_status": "cannot_be_merged",
          "subscribed": true,
          "user_notes_count": 7
        },
        "target_url": "https://gitlab.example.com/gitlab-org/gitlab-ce/merge_requests/7",
        "body": "Dolores in voluptatem tenetur praesentium omnis repellendus voluptatem quaerat.",
        "state": "pending",
        "created_at": "2016-06-17T07:52:35.225Z"
      }
      """

    fun getTestDate(): ZonedDateTime = ZonedDateTime.of(LocalDateTime.of(
            2007, Month.JULY, 14, 11, 0), ZoneOffset.UTC)

    fun getCommit() = Commit(
            "dsf233fef2fes34",
            "dsf233",
            "commit title",
            "Mr Maintainer",
            null,
            getTestDate(),
            null,
            null,
            null,
            getTestDate(),
            "commit message",
            listOf("wqdwqe23s3e", "fesfesf232")
    )

    fun getNote() = Note(
            13L,
            "note test body",
            ShortUser(11L, "state", "name", "url", "avatar", "username"),
            getTestDate(),
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
            name = "GitFox",
            nameWithNamespace = "",
            path = "test path",
            pathWithNamespace = "terrakok/gitlab-client",
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
            getTestDate(),
            null,
            "target branch",
            "source branch",
            9999L,
            "MR title",
            MergeRequestState.OPENED,
            2,
            1,
            ShortUser(11L, "state", "name", "url", "avatar", "username"),
            null,
            8888,
            7777,
            "description",
            false,
            null,
            true,
            MergeRequestMergeStatus.CAN_BE_MERGED,
            "sha",
            null,
            3,
            true,
            false,
            null,
            listOf("label 1", "label 2"),
            null,
            null,
            null,
            null,
            null,
            null,
            TimeStats(43, 34, null, null),
            false
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

    fun getFile(content: String = "file content", branch: String = "test_br") = File(
            "file name",
            "file path",
            500L,
            "encoding",
            content,
            branch,
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

    fun getLabel(id: Long = 555L, subscribed: Boolean = false) = Label(
            id = id,
            name = "name$id",
            color = Color("green", 12),
            description = "description",
            openIssuesCount = 0,
            closedIssuesCount = 0,
            openMergeRequestsCount = 0,
            subscribed = subscribed,
            priority = null
    )

    fun getUserAccount() = UserAccount(
            13L,
            "token",
            "user_server_path",
            "user_avatar_url",
            "user_name",
            true
    )

    fun getTodo() = GsonBuilder()
            .registerTypeAdapter(ZonedDateTime::class.java, ZonedDateTimeDeserializer())
            .registerTypeAdapter(Todo::class.java, TodoDeserializer())
            .create().fromJson(todoJson, Todo::class.java)

    fun getUser() = User(
            4321L, "username", "email@mail.com", "Name", null,
            null, null,
            getTestDate(),
            false, null, null, null, null, null,
            null, null,
            getTestDate(),
            getTestDate(),
            2424L, 32L,
            getTestDate(),
            null, canCreateGroup = false, canCreateProject = false,
            twoFactorEnabled = true, external = false
    )

    fun getTargetHeaderForTodo(todo: Todo, currentUser: User): TargetHeader {
        val target = todo.target
        val assignee = if (todo.actionName != TodoAction.MARKED) {
            currentUser.let {
                ShortUser(it.id, it.state, it.name, it.webUrl, it.avatarUrl, it.username)
            }
        } else {
            null
        }
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
                        assignee?.name,
                        todo.actionName,
                        targetName,
                        todo.project.nameWithNamespace,
                        todo.author.id == currentUser.id,
                        assignee?.id == currentUser.id
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
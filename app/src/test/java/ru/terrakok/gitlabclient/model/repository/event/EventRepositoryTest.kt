package ru.terrakok.gitlabclient.model.repository.event

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.anyOrNull
import io.reactivex.Single
import org.junit.Test
import org.mockito.Mockito.*
import org.threeten.bp.LocalDateTime
import org.threeten.bp.Month
import ru.terrakok.gitlabclient.TestSchedulers
import ru.terrakok.gitlabclient.di.PrimitiveWrapper
import ru.terrakok.gitlabclient.entity.*
import ru.terrakok.gitlabclient.entity.app.target.*
import ru.terrakok.gitlabclient.entity.event.Event
import ru.terrakok.gitlabclient.entity.event.EventAction
import ru.terrakok.gitlabclient.entity.event.EventTargetType
import ru.terrakok.gitlabclient.model.data.server.GitlabApi
import ru.terrakok.gitlabclient.model.data.server.MarkDownUrlResolver

/**
 * @author Vitaliy Belyaev on 21.05.2019.
 */
class EventRepositoryTest {
    private val defaultPageSize = 1
    private val testPage = 2
    private val testAuthor = Author(1L, "", "", "", "", "")
    private val testEvent = getTestEvent()
    private val brokenEvent = testEvent.copy(targetType = EventTargetType.NOTE, note = null)
    private val testEvents = listOf(testEvent, brokenEvent)
    private val testProject = getTestProject(testEvent.projectId)
    private val testBeforeDay = LocalDateTime.of(2007, Month.DECEMBER, 14, 11, 0)
    private val testAfterDay = LocalDateTime.of(2007, Month.DECEMBER, 24, 12, 0)
    private val expectedTargetHeaders = listOf(getExpectedTargetHeaderForIssue(testEvent))

    private val api = mock(GitlabApi::class.java)
    private val markDownUrlResolver = mock(MarkDownUrlResolver::class.java)
    private val repository = EventRepository(
            api,
            TestSchedulers(),
            PrimitiveWrapper(defaultPageSize),
            markDownUrlResolver)

    @Test
    fun `get events success`() {
        `when`(api.getEvents(
                anyOrNull(),
                anyOrNull(),
                anyOrNull(),
                anyOrNull(),
                anyOrNull(),
                anyOrNull(),
                anyInt(),
                anyInt())).thenReturn(Single.just(testEvents))

        `when`(api.getProject(anyLong(), anyBoolean())).thenReturn(Single.just(testProject))

        val testObserver = repository.getEvents(
                page = testPage,
                beforeDay = testBeforeDay,
                afterDay = testAfterDay).test()
        testObserver.awaitTerminalEvent()

        verify(api, times(1)).getEvents(
                null,
                null,
                "2007-12-14",
                "2007-12-24",
                Sort.DESC,
                OrderBy.UPDATED_AT,
                testPage,
                defaultPageSize
        )
        verify(api, times(1)).getProject(testEvent.projectId)
        verifyNoMoreInteractions(api)

        testObserver
                .assertResult(expectedTargetHeaders)

    }

    @Test
    fun `handle empty events list success`() {
        `when`(api.getEvents(
                anyOrNull(),
                anyOrNull(),
                anyOrNull(),
                anyOrNull(),
                anyOrNull(),
                anyOrNull(),
                anyInt(),
                anyInt())).thenReturn(Single.just(emptyList()))

        val testObserver = repository.getEvents(
                page = testPage,
                beforeDay = testBeforeDay,
                afterDay = testAfterDay).test()
        testObserver.awaitTerminalEvent()

        verify(api, times(1)).getEvents(
                null,
                null,
                "2007-12-14",
                "2007-12-24",
                Sort.DESC,
                OrderBy.UPDATED_AT,
                testPage,
                defaultPageSize
        )
        verifyNoMoreInteractions(api)

        testObserver
                .assertResult(emptyList())

    }

    @Test
    fun `handle get events error success`() {
        val error = RuntimeException()

        `when`(api.getEvents(
                anyOrNull(),
                anyOrNull(),
                anyOrNull(),
                anyOrNull(),
                anyOrNull(),
                anyOrNull(),
                anyInt(),
                anyInt())).thenReturn(Single.error(error))

        val testObserver = repository.getEvents(
                page = testPage,
                beforeDay = testBeforeDay,
                afterDay = testAfterDay).test()
        testObserver.awaitTerminalEvent()

        verify(api, times(1)).getEvents(
                null,
                null,
                "2007-12-14",
                "2007-12-24",
                Sort.DESC,
                OrderBy.UPDATED_AT,
                testPage,
                defaultPageSize
        )
        verifyNoMoreInteractions(api)

        testObserver
                .assertNoValues()
                .assertError(error)
                .assertNotComplete()
    }

    @Test
    fun `get project events success`() {
        `when`(api.getProjectEvents(
                anyLong(),
                anyOrNull(),
                anyOrNull(),
                anyOrNull(),
                anyOrNull(),
                anyOrNull(),
                anyOrNull(),
                anyInt(),
                anyInt())).thenReturn(Single.just(testEvents))

        `when`(api.getProject(anyLong(), anyBoolean())).thenReturn(Single.just(testProject))

        val testObserver = repository.getProjectEvents(
                projectId = testProject.id,
                page = testPage,
                beforeDay = testBeforeDay,
                afterDay = testAfterDay).test()
        testObserver.awaitTerminalEvent()

        verify(api, times(1)).getProjectEvents(
                testProject.id,
                null,
                null,
                "2007-12-14",
                "2007-12-24",
                Sort.DESC,
                OrderBy.UPDATED_AT,
                testPage,
                defaultPageSize
        )
        verify(api, times(1)).getProject(testEvent.projectId)
        verifyNoMoreInteractions(api)

        testObserver
                .assertResult(expectedTargetHeaders)
    }

    @Test
    fun `handle empty project events success`() {
        `when`(api.getProjectEvents(
                anyLong(),
                anyOrNull(),
                anyOrNull(),
                anyOrNull(),
                anyOrNull(),
                anyOrNull(),
                anyOrNull(),
                anyInt(),
                anyInt())).thenReturn(Single.just(emptyList()))

        val testObserver = repository.getProjectEvents(
                projectId = testProject.id,
                page = testPage,
                beforeDay = testBeforeDay,
                afterDay = testAfterDay).test()
        testObserver.awaitTerminalEvent()

        verify(api, times(1)).getProjectEvents(
                testProject.id,
                null,
                null,
                "2007-12-14",
                "2007-12-24",
                Sort.DESC,
                OrderBy.UPDATED_AT,
                testPage,
                defaultPageSize
        )
        verifyNoMoreInteractions(api)

        testObserver
                .assertResult(emptyList())
    }

    @Test
    fun `handle get project events error success`() {
        val error = RuntimeException()

        `when`(api.getProjectEvents(
                anyLong(),
                anyOrNull(),
                anyOrNull(),
                anyOrNull(),
                anyOrNull(),
                anyOrNull(),
                anyOrNull(),
                anyInt(),
                anyInt())).thenReturn(Single.error(error))


        val testObserver = repository.getProjectEvents(
                projectId = testProject.id,
                page = testPage,
                beforeDay = testBeforeDay,
                afterDay = testAfterDay).test()
        testObserver.awaitTerminalEvent()

        verify(api, times(1)).getProjectEvents(
                testProject.id,
                null,
                null,
                "2007-12-14",
                "2007-12-24",
                Sort.DESC,
                OrderBy.UPDATED_AT,
                testPage,
                defaultPageSize
        )
        verifyNoMoreInteractions(api)

        testObserver
                .assertNoValues()
                .assertError(error)
                .assertNotComplete()
    }

    @Test
    fun `handle note event success`() {
        val diffNoteEvent = testEvent.copy(targetType = EventTargetType.DIFF_NOTE)
        val testEvents = listOf(diffNoteEvent, brokenEvent)
        val resolvedBody = "resolved body"
        val expectedTargetHeader = getExpectedTargetHeaderForDiffNote(diffNoteEvent, resolvedBody)

        `when`(api.getEvents(
                anyOrNull(),
                anyOrNull(),
                anyOrNull(),
                anyOrNull(),
                anyOrNull(),
                anyOrNull(),
                anyInt(),
                anyInt())).thenReturn(Single.just(testEvents))

        `when`(api.getProject(anyLong(), anyBoolean())).thenReturn(Single.just(testProject))
        `when`(markDownUrlResolver.resolve(anyString(), any())).thenReturn(resolvedBody)

        val testObserver = repository.getEvents(
                page = testPage,
                beforeDay = testBeforeDay,
                afterDay = testAfterDay).test()
        testObserver.awaitTerminalEvent()

        verify(api, times(1)).getEvents(
                null,
                null,
                "2007-12-14",
                "2007-12-24",
                Sort.DESC,
                OrderBy.UPDATED_AT,
                testPage,
                defaultPageSize
        )
        verify(api, times(1)).getProject(testEvent.projectId)
        verify(markDownUrlResolver, times(1))
                .resolve(diffNoteEvent.note!!.body, testProject)
        verifyNoMoreInteractions(api)

        testObserver
                .assertResult(listOf(expectedTargetHeader))

    }

    private fun getTestProject(projectId: Long) = Project(
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

    private fun getTestEvent() = Event(
            123L,
            EventAction.CREATED,
            321L,
            4331L,
            EventTargetType.ISSUE,
            666L,
            "title of issue",
            LocalDateTime.of(2018, Month.DECEMBER, 13, 12, 0),
            testAuthor,
            "author",
            null,
            getTestNote()
    )

    private fun getTestNote() = Note(
            13L,
            "note test body",
            testAuthor,
            LocalDateTime.of(2007, Month.DECEMBER, 14, 11, 0),
            null,
            false,
            435L,
            EventTargetType.ISSUE,
            333L)

    private fun getExpectedTargetHeaderForIssue(event: Event): TargetHeader {
        return TargetHeader.Public(
                event.author,
                TargetHeaderIcon.CREATED,
                TargetHeaderTitle.Event(
                        event.author.name,
                        event.actionName,
                        "${AppTarget.ISSUE} #${event.targetIid!!}",
                        testProject.name),
                event.targetTitle!!,
                event.createdAt,
                AppTarget.ISSUE,
                event.targetId!!,
                TargetInternal(event.projectId, event.targetIid!!),
                listOf(TargetBadge.Text(testProject.name, AppTarget.PROJECT, testProject.id),
                        TargetBadge.Text(event.author.username, AppTarget.USER, event.author.id)),
                TargetAction.Undefined
        )
    }

    private fun getExpectedTargetHeaderForDiffNote(event: Event, resolvedBody: String): TargetHeader {
        return TargetHeader.Public(
                event.author,
                TargetHeaderIcon.CREATED,
                TargetHeaderTitle.Event(
                        event.author.name,
                        event.actionName,
                        "${AppTarget.ISSUE} #${event.note!!.noteableIid}",
                        testProject.name),
                resolvedBody,
                event.createdAt,
                AppTarget.ISSUE,
                event.note!!.noteableId,
                TargetInternal(event.projectId, event.note!!.noteableIid),
                listOf(TargetBadge.Text(testProject.name, AppTarget.PROJECT, testProject.id),
                        TargetBadge.Text(event.author.username, AppTarget.USER, event.author.id)),
                TargetAction.Undefined
        )
    }
}
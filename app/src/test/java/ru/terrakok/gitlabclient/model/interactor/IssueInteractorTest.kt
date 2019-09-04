package ru.terrakok.gitlabclient.model.interactor

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.anyOrNull
import io.reactivex.Single
import org.junit.Test
import org.mockito.BDDMockito.anyBoolean
import org.mockito.BDDMockito.anyInt
import org.mockito.BDDMockito.anyLong
import org.mockito.BDDMockito.anyString
import org.mockito.BDDMockito.eq
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.inOrder
import org.mockito.BDDMockito.then
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import ru.terrakok.gitlabclient.TestData
import ru.terrakok.gitlabclient.TestSchedulers
import ru.terrakok.gitlabclient.di.PrimitiveWrapper
import ru.terrakok.gitlabclient.entity.OrderBy
import ru.terrakok.gitlabclient.entity.Project
import ru.terrakok.gitlabclient.entity.Sort
import ru.terrakok.gitlabclient.entity.app.target.AppTarget
import ru.terrakok.gitlabclient.entity.app.target.TargetAction
import ru.terrakok.gitlabclient.entity.app.target.TargetBadge
import ru.terrakok.gitlabclient.entity.app.target.TargetBadgeIcon
import ru.terrakok.gitlabclient.entity.app.target.TargetBadgeStatus
import ru.terrakok.gitlabclient.entity.app.target.TargetHeader
import ru.terrakok.gitlabclient.entity.app.target.TargetHeaderIcon
import ru.terrakok.gitlabclient.entity.app.target.TargetHeaderTitle
import ru.terrakok.gitlabclient.entity.app.target.TargetInternal
import ru.terrakok.gitlabclient.entity.event.EventAction
import ru.terrakok.gitlabclient.entity.issue.Issue
import ru.terrakok.gitlabclient.model.data.server.GitlabApi
import ru.terrakok.gitlabclient.model.data.server.MarkDownUrlResolver
import ru.terrakok.gitlabclient.model.data.state.ServerChanges

/**
 * @author Vitaliy Belyaev on 28.05.2019.
 */
class IssueInteractorTest {
    private val defaultPageSize = 1
    private val testPage = 2
    private val testIssue = TestData.getTestIssue()
    private val testNote = TestData.getNote()
    private val testProject = TestData.getProject(testIssue.projectId)

    private val api = mock(GitlabApi::class.java)
    private val markDownUrlResolver = mock(MarkDownUrlResolver::class.java)
    private val interactor = IssueInteractor(
        api,
        ServerChanges(TestSchedulers()),
        TestSchedulers(),
        PrimitiveWrapper(defaultPageSize),
        markDownUrlResolver
    )

    @Test
    fun `get my issues should succeed with valid api response`() {
        // GIVEN
        val expectedTargetHeader = getExpectedTargetHeader(testIssue, testProject)

        given(api.getMyIssues(
                anyOrNull(),
                anyOrNull(),
                anyOrNull(),
                anyOrNull(),
                anyOrNull(),
                anyOrNull(),
                anyOrNull(),
                anyOrNull(),
                anyInt(),
                anyInt())).willReturn(Single.just(listOf(testIssue)))

        given(api.getProject(anyLong(), anyBoolean())).willReturn(Single.just(testProject))

        // WHEN
        val testObserver = interactor
                .getMyIssues(page = testPage, pageSize = defaultPageSize)
                .test()

        testObserver.awaitTerminalEvent()

        // THEN
        then(api)
                .should(times(1))
                .getMyIssues(
                    null,
                    null,
                    null,
                    null,
                    null,
                    OrderBy.UPDATED_AT,
                    Sort.ASC,
                    null,
                    testPage,
                    defaultPageSize)

        then(api)
                .should(times(1))
                .getProject(testIssue.projectId, true)

        then(api).shouldHaveNoMoreInteractions()

        testObserver.assertResult(listOf(expectedTargetHeader))
    }

    @Test
    fun `get issues should succeed with valid api response`() {
        // GIVEN
        val expectedTargetHeader = getExpectedTargetHeader(testIssue, testProject)

        given(api.getIssues(
                anyLong(),
                anyOrNull(),
                anyOrNull(),
                anyOrNull(),
                anyOrNull(),
                anyOrNull(),
                anyOrNull(),
                anyOrNull(),
                anyOrNull(),
                anyInt(),
                anyInt())).willReturn(Single.just(listOf(testIssue)))

        given(api.getProject(anyLong(), anyBoolean())).willReturn(Single.just(testProject))

        // WHEN
        val testObserver = interactor
                .getIssues(projectId = testIssue.projectId, page = testPage, pageSize = defaultPageSize)
                .test()

        testObserver.awaitTerminalEvent()

        // THEN
        then(api)
                .should(times(1))
                .getIssues(
                        testIssue.projectId,
                        null,
                        null,
                        null,
                        null,
                        null,
                        OrderBy.UPDATED_AT,
                        Sort.ASC,
                        null,
                        testPage,
                        defaultPageSize)

        then(api)
                .should(times(1))
                .getProject(testIssue.projectId, true)

        then(api).shouldHaveNoMoreInteractions()

        testObserver.assertResult(listOf(expectedTargetHeader))
    }

    @Test
    fun `get issue should return issue with modified description`() {
        // GIVEN
        val resolvedBody = "body that differs from issue description"
        val expectedIssue = testIssue.copy(description = resolvedBody)

        given(api.getIssue(
                anyLong(),
                anyLong())).willReturn(Single.just(testIssue))

        given(api.getProject(anyLong(), anyBoolean())).willReturn(Single.just(testProject))
        given(markDownUrlResolver.resolve(anyString(), any())).willReturn(resolvedBody)

        // WHEN
        val testObserver = interactor
                .getIssue(testIssue.projectId, testIssue.id)
                .test()

        testObserver.awaitTerminalEvent()

        // THEN
        then(api)
                .should(times(1))
                .getProject(testIssue.projectId, true)

        then(api)
                .should(times(1))
                .getIssue(testIssue.projectId, testIssue.id)

        then(markDownUrlResolver)
                .should(times(1))
                .resolve(testIssue.description, testProject)

        then(api).shouldHaveNoMoreInteractions()

        testObserver.assertResult(expectedIssue)
    }

    @Test
    fun `get issue should return issue without modifications`() {
        // GIVEN
        val resolvedBody = testIssue.description

        given(api.getIssue(
                anyLong(),
                anyLong())).willReturn(Single.just(testIssue))

        given(api.getProject(anyLong(), anyBoolean())).willReturn(Single.just(testProject))
        given(markDownUrlResolver.resolve(anyString(), any())).willReturn(resolvedBody)

        // WHEN
        val testObserver = interactor
                .getIssue(testIssue.projectId, testIssue.id)
                .test()

        testObserver.awaitTerminalEvent()

        // THEN
        then(api)
                .should(times(1))
                .getProject(testIssue.projectId, true)

        then(api)
                .should(times(1))
                .getIssue(testIssue.projectId, testIssue.id)

        then(markDownUrlResolver)
                .should(times(1))
                .resolve(testIssue.description, testProject)

        then(api).shouldHaveNoMoreInteractions()

        testObserver.assertResult(testIssue)
    }

    @Test
    fun `get issue notes should return notes with modified body`() {
        // GIVEN
        val resolvedBody = "body that differs from issue description"
        val expected = testNote.copy(body = resolvedBody)

        given(api.getIssueNotes(
                anyLong(),
                anyLong(),
                anyOrNull(),
                anyOrNull(),
                anyInt(),
                anyInt())).willReturn(Single.just(listOf(testNote)))

        given(api.getProject(anyLong(), anyBoolean())).willReturn(Single.just(testProject))
        given(markDownUrlResolver.resolve(anyString(), any())).willReturn(resolvedBody)

        // WHEN
        val testObserver = interactor.getIssueNotes(
                testIssue.projectId,
                testIssue.id,
                null,
                null,
                testPage,
                defaultPageSize).test()

        testObserver.awaitTerminalEvent()

        // THEN
        then(api)
                .should(times(1))
                .getProject(testIssue.projectId, true)

        then(api)
                .should(times(1))
                .getIssueNotes(
                        testIssue.projectId,
                        testIssue.id,
                        null,
                        null,
                        testPage,
                        defaultPageSize)

        then(markDownUrlResolver)
                .should(times(1))
                .resolve(testNote.body, testProject)

        then(api).shouldHaveNoMoreInteractions()

        testObserver.assertResult(listOf(expected))
    }

    @Test
    fun `get issue notes should return notes without modifications`() {
        // GIVEN
        val resolvedBody = testNote.body

        given(api.getIssueNotes(
                anyLong(),
                anyLong(),
                anyOrNull(),
                anyOrNull(),
                anyInt(),
                anyInt())).willReturn(Single.just(listOf(testNote)))

        given(api.getProject(anyLong(), anyBoolean())).willReturn(Single.just(testProject))
        given(markDownUrlResolver.resolve(anyString(), any())).willReturn(resolvedBody)

        // WHEN
        val testObserver = interactor.getIssueNotes(
                testIssue.projectId,
                testIssue.id,
                null,
                null,
                testPage,
                defaultPageSize).test()

        testObserver.awaitTerminalEvent()

        // THEN
        then(api)
                .should(times(1))
                .getProject(testIssue.projectId, true)

        then(api)
                .should(times(1))
                .getIssueNotes(
                        testIssue.projectId,
                        testIssue.id,
                        null,
                        null,
                        testPage,
                        defaultPageSize)

        then(markDownUrlResolver)
                .should(times(1))
                .resolve(testNote.body, testProject)

        then(api).shouldHaveNoMoreInteractions()

        testObserver.assertResult(listOf(testNote))
    }

    @Test
    fun `get all issue notes should return notes from all pages`() {
        // GIVEN
        given(api.getIssueNotes(
                anyLong(),
                anyLong(),
                anyOrNull(),
                anyOrNull(),
                anyInt(),
                anyInt())).willReturn(Single.just(listOf(testNote)))

        given(api.getIssueNotes(
                anyLong(),
                anyLong(),
                anyOrNull(),
                anyOrNull(),
                eq(3),
                anyInt())).willReturn(Single.just(emptyList()))

        given(api.getProject(anyLong(), anyBoolean())).willReturn(Single.just(testProject))
        given(markDownUrlResolver.resolve(anyString(), any())).willReturn(testNote.body)

        // WHEN
        val testObserver = interactor.getAllIssueNotes(
                testIssue.projectId,
                testIssue.id,
                null,
                null).test()

        testObserver.awaitTerminalEvent()

        // THEN
        val inOrder = inOrder(api)

        then(api).should(inOrder).getProject(testIssue.projectId, true)
        then(api).should(inOrder).getIssueNotes(
                testIssue.projectId,
                testIssue.id,
                null,
                null,
                1,
                GitlabApi.MAX_PAGE_SIZE)

        then(api).should(inOrder).getIssueNotes(
                testIssue.projectId,
                testIssue.id,
                null,
                null,
                2,
                GitlabApi.MAX_PAGE_SIZE)

        then(api).should(inOrder).getIssueNotes(
                testIssue.projectId,
                testIssue.id,
                null,
                null,
                3,
                GitlabApi.MAX_PAGE_SIZE)

        then(markDownUrlResolver)
                .should(times(2))
                .resolve(testNote.body, testProject)

        then(api).shouldHaveNoMoreInteractions()
        then(markDownUrlResolver).shouldHaveNoMoreInteractions()

        testObserver.assertResult(listOf(testNote, testNote))
    }

    private fun getExpectedTargetHeader(issue: Issue, project: Project): TargetHeader {
        val badges = mutableListOf<TargetBadge>()
        badges.add(TargetBadge.Status(TargetBadgeStatus.OPENED))
        badges.add(TargetBadge.Text(project.name, AppTarget.PROJECT, project.id))
        badges.add(TargetBadge.Text(issue.author.username, AppTarget.USER, issue.author.id))
        badges.add(TargetBadge.Icon(TargetBadgeIcon.COMMENTS, issue.userNotesCount))
        badges.add(TargetBadge.Icon(TargetBadgeIcon.UP_VOTES, issue.upvotes))
        badges.add(TargetBadge.Icon(TargetBadgeIcon.DOWN_VOTES, issue.downvotes))
        badges.add(TargetBadge.Icon(TargetBadgeIcon.RELATED_MERGE_REQUESTS, issue.relatedMergeRequestCount))
        issue.labels.forEach { label -> badges.add(TargetBadge.Text(label)) }

        return TargetHeader.Public(
                issue.author,
                TargetHeaderIcon.NONE,
                TargetHeaderTitle.Event(
                        issue.author.name,
                        EventAction.CREATED,
                        "${AppTarget.ISSUE} #${issue.iid}",
                        project.name
                ),
                issue.title ?: "",
                issue.createdAt,
                AppTarget.ISSUE,
                issue.id,
                TargetInternal(issue.projectId, issue.iid),
                badges,
                TargetAction.Undefined
        )
    }
}
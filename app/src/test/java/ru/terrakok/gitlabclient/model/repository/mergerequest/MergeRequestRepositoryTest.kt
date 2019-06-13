package ru.terrakok.gitlabclient.model.repository.mergerequest

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.anyOrNull
import io.reactivex.Single
import org.junit.Test
import org.mockito.ArgumentMatchers.*
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.then
import org.mockito.Mockito
import org.mockito.Mockito.inOrder
import org.mockito.Mockito.times
import ru.terrakok.gitlabclient.TestData
import ru.terrakok.gitlabclient.TestSchedulers
import ru.terrakok.gitlabclient.di.PrimitiveWrapper
import ru.terrakok.gitlabclient.entity.ShortUser
import ru.terrakok.gitlabclient.entity.app.CommitWithShortUser
import ru.terrakok.gitlabclient.entity.mergerequest.MergeRequestChange
import ru.terrakok.gitlabclient.model.data.server.GitlabApi
import ru.terrakok.gitlabclient.model.data.server.MarkDownUrlResolver

/**
 * @author Vitaliy Belyaev on 01.06.2019.
 */
class MergeRequestRepositoryTest {
    private val defaultPageSize = 1
    private val testPage = 2
    private val testMergeRequest = TestData.getMergeRequest()
    private val testProject = TestData.getProject(testMergeRequest.projectId)

    private val api = Mockito.mock(GitlabApi::class.java)
    private val markDownUrlResolver = Mockito.mock(MarkDownUrlResolver::class.java)
    private val repository = MergeRequestRepository(
            api,
            TestSchedulers(),
            PrimitiveWrapper(defaultPageSize),
            markDownUrlResolver)

    @Test
    fun `get my mrs should succeed with valid api response`() {
        // GIVEN
        val expectedTargetHeader = TestData.getExpectedTargetHeader(testMergeRequest, testProject)

        given(api.getMyMergeRequests(
                anyOrNull(), anyOrNull(), anyOrNull(), anyOrNull(), anyOrNull(),
                anyOrNull(), anyOrNull(), anyOrNull(), anyOrNull(), anyOrNull(),
                anyOrNull(), anyOrNull(), anyInt(), anyInt()))
                .willReturn(Single.just(listOf(testMergeRequest)))

        given(api.getProject(anyLong(), anyBoolean())).willReturn(Single.just(testProject))

        // WHEN
        val testObserver = repository.getMyMergeRequests(page = testPage).test()
        testObserver.awaitTerminalEvent()

        // THEN
        then(api)
                .should(times(1))
                .getMyMergeRequests(
                        null, null, null, null, null,
                        null, null, null, null,
                        null, null, null, testPage, defaultPageSize)

        then(api)
                .should(times(1))
                .getProject(testMergeRequest.projectId, true)

        then(api).shouldHaveNoMoreInteractions()

        testObserver.assertResult(listOf(expectedTargetHeader))
    }

    @Test
    fun `get mrs should succeed with valid api response`() {
        // GIVEN
        val expectedTargetHeader = TestData.getExpectedTargetHeader(testMergeRequest, testProject)

        given(api.getMergeRequests(
                anyLong(), anyOrNull(), anyOrNull(), anyOrNull(), anyOrNull(),
                anyOrNull(), anyOrNull(), anyOrNull(), anyOrNull(), anyOrNull(),
                anyOrNull(), anyOrNull(), anyOrNull(), anyInt(), anyInt()))
                .willReturn(Single.just(listOf(testMergeRequest)))

        given(api.getProject(anyLong(), anyBoolean())).willReturn(Single.just(testProject))

        // WHEN
        val testObserver = repository.getMergeRequests(
                projectId = testMergeRequest.projectId, page = testPage).test()
        testObserver.awaitTerminalEvent()

        // THEN
        then(api)
                .should(times(1))
                .getMergeRequests(
                        testMergeRequest.projectId, null, null, null,
                        null, null, null, null,
                        null, null, null, null,
                        null, testPage, defaultPageSize)

        then(api)
                .should(times(1))
                .getProject(testMergeRequest.projectId, true)

        then(api).shouldHaveNoMoreInteractions()

        testObserver.assertResult(listOf(expectedTargetHeader))
    }

    @Test
    fun `get mr should return mr without modifications`() {
        // GIVEN
        val resolvedBody = testMergeRequest.description

        given(api.getMergeRequest(anyLong(), anyLong())).willReturn(Single.just(testMergeRequest))
        given(api.getProject(anyLong(), anyBoolean())).willReturn(Single.just(testProject))
        given(markDownUrlResolver.resolve(anyString(), any())).willReturn(resolvedBody)

        // WHEN
        val testObserver = repository.getMergeRequest(
                testMergeRequest.projectId, testMergeRequest.id).test()
        testObserver.awaitTerminalEvent()

        // THEN
        then(api)
                .should(times(1))
                .getMergeRequest(testMergeRequest.projectId, testMergeRequest.id)

        then(api)
                .should(times(1))
                .getProject(testMergeRequest.projectId, true)

        then(markDownUrlResolver)
                .should(times(1))
                .resolve(testMergeRequest.description, testProject)

        then(api).shouldHaveNoMoreInteractions()
        then(markDownUrlResolver).shouldHaveNoMoreInteractions()

        testObserver.assertResult(testMergeRequest)
    }

    @Test
    fun `get mr should return mr with modified description`() {
        // GIVEN
        val resolvedBody = "resolved body that differ from mr description"

        given(api.getMergeRequest(anyLong(), anyLong())).willReturn(Single.just(testMergeRequest))
        given(api.getProject(anyLong(), anyBoolean())).willReturn(Single.just(testProject))
        given(markDownUrlResolver.resolve(anyString(), any())).willReturn(resolvedBody)

        // WHEN
        val testObserver = repository.getMergeRequest(
                testMergeRequest.projectId, testMergeRequest.id).test()
        testObserver.awaitTerminalEvent()

        // THEN
        then(api)
                .should(times(1))
                .getMergeRequest(testMergeRequest.projectId, testMergeRequest.id)

        then(api)
                .should(times(1))
                .getProject(testMergeRequest.projectId, true)

        then(markDownUrlResolver)
                .should(times(1))
                .resolve(testMergeRequest.description, testProject)

        then(api).shouldHaveNoMoreInteractions()
        then(markDownUrlResolver).shouldHaveNoMoreInteractions()

        testObserver.assertResult(testMergeRequest.copy(description = resolvedBody))
    }

    @Test
    fun `get mr notes should return notes without modifications`() {
        // GIVEN
        val testNote = TestData.getNote()
        val resolvedBody = testNote.body

        given(api.getMergeRequestNotes(
                anyLong(), anyLong(), anyOrNull(), anyOrNull(), anyInt(), anyInt()))
                .willReturn(Single.just(listOf(testNote)))

        given(api.getProject(anyLong(), anyBoolean())).willReturn(Single.just(testProject))
        given(markDownUrlResolver.resolve(anyString(), any())).willReturn(resolvedBody)

        // WHEN
        val testObserver = repository.getMergeRequestNotes(
                testMergeRequest.projectId, testMergeRequest.id,
                null, null, testPage).test()

        testObserver.awaitTerminalEvent()

        // THEN
        then(api)
                .should(times(1))
                .getMergeRequestNotes(
                        testMergeRequest.projectId, testMergeRequest.id,
                        null, null, testPage, defaultPageSize)

        then(api)
                .should(times(1))
                .getProject(testMergeRequest.projectId, true)

        then(markDownUrlResolver)
                .should(times(1))
                .resolve(testNote.body, testProject)

        then(api).shouldHaveNoMoreInteractions()
        then(markDownUrlResolver).shouldHaveNoMoreInteractions()

        testObserver.assertResult(listOf(testNote))
    }

    @Test
    fun `get mr notes should return notes with modified body`() {
        // GIVEN
        val testNote = TestData.getNote()
        val resolvedBody = "resolved body that differ from note body"

        given(api.getMergeRequestNotes(
                anyLong(), anyLong(), anyOrNull(), anyOrNull(), anyInt(), anyInt()))
                .willReturn(Single.just(listOf(testNote)))

        given(api.getProject(anyLong(), anyBoolean())).willReturn(Single.just(testProject))
        given(markDownUrlResolver.resolve(anyString(), any())).willReturn(resolvedBody)

        // WHEN
        val testObserver = repository.getMergeRequestNotes(
                testMergeRequest.projectId, testMergeRequest.id,
                null, null, testPage).test()

        testObserver.awaitTerminalEvent()

        // THEN
        then(api)
                .should(times(1))
                .getMergeRequestNotes(
                        testMergeRequest.projectId, testMergeRequest.id,
                        null, null, testPage, defaultPageSize)

        then(api)
                .should(times(1))
                .getProject(testMergeRequest.projectId, true)

        then(markDownUrlResolver)
                .should(times(1))
                .resolve(testNote.body, testProject)

        then(api).shouldHaveNoMoreInteractions()
        then(markDownUrlResolver).shouldHaveNoMoreInteractions()

        testObserver.assertResult(listOf(testNote.copy(body = resolvedBody)))
    }

    @Test
    fun `get all mr notes should return notes without modifications`() {
        // GIVEN
        val testNote = TestData.getNote()
        val resolvedBody = testNote.body

        given(api.getMergeRequestNotes(
                anyLong(), anyLong(), anyOrNull(), anyOrNull(), anyInt(), anyInt()))
                .willReturn(Single.just(listOf(testNote)))

        given(api.getMergeRequestNotes(
                anyLong(), anyLong(), anyOrNull(), anyOrNull(), eq(3), anyInt()))
                .willReturn(Single.just(emptyList()))

        given(api.getProject(anyLong(), anyBoolean())).willReturn(Single.just(testProject))
        given(markDownUrlResolver.resolve(anyString(), any())).willReturn(resolvedBody)

        // WHEN
        val testObserver = repository.getAllMergeRequestNotes(
                testMergeRequest.projectId,
                testMergeRequest.id,
                null,
                null).test()

        testObserver.awaitTerminalEvent()

        // THEN
        val inOrder = inOrder(api)
        then(api)
                .should(inOrder)
                .getProject(testMergeRequest.projectId, true)

        then(api)
                .should(inOrder)
                .getMergeRequestNotes(
                        testMergeRequest.projectId, testMergeRequest.id,
                        null, null, 1, GitlabApi.MAX_PAGE_SIZE)

        then(api)
                .should(inOrder)
                .getMergeRequestNotes(
                        testMergeRequest.projectId, testMergeRequest.id,
                        null, null, 2, GitlabApi.MAX_PAGE_SIZE)

        then(api)
                .should(inOrder)
                .getMergeRequestNotes(
                        testMergeRequest.projectId, testMergeRequest.id,
                        null, null, 3, GitlabApi.MAX_PAGE_SIZE)

        then(markDownUrlResolver)
                .should(times(2))
                .resolve(testNote.body, testProject)

        then(api).shouldHaveNoMoreInteractions()
        then(markDownUrlResolver).shouldHaveNoMoreInteractions()

        testObserver.assertResult(listOf(testNote, testNote))
    }

    @Test
    fun `get all mr notes should return notes with modified body`() {
        // GIVEN
        val testNote = TestData.getNote()
        val resolvedBody = "resolved body that differ from note body"

        given(api.getMergeRequestNotes(
                anyLong(), anyLong(), anyOrNull(), anyOrNull(), anyInt(), anyInt()))
                .willReturn(Single.just(listOf(testNote)))

        given(api.getMergeRequestNotes(
                anyLong(), anyLong(), anyOrNull(), anyOrNull(), eq(3), anyInt()))
                .willReturn(Single.just(emptyList()))

        given(api.getProject(anyLong(), anyBoolean())).willReturn(Single.just(testProject))
        given(markDownUrlResolver.resolve(anyString(), any())).willReturn(resolvedBody)

        // WHEN
        val testObserver = repository.getAllMergeRequestNotes(
                testMergeRequest.projectId, testMergeRequest.id, null, null).test()

        testObserver.awaitTerminalEvent()

        // THEN
        val inOrder = inOrder(api)
        then(api)
                .should(inOrder)
                .getProject(testMergeRequest.projectId, true)

        then(api)
                .should(inOrder)
                .getMergeRequestNotes(
                        testMergeRequest.projectId, testMergeRequest.id,
                        null, null, 1, GitlabApi.MAX_PAGE_SIZE)

        then(api)
                .should(inOrder)
                .getMergeRequestNotes(
                        testMergeRequest.projectId, testMergeRequest.id,
                        null, null, 2, GitlabApi.MAX_PAGE_SIZE)

        then(api)
                .should(inOrder)
                .getMergeRequestNotes(
                        testMergeRequest.projectId, testMergeRequest.id,
                        null, null, 3, GitlabApi.MAX_PAGE_SIZE)

        then(markDownUrlResolver)
                .should(times(2))
                .resolve(testNote.body, testProject)

        then(api).shouldHaveNoMoreInteractions()
        then(markDownUrlResolver).shouldHaveNoMoreInteractions()

        testObserver.assertResult(listOf(
                testNote.copy(body = resolvedBody),
                testNote.copy(body = resolvedBody)))
    }

    @Test
    fun `create mr note should succeed with valid api response`() {
        // GIVEN
        val testNote = TestData.getNote()
        val testProjectId = 123L
        val testIssueId = 321L

        given(api.createMergeRequestNote(
                anyLong(), anyLong(), anyString())).willReturn(Single.just(testNote))

        // WHEN
        val testObserver = repository.createMergeRequestNote(
                testProjectId, testIssueId, testNote.body).test()

        testObserver.awaitTerminalEvent()

        // THEN
        then(api)
                .should(times(1))
                .createMergeRequestNote(testProjectId, testIssueId, testNote.body)

        then(api).shouldHaveNoMoreInteractions()

        testObserver.assertResult(testNote)
    }

    @Test
    fun `get mr commits should add avatar url to commit when authors match`() {
        // GIVEN
        val projectId = 123L
        val mergeRequestId = 321L
        val testCommit = TestData.getCommit()
        val testShortUser = ShortUser(
                11L, "state", testCommit.authorName, "url",
                "avatar", "username")

        val commitThatNotMatchAnyParticipant = testCommit.copy(authorName = "Mr Lost")
        val authorThatNotMatchAnyCommit = testShortUser.copy(name = "Mr No Commiter")

        val expected = listOf(
                CommitWithShortUser(testCommit, testShortUser),
                CommitWithShortUser(commitThatNotMatchAnyParticipant, null))

        given(api.getMergeRequestParticipants(
                anyLong(), anyLong(), anyInt(), anyInt())).willReturn(Single.just(
                listOf(testShortUser, authorThatNotMatchAnyCommit)))

        given(api.getMergeRequestParticipants(
                anyLong(), anyLong(), eq(2), anyInt())).willReturn(Single.just(emptyList()))

        given(api.getMergeRequestCommits(
                anyLong(), anyLong(), anyInt(), anyInt())).willReturn(Single.just(
                listOf(testCommit, commitThatNotMatchAnyParticipant)))

        // WHEN
        val testObserver = repository.getMergeRequestCommits(
                projectId, mergeRequestId, testPage).test()

        testObserver.awaitTerminalEvent()

        // THEN
        then(api)
                .should(times(1))
                .getMergeRequestParticipants(
                        projectId, mergeRequestId, 1, GitlabApi.MAX_PAGE_SIZE)

        then(api)
                .should(times(1))
                .getMergeRequestParticipants(
                        projectId, mergeRequestId, 2, GitlabApi.MAX_PAGE_SIZE)

        then(api)
                .should(times(1))
                .getMergeRequestCommits(projectId, mergeRequestId, testPage, defaultPageSize)

        then(api).shouldHaveNoMoreInteractions()

        testObserver.assertResult(expected)
    }

    @Test
    fun `get mr changes should return changes when it exists`() {
        // GIVEN
        val mrChanges = listOf(
                MergeRequestChange(
                        "", "", "", "",
                        false, false, true, "diff"))

        val mrWithChanges = testMergeRequest.copy(changes = mrChanges)

        given(api.getMergeRequestChanges(
                anyLong(), anyLong())).willReturn(Single.just(mrWithChanges))

        // WHEN
        val testObserver = repository.getMergeRequestChanges(
                mrWithChanges.projectId, mrWithChanges.id).test()

        testObserver.awaitTerminalEvent()

        // THEN
        then(api)
                .should(times(1))
                .getMergeRequestChanges(mrWithChanges.projectId, mrWithChanges.id)

        then(api).shouldHaveNoMoreInteractions()

        testObserver.assertResult(mrChanges)
    }

    @Test
    fun `get mr changes should return empty list when changes not exists`() {
        // GIVEN
        given(api.getMergeRequestChanges(
                anyLong(), anyLong())).willReturn(Single.just(testMergeRequest))

        // WHEN
        val testObserver = repository.getMergeRequestChanges(
                testMergeRequest.projectId, testMergeRequest.id).test()

        testObserver.awaitTerminalEvent()

        // THEN
        then(api)
                .should(times(1))
                .getMergeRequestChanges(testMergeRequest.projectId, testMergeRequest.id)

        then(api).shouldHaveNoMoreInteractions()

        testObserver.assertResult(emptyList())
    }

    @Test
    fun `get milestone mrs should succeed with valid api response`() {
        // GIVEN
        val milestoneMrs = listOf(testMergeRequest)

        given(api.getMilestoneMergeRequests(
                anyLong(), anyLong(), anyInt(), anyInt())).willReturn(Single.just(milestoneMrs))

        // WHEN
        val testObserver = repository.getMilestoneMergeRequests(
                testMergeRequest.projectId, testMergeRequest.id, testPage).test()

        testObserver.awaitTerminalEvent()

        // THEN
        then(api)
                .should(times(1))
                .getMilestoneMergeRequests(
                        testMergeRequest.projectId, testMergeRequest.id, testPage, defaultPageSize)

        then(api).shouldHaveNoMoreInteractions()

        testObserver.assertResult(milestoneMrs)
    }
}
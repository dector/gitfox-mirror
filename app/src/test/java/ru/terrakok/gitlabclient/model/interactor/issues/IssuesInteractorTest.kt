package ru.terrakok.gitlabclient.model.interactor.issues

import com.nhaarman.mockito_kotlin.*
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import ru.terrakok.gitlabclient.entity.OrderBy
import ru.terrakok.gitlabclient.entity.issue.IssueScope
import ru.terrakok.gitlabclient.entity.issue.IssueState
import ru.terrakok.gitlabclient.model.interactor.issue.IssueInteractor
import ru.terrakok.gitlabclient.model.repository.issue.IssueRepository

/**
 * @author Eugene Shapovalov (CraggyHaggy) on 21.11.17.
 */
class IssuesInteractorTest {

    private lateinit var issueRepository: IssueRepository

    private val testPage = 0
    private val testError = RuntimeException("Test error")

    @Before
    fun setUp() {
        issueRepository = mock()
    }

    @Test
    fun getMyIssuesClosed_success() {
        val isOpened = false
        val createdByMe = true
        whenever(
            issueRepository.getMyIssues(
                eq(IssueScope.CREATED_BY_ME),
                eq(null),
                anyOrNull(),
                anyOrNull(),
                anyOrNull(),
                anyOrNull(),
                anyOrNull(),
                anyOrNull(),
                eq(testPage),
                any()
            )
        ).thenReturn(Single.just(emptyList()))

        val interactor = IssueInteractor(issueRepository)

        val testObserver = interactor.getMyIssues(createdByMe, isOpened, testPage).test()
        testObserver.awaitTerminalEvent()

        verify(issueRepository).getMyIssues(
            eq(IssueScope.CREATED_BY_ME),
            eq(null),
            eq(null),
            eq(null),
            eq(null),
            eq(OrderBy.UPDATED_AT),
            eq(null),
            eq(null),
            eq(testPage),
            any()
        )
        testObserver
            .assertNoErrors()
            .assertValueCount(1)
    }

    @Test
    fun getMyIssuesOpened_success() {
        val isOpened = true
        val createdByMe = true
        whenever(
            issueRepository.getMyIssues(
                anyOrNull(),
                eq(IssueState.OPENED),
                anyOrNull(),
                anyOrNull(),
                anyOrNull(),
                anyOrNull(),
                anyOrNull(),
                anyOrNull(),
                eq(testPage),
                any()
            )
        ).thenReturn(Single.just(emptyList()))

        val interactor = IssueInteractor(issueRepository)

        val testObserver = interactor.getMyIssues(createdByMe, isOpened, testPage).test()
        testObserver.awaitTerminalEvent()

        verify(issueRepository).getMyIssues(
            eq(IssueScope.CREATED_BY_ME),
            eq(IssueState.OPENED),
            eq(null),
            eq(null),
            eq(null),
            eq(OrderBy.UPDATED_AT),
            eq(null),
            eq(null),
            eq(testPage),
            any()
        )
        testObserver
            .assertNoErrors()
            .assertValueCount(1)
    }

    @Test
    fun getMyIssues_error() {
        val isOpened = false
        val createdByMe = true
        whenever(
            issueRepository.getMyIssues(
                eq(IssueScope.CREATED_BY_ME),
                eq(null),
                anyOrNull(),
                anyOrNull(),
                anyOrNull(),
                anyOrNull(),
                anyOrNull(),
                anyOrNull(),
                eq(testPage),
                any()
            )
        ).thenReturn(Single.error(testError))

        val interactor = IssueInteractor(issueRepository)

        val testObserver = interactor.getMyIssues(createdByMe, isOpened, testPage).test()
        testObserver.awaitTerminalEvent()

        verify(issueRepository).getMyIssues(
            eq(IssueScope.CREATED_BY_ME),
            eq(null),
            eq(null),
            eq(null),
            eq(null),
            eq(OrderBy.UPDATED_AT),
            eq(null),
            eq(null),
            eq(testPage),
            any()
        )
        testObserver
            .assertError(testError)
            .assertNoValues()
    }
}
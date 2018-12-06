package ru.terrakok.gitlabclient.model.interactor.issues

import com.nhaarman.mockito_kotlin.*
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
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
        issueRepository = mock(IssueRepository::class.java)
    }

    @Test
    fun getMyIssuesClosed_success() {
        val isOpened = false
        whenever(issueRepository.getMyIssues(
                eq(IssueState.CLOSED),
                anyOrNull(),
                anyOrNull(),
                anyOrNull(),
                anyOrNull(),
                anyOrNull(),
                anyOrNull(),
                eq(testPage),
                any())).thenReturn(Single.just(emptyList()))

        val interactor = IssuesInteractor(issueRepository)

        val testObserver = interactor.getMyIssues(isOpened, testPage).test()
        testObserver.awaitTerminalEvent()

        verify(issueRepository).getMyIssues(
                eq(IssueState.CLOSED),
                eq(null),
                eq(null),
                eq(null),
                eq(null),
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
        whenever(issueRepository.getMyIssues(
                eq(IssueState.OPENED),
                anyOrNull(),
                anyOrNull(),
                anyOrNull(),
                anyOrNull(),
                anyOrNull(),
                anyOrNull(),
                eq(testPage),
                any())).thenReturn(Single.just(emptyList()))

        val interactor = IssuesInteractor(issueRepository)

        val testObserver = interactor.getMyIssues(isOpened, testPage).test()
        testObserver.awaitTerminalEvent()

        verify(issueRepository).getMyIssues(
                eq(IssueState.OPENED),
                eq(null),
                eq(null),
                eq(null),
                eq(null),
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

        whenever(issueRepository.getMyIssues(
                eq(IssueState.CLOSED),
                anyOrNull(),
                anyOrNull(),
                anyOrNull(),
                anyOrNull(),
                anyOrNull(),
                anyOrNull(),
                eq(testPage),
                any())).thenReturn(Single.error(testError))

        val interactor = IssuesInteractor(issueRepository)

        val testObserver = interactor.getMyIssues(isOpened, testPage).test()
        testObserver.awaitTerminalEvent()

        verify(issueRepository).getMyIssues(
                eq(IssueState.CLOSED),
                eq(null),
                eq(null),
                eq(null),
                eq(null),
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
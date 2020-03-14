package ru.terrakok.gitlabclient.model.interactor

import com.nhaarman.mockitokotlin2.anyOrNull
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.Test
import org.mockito.ArgumentMatchers.*
import org.mockito.BDDMockito
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.then
import org.mockito.Mockito
import org.mockito.Mockito.times
import ru.terrakok.gitlabclient.TestData
import ru.terrakok.gitlabclient.TestSchedulers
import ru.terrakok.gitlabclient.di.PrimitiveWrapper
import ru.terrakok.gitlabclient.model.data.cache.ProjectLabelCache
import ru.terrakok.gitlabclient.model.data.cache.ProjectMilestoneCache
import ru.terrakok.gitlabclient.model.data.server.GitlabApi
import ru.terrakok.gitlabclient.model.data.state.ServerChanges

/**
 * @author Vitaliy Belyaev on 03.06.2019.
 */
class MilestoneInteractorTest {
    private val defaultPageSize = 1
    private val testPage = 2
    private val testMilestone = TestData.getMilestone()
    private val testMergeRequest = TestData.getMergeRequest()
    private val testIssue = TestData.getTestIssue()

    private val api = Mockito.mock(GitlabApi::class.java)
    private val interactor = MilestoneInteractor(
        api,
        ServerChanges(TestSchedulers()),
        TestSchedulers(),
        PrimitiveWrapper(defaultPageSize),
        ProjectMilestoneCache(PrimitiveWrapper(1000))
    )

    @Test
    fun `get milestones should succeed with valid api response`() {
        // GIVEN
        given(
            api.getMilestones(
                anyLong(),
                anyOrNull(),
                anyInt(),
                anyInt()
            )
        ).willReturn(Single.just(listOf(testMilestone)))

        // WHEN
        val testObserver = interactor
            .getMilestones(projectId = testMilestone.projectId, page = testPage).test()

        testObserver.awaitTerminalEvent()

        // THEN
        then(api)
            .should(times(1))
            .getMilestones(
                testMilestone.projectId,
                null,
                testPage,
                defaultPageSize
            )

        then(api).shouldHaveNoMoreInteractions()

        testObserver.assertResult(listOf(testMilestone))
    }

    @Test
    fun `get milestone should succeed with valid api response`() {
        // GIVEN
        given(
            api.getMilestone(
                anyLong(),
                anyLong()
            )
        ).willReturn(Single.just(testMilestone))

        // WHEN
        val testObserver = interactor
            .getMilestone(testMilestone.projectId, testMilestone.id).test()

        testObserver.awaitTerminalEvent()

        // THEN
        then(api)
            .should(times(1))
            .getMilestone(testMilestone.projectId, testMilestone.id)

        then(api).shouldHaveNoMoreInteractions()

        testObserver.assertResult(testMilestone)
    }

    @Test
    fun `create milestone should succeed with valid api response`() {
        // GIVEN
        given(
            api.createMilestone(
                anyLong(),
                anyString(),
                anyOrNull(),
                anyOrNull(),
                anyOrNull()
            )
        ).willReturn(Single.just(testMilestone))

        // WHEN
        val testObserver = interactor
            .createMilestone(
                testMilestone.projectId,
                testMilestone.title!!,
                null,
                null,
                null
            ).test()

        testObserver.awaitTerminalEvent()

        // THEN
        then(api)
            .should(times(1))
            .createMilestone(
                testMilestone.projectId,
                testMilestone.title!!,
                null,
                null,
                null
            )

        then(api).shouldHaveNoMoreInteractions()

        testObserver.assertResult(testMilestone)
    }

    @Test
    fun `update milestone should succeed with valid api response`() {
        // GIVEN
        given(
            api.updateMilestone(
                anyLong(),
                anyLong(),
                anyOrNull(),
                anyOrNull(),
                anyOrNull(),
                anyOrNull()
            )
        ).willReturn(Single.just(testMilestone))

        // WHEN
        val testObserver = interactor
            .updateMilestone(
                testMilestone.projectId,
                testMilestone.id,
                null,
                null,
                null,
                null
            ).test()

        testObserver.awaitTerminalEvent()

        // THEN
        then(api)
            .should(times(1))
            .updateMilestone(
                testMilestone.projectId,
                testMilestone.id,
                null,
                null,
                null,
                null
            )

        then(api).shouldHaveNoMoreInteractions()

        testObserver.assertResult(testMilestone)
    }

    @Test
    fun `delete milestone should succeed with valid api response`() {
        // GIVEN
        given(
            api.deleteMilestone(
                anyLong(),
                anyLong()
            )
        ).willReturn(Completable.complete())

        // WHEN
        val testObserver = interactor
            .deleteMilestone(testMilestone.projectId, testMilestone.id).test()

        testObserver.awaitTerminalEvent()

        // THEN
        then(api)
            .should(times(1))
            .deleteMilestone(testMilestone.projectId, testMilestone.id)

        then(api).shouldHaveNoMoreInteractions()

        testObserver
            .assertNoErrors()
            .assertNoValues()
            .assertComplete()
    }

    @Test
    fun `get milestone mrs should succeed with valid api response`() {
        // GIVEN
        val milestoneMrs = listOf(testMergeRequest)

        given(
            api.getMilestoneMergeRequests(
                anyLong(), anyLong(), anyInt(), anyInt()
            )
        ).willReturn(Single.just(milestoneMrs))

        // WHEN
        val testObserver = interactor.getMilestoneMergeRequests(
            testMergeRequest.projectId, testMergeRequest.id, testPage
        ).test()

        testObserver.awaitTerminalEvent()

        // THEN
        then(api)
            .should(times(1))
            .getMilestoneMergeRequests(
                testMergeRequest.projectId, testMergeRequest.id, testPage, defaultPageSize
            )

        then(api).shouldHaveNoMoreInteractions()

        testObserver.assertResult(milestoneMrs)
    }

    @Test
    fun `get milestone issues should success with valid api response`() {
        // GIVEN
        val testMilestoneId = 3232L

        given(
            api.getMilestoneIssues(
                BDDMockito.anyLong(),
                BDDMockito.anyLong(),
                BDDMockito.anyInt(),
                BDDMockito.anyInt()
            )
        ).willReturn(Single.just(listOf(testIssue)))

        // WHEN
        val testObserver = interactor.getMilestoneIssues(
            testIssue.projectId,
            testMilestoneId,
            testPage,
            defaultPageSize
        ).test()

        testObserver.awaitTerminalEvent()

        // THEN
        then(api)
            .should(times(1))
            .getMilestoneIssues(
                testIssue.projectId,
                testMilestoneId,
                testPage,
                defaultPageSize
            )

        then(api).shouldHaveNoMoreInteractions()

        testObserver.assertResult(listOf(testIssue))
    }
}

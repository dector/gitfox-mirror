package ru.terrakok.gitlabclient.model.repository.milestone

import com.nhaarman.mockitokotlin2.anyOrNull
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.Test
import org.mockito.ArgumentMatchers.*
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.then
import org.mockito.Mockito
import org.mockito.Mockito.times
import ru.terrakok.gitlabclient.TestData
import ru.terrakok.gitlabclient.TestSchedulers
import ru.terrakok.gitlabclient.di.PrimitiveWrapper
import ru.terrakok.gitlabclient.model.data.server.GitlabApi
import ru.terrakok.gitlabclient.model.data.state.ServerChanges
import ru.terrakok.gitlabclient.model.interactor.MilestoneInteractor

/**
 * @author Vitaliy Belyaev on 03.06.2019.
 */
class MilestoneInteractorTest {
    private val defaultPageSize = 1
    private val testPage = 2
    private val testMilestone = TestData.getMilestone()

    private val api = Mockito.mock(GitlabApi::class.java)
    private val repository =
        MilestoneInteractor(
            api,
            ServerChanges(TestSchedulers()),
            TestSchedulers(),
            PrimitiveWrapper(defaultPageSize)
        )

    @Test
    fun `get milestones should succeed with valid api response`() {
        // GIVEN
        given(api.getMilestones(
                anyLong(),
                anyOrNull(),
                anyInt(),
                anyInt())).willReturn(Single.just(listOf(testMilestone)))

        // WHEN
        val testObserver = repository
                .getMilestones(projectId = testMilestone.projectId, page = testPage).test()

        testObserver.awaitTerminalEvent()

        // THEN
        then(api)
                .should(times(1))
                .getMilestones(
                        testMilestone.projectId,
                        null,
                        testPage,
                        defaultPageSize)

        then(api).shouldHaveNoMoreInteractions()

        testObserver.assertResult(listOf(testMilestone))
    }

    @Test
    fun `get milestone should succeed with valid api response`() {
        // GIVEN
        given(api.getMilestone(
                anyLong(),
                anyLong())).willReturn(Single.just(testMilestone))

        // WHEN
        val testObserver = repository
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
        given(api.createMilestone(
                anyLong(),
                anyString(),
                anyOrNull(),
                anyOrNull(),
                anyOrNull())).willReturn(Single.just(testMilestone))

        // WHEN
        val testObserver = repository
                .createMilestone(
                        testMilestone.projectId,
                        testMilestone.title!!,
                        null,
                        null,
                        null).test()

        testObserver.awaitTerminalEvent()

        // THEN
        then(api)
                .should(times(1))
                .createMilestone(
                        testMilestone.projectId,
                        testMilestone.title!!,
                        null,
                        null,
                        null)

        then(api).shouldHaveNoMoreInteractions()

        testObserver.assertResult(testMilestone)
    }

    @Test
    fun `update milestone should succeed with valid api response`() {
        // GIVEN
        given(api.updateMilestone(
                anyLong(),
                anyLong(),
                anyOrNull(),
                anyOrNull(),
                anyOrNull(),
                anyOrNull())).willReturn(Single.just(testMilestone))

        // WHEN
        val testObserver = repository
                .updateMilestone(
                        testMilestone.projectId,
                        testMilestone.id,
                        null,
                        null,
                        null,
                        null).test()

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
                        null)

        then(api).shouldHaveNoMoreInteractions()

        testObserver.assertResult(testMilestone)
    }

    @Test
    fun `delete milestone should succeed with valid api response`() {
        // GIVEN
        given(api.deleteMilestone(
                anyLong(),
                anyLong())).willReturn(Completable.complete())

        // WHEN
        val testObserver = repository
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
}
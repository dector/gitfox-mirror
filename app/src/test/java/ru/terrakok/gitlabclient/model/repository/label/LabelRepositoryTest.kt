package ru.terrakok.gitlabclient.model.repository.label

import com.nhaarman.mockitokotlin2.anyOrNull
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.BDDMockito.*
import org.mockito.Mockito.mock
import ru.terrakok.gitlabclient.TestData
import ru.terrakok.gitlabclient.TestSchedulers
import ru.terrakok.gitlabclient.di.PrimitiveWrapper
import ru.terrakok.gitlabclient.model.data.server.GitlabApi
import ru.terrakok.gitlabclient.model.data.state.ServerChanges

/**
 * @author Vitaliy Belyaev on 26.05.2019.
 */
class LabelRepositoryTest {
    private val defaultPageSize = 2
    private val projectId = 123L
    private val testLabel = TestData.getLabel()

    private val api = mock(GitlabApi::class.java)
    private val repository = LabelRepository(
            api,
            ServerChanges(TestSchedulers()),
            PrimitiveWrapper(defaultPageSize),
            TestSchedulers())

    @Test
    fun `get label list should succeed with valid input`() {
        // GIVEN
        val testPage = 1

        given(api.getProjectLabels(
                anyLong(),
                anyInt(),
                anyInt())).willReturn(Single.just(listOf(testLabel)))

        // WHEN
        val testObserver = repository.getLabelList(projectId, testPage).test()
        testObserver.awaitTerminalEvent()

        // THEN
        then(api)
                .should(times(1))
                .getProjectLabels(projectId, testPage, defaultPageSize)

        then(api).shouldHaveNoMoreInteractions()

        testObserver.assertResult(listOf(testLabel))
    }

    @Test
    fun `create label should succeed with valid input`() {
        // GIVEN
        val labelName = "label name"
        val color = "color"
        val description = "description"
        val priority = 1

        given(api.createLabel(
                anyLong(),
                anyString(),
                anyString(),
                anyOrNull(),
                anyOrNull())).willReturn(Single.just(testLabel))

        // WHEN
        val testObserver = repository
                .createLabel(projectId, labelName, color, description, priority)
                .test()
        testObserver.awaitTerminalEvent()

        // THEN
        then(api)
                .should(times(1))
                .createLabel(projectId, labelName, color, description, priority)

        then(api).shouldHaveNoMoreInteractions()

        testObserver.assertResult(testLabel)
    }

    @Test
    fun `delete label should succeed with valid input`() {
        // GIVEN
        val labelName = "label name"

        given(api.deleteLabel(
                anyLong(),
                anyString())).willReturn(Completable.complete())

        // WHEN
        val testObserver = repository.deleteLabel(projectId, labelName).test()
        testObserver.awaitTerminalEvent()

        // THEN
        then(api)
                .should(times(1))
                .deleteLabel(projectId, labelName)

        then(api).shouldHaveNoMoreInteractions()

        testObserver
                .assertNoErrors()
                .assertComplete()
    }

    @Test
    fun `subscribe to label should succeed with valid input`() {
        // GIVEN
        val labelId = 343L

        given(api.subscribeToLabel(
                anyLong(),
                anyLong())).willReturn(Single.just(testLabel))

        // WHEN
        val testObserver = repository.subscribeToLabel(projectId, labelId).test()
        testObserver.awaitTerminalEvent()

        // THEN
        then(api)
                .should(times(1))
                .subscribeToLabel(projectId, labelId)

        then(api).shouldHaveNoMoreInteractions()

        testObserver.assertResult(testLabel)
    }

    @Test
    fun `unsubscribe from label should succeed with valid input`() {
        // GIVEN
        val labelId = 343L

        given(api.unsubscribeFromLabel(
                anyLong(),
                anyLong())).willReturn(Single.just(testLabel))

        // WHEN
        val testObserver = repository.unsubscribeFromLabel(projectId, labelId).test()
        testObserver.awaitTerminalEvent()

        // THEN
        then(api)
                .should(times(1))
                .unsubscribeFromLabel(projectId, labelId)

        then(api).shouldHaveNoMoreInteractions()

        testObserver.assertResult(testLabel)
    }
}
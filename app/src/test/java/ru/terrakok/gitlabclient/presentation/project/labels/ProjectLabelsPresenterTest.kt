package ru.terrakok.gitlabclient.presentation.project.labels

import com.nhaarman.mockito_kotlin.*
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import ru.terrakok.gitlabclient.entity.Label
import ru.terrakok.gitlabclient.model.interactor.label.LabelInteractor
import ru.terrakok.gitlabclient.presentation.global.ErrorHandler
import ru.terrakok.gitlabclient.toothpick.PrimitiveWrapper

/**
 * @author Maxim Myalkin (MaxMyalkin) on 11.11.2018.
 */
class ProjectLabelsPresenterTest {

    val projectId = 42L

    val view: ProjectLabelsView = mock()
    val interactor: LabelInteractor = mock()
    val errorHandler: ErrorHandler = mock()

    val presenter = ProjectLabelsPresenter(
        PrimitiveWrapper(projectId),
        interactor,
        errorHandler
    )

    @Test
    fun `show empty label list`() {
        whenever(interactor.getLabelList(projectId, 1)).thenReturn(Single.just(emptyList()))

        presenter.attachView(view)

        inOrder(view) {
            verify(view).showEmptyProgress(true)
            verify(view).showEmptyProgress(false)
            verify(view).showEmptyView(true)
        }
        verifyNoMoreInteractions(view)
    }

    @Test
    fun `show label list`() {
        val dataList = listOf(label(1), label(2), label(3))

        whenever(interactor.getLabelList(projectId, 1)).thenReturn(Single.just(dataList))

        presenter.attachView(view)

        val uiItems = dataList.map { LabelUi(it, isLoading = false) }

        inOrder(view) {
            verify(view).showEmptyProgress(true)
            verify(view).showItems(true, uiItems)
            verify(view).showEmptyProgress(false)
        }
        verifyNoMoreInteractions(view)
    }

    @Test
    fun `show label list with multiple pages`() {
        val firstPageLabels = listOf(label(1), label(2), label(3))
        val secondPageLabels = listOf(label(4), label(5), label(6))

        whenever(interactor.getLabelList(eq(projectId), any()))
            .thenReturn(Single.just(firstPageLabels))
            .thenReturn(Single.just(secondPageLabels))

        presenter.attachView(view)
        presenter.loadNextLabelsPage()

        val firstPageItems = firstPageLabels.map { LabelUi(it, isLoading = false) }
        val secondPageItems = secondPageLabels.map { LabelUi(it, isLoading = false) }

        inOrder(view) {
            verify(view).showEmptyProgress(true)
            verify(view).showItems(true, firstPageItems)
            verify(view).showEmptyProgress(false)
            verify(view).showPageProgress(true)
            verify(view).showPageProgress(false)
            verify(view).showItems(true, firstPageItems + secondPageItems)
        }

        verifyNoMoreInteractions(view)
    }

    @Test
    fun `show label list with error on first page`() {
        val error = RuntimeException("error")

        whenever(interactor.getLabelList(eq(projectId), any()))
            .thenReturn(Single.error(error))

        presenter.attachView(view)

        inOrder(view, errorHandler) {
            verify(view).showEmptyProgress(true)
            verify(view).showEmptyProgress(false)
            verify(errorHandler).proceed(eq(error), any())
        }

        verifyNoMoreInteractions(view, errorHandler)
    }

    @Test
    fun `show label list with error on second page`() {
        val firstPageLabels = listOf(label(1), label(2), label(3))
        val error = RuntimeException("error")

        whenever(interactor.getLabelList(eq(projectId), any()))
            .thenReturn(Single.just(firstPageLabels))
            .thenReturn(Single.error(error))

        presenter.attachView(view)
        presenter.loadNextLabelsPage()

        inOrder(view, errorHandler) {
            verify(view).showPageProgress(true)
            verify(view).showPageProgress(false)
            verify(errorHandler).proceed(eq(error), any())
        }
    }

    @Test
    fun `successfully unsubscribe from label`() {
        val firstPageLabels = listOf(label(1, subscribed = true), label(2), label(3))
        val labelToToggle = firstPageLabels[0]

        whenever(interactor.getLabelList(eq(projectId), any()))
            .thenReturn(Single.just(firstPageLabels))
        whenever(interactor.unsubscribeFromLabel(eq(projectId), eq(labelToToggle.id)))
            .thenReturn(Single.just(label(1, subscribed = false)))

        presenter.attachView(view)
        presenter.toggleSubscription(LabelUi(labelToToggle, isLoading = false))

        val labelsBeforeToggling = firstPageLabels.map { LabelUi(it, isLoading = false) }
        val labelsWithLoading = listOf(
            LabelUi(label(1, subscribed = true), isLoading = true),
            LabelUi(label(2), isLoading = false),
            LabelUi(label(3), isLoading = false)
        )
        val labelsAfterToggling = listOf(
            LabelUi(label(1, subscribed = false), isLoading = false),
            LabelUi(label(2), isLoading = false),
            LabelUi(label(3), isLoading = false)
        )

        argumentCaptor<List<LabelUi>>().apply {
            verify(view, times(3)).showItems(eq(true), capture())
            assertEquals(labelsBeforeToggling, firstValue)
            assertEquals(labelsWithLoading, secondValue)
            assertEquals(labelsAfterToggling, thirdValue)
        }
    }

    @Test
    fun `successfully subscribe to label`() {
        val firstPageLabels = listOf(label(1, subscribed = false), label(2), label(3))
        val labelToToggle = firstPageLabels[0]

        whenever(interactor.getLabelList(eq(projectId), any()))
            .thenReturn(Single.just(firstPageLabels))
        whenever(interactor.subscribeToLabel(eq(projectId), eq(labelToToggle.id)))
            .thenReturn(Single.just(label(1, subscribed = true)))

        presenter.attachView(view)
        presenter.toggleSubscription(LabelUi(labelToToggle, isLoading = false))

        val labelsBeforeToggling = firstPageLabels.map { LabelUi(it, isLoading = false) }
        val labelsWithLoading = listOf(
            LabelUi(label(1, subscribed = false), isLoading = true),
            LabelUi(label(2), isLoading = false),
            LabelUi(label(3), isLoading = false)
        )
        val labelsAfterToggling = listOf(
            LabelUi(label(1, subscribed = true), isLoading = false),
            LabelUi(label(2), isLoading = false),
            LabelUi(label(3), isLoading = false)
        )

        argumentCaptor<List<LabelUi>>().apply {
            verify(view, times(3)).showItems(eq(true), capture())
            assertEquals(labelsBeforeToggling, firstValue)
            assertEquals(labelsWithLoading, secondValue)
            assertEquals(labelsAfterToggling, thirdValue)
        }
    }


    @Test
    fun `unsubscribe from label with fail`() {
        val firstPageLabels = listOf(label(1, subscribed = true), label(2), label(3))
        val labelToToggle = firstPageLabels[0]
        val error = RuntimeException("error")

        whenever(interactor.getLabelList(eq(projectId), any()))
            .thenReturn(Single.just(firstPageLabels))
        whenever(interactor.unsubscribeFromLabel(eq(projectId), eq(labelToToggle.id)))
            .thenReturn(Single.error(error))

        presenter.attachView(view)
        presenter.toggleSubscription(LabelUi(labelToToggle, isLoading = false))

        val labelsBeforeToggling = firstPageLabels.map { LabelUi(it, isLoading = false) }
        val labelsWithLoading = listOf(
            LabelUi(label(1, subscribed = true), isLoading = true),
            LabelUi(label(2), isLoading = false),
            LabelUi(label(3), isLoading = false)
        )

        argumentCaptor<List<LabelUi>>().apply {
            verify(view, times(3)).showItems(eq(true), capture())
            assertEquals(labelsBeforeToggling, firstValue)
            assertEquals(labelsWithLoading, secondValue)
            assertEquals(labelsBeforeToggling, thirdValue)
        }
        verify(errorHandler).proceed(eq(error), any())
    }

    private fun label(id: Long, subscribed: Boolean = false): Label {
        return Label(
            id = id,
            name = "name$id",
            color = "color$id",
            description = null,
            openIssuesCount = 0,
            closedIssuesCount = 0,
            openMergeRequestsCount = 0,
            subscribed = subscribed,
            priority = null
        )
    }

}
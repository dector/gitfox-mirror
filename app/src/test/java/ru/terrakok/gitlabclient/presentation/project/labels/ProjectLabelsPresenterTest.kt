package ru.terrakok.gitlabclient.presentation.project.labels

import com.nhaarman.mockito_kotlin.*
import io.reactivex.Single
import org.junit.Assert.assertEquals
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

        inOrder(view) {
            verify(view).showEmptyProgress(true)
            verify(view).showLabels(true, dataList)
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

        inOrder(view) {
            verify(view).showEmptyProgress(true)
            verify(view).showLabels(true, firstPageLabels)
            verify(view).showEmptyProgress(false)
        }

        presenter.loadNextLabelsPage()

        inOrder(view) {
            verify(view).showPageProgress(true)
            verify(view).showPageProgress(false)
            verify(view).showLabels(true, firstPageLabels + secondPageLabels)
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
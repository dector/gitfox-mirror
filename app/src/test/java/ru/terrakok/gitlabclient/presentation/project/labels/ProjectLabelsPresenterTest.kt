package ru.terrakok.gitlabclient.presentation.project.labels

import com.nhaarman.mockitokotlin2.*
import io.reactivex.Single
import org.junit.Test
import ru.terrakok.gitlabclient.di.PrimitiveWrapper
import ru.terrakok.gitlabclient.entity.Color
import ru.terrakok.gitlabclient.entity.Label
import ru.terrakok.gitlabclient.model.interactor.label.LabelInteractor
import ru.terrakok.gitlabclient.model.system.flow.FlowRouter
import ru.terrakok.gitlabclient.presentation.global.ErrorHandler

/**
 * @author Maxim Myalkin (MaxMyalkin) on 11.11.2018.
 *
 * @author Vitaliy Belyaev on 19.05.2019.
 */
class ProjectLabelsPresenterTest {

    private val projectId = 42L

    private val view: ProjectLabelsView = mock()
    private val interactor: LabelInteractor = mock()
    private val errorHandler: ErrorHandler = mock()
    private val flowRouter: FlowRouter = mock()

    private val presenter = ProjectLabelsPresenter(
        PrimitiveWrapper(projectId),
        interactor,
        errorHandler,
        flowRouter
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
            color = Color("green", 12),
            description = null,
            openIssuesCount = 0,
            closedIssuesCount = 0,
            openMergeRequestsCount = 0,
            subscribed = subscribed,
            priority = null
        )
    }
}
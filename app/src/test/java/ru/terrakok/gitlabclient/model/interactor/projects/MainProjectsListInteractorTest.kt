package ru.terrakok.gitlabclient.model.interactor.projects

import com.nhaarman.mockito_kotlin.*
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import ru.terrakok.gitlabclient.entity.OrderBy
import ru.terrakok.gitlabclient.model.repository.project.ProjectRepository

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 04.06.17.
 */
class MainProjectsListInteractorTest {

    private lateinit var projectRepository: ProjectRepository
    private val testPage = 0
    private val testError = RuntimeException()

    @Before
    fun setUp() {
        projectRepository = mock<ProjectRepository>()
    }

    @Test
    fun getMainProjects_success() {
        whenever(projectRepository.getProjectsList(
                anyOrNull(), anyOrNull(), anyOrNull(),
                anyOrNull(), anyOrNull(), anyOrNull(),
                anyOrNull(), anyOrNull(), anyOrNull(),
                any(), any())).thenReturn(Single.just(emptyList()))

        val interactor = MainProjectsListInteractor(projectRepository)

        val testObserver = interactor.getMainProjects(testPage).test()
        testObserver.awaitTerminalEvent()

        verify(projectRepository).getProjectsList(
                eq(false),
                eq(null),
                eq(OrderBy.LAST_ACTIVITY_AT),
                eq(null),
                eq(null),
                eq(null),
                eq(null),
                eq(true),
                eq(null),
                eq(testPage),
                any()
        )
        testObserver
                .assertValueCount(1)
                .assertNoErrors()
    }

    @Test
    fun getMainProjects_error() {
        whenever(projectRepository.getProjectsList(
                anyOrNull(), anyOrNull(), anyOrNull(),
                anyOrNull(), anyOrNull(), anyOrNull(),
                anyOrNull(), anyOrNull(), anyOrNull(),
                any(), any())).thenReturn(Single.error(testError))

        val interactor = MainProjectsListInteractor(projectRepository)

        val testObserver = interactor.getMainProjects(testPage).test()
        testObserver.awaitTerminalEvent()

        verify(projectRepository).getProjectsList(
                eq(false),
                eq(null),
                eq(OrderBy.LAST_ACTIVITY_AT),
                eq(null),
                eq(null),
                eq(null),
                eq(null),
                eq(true),
                eq(null),
                eq(testPage),
                any()
        )
        testObserver
                .assertNoValues()
                .assertError(testError)
    }

    @Test
    fun getMyProjects_success() {
        whenever(projectRepository.getProjectsList(
                anyOrNull(), anyOrNull(), anyOrNull(),
                anyOrNull(), anyOrNull(), anyOrNull(),
                anyOrNull(), anyOrNull(), anyOrNull(),
                any(), any())).thenReturn(Single.just(emptyList()))

        val interactor = MainProjectsListInteractor(projectRepository)

        val testObserver = interactor.getMyProjects(testPage).test()
        testObserver.awaitTerminalEvent()

        verify(projectRepository).getProjectsList(
                eq(false),
                eq(null),
                eq(OrderBy.LAST_ACTIVITY_AT),
                eq(null),
                eq(null),
                eq(null),
                eq(true),
                eq(null),
                eq(null),
                eq(testPage),
                any()
        )
        testObserver
                .assertValueCount(1)
                .assertNoErrors()
    }

    @Test
    fun getMyProjects_error() {
        whenever(projectRepository.getProjectsList(
                anyOrNull(), anyOrNull(), anyOrNull(),
                anyOrNull(), anyOrNull(), anyOrNull(),
                anyOrNull(), anyOrNull(), anyOrNull(),
                any(), any())).thenReturn(Single.error(testError))

        val interactor = MainProjectsListInteractor(projectRepository)

        val testObserver = interactor.getMyProjects(testPage).test()
        testObserver.awaitTerminalEvent()

        verify(projectRepository).getProjectsList(
                eq(false),
                eq(null),
                eq(OrderBy.LAST_ACTIVITY_AT),
                eq(null),
                eq(null),
                eq(null),
                eq(true),
                eq(null),
                eq(null),
                eq(testPage),
                any()
        )
        testObserver
                .assertNoValues()
                .assertError(testError)
    }

    @Test
    fun getStarredProjects_success() {
        whenever(projectRepository.getProjectsList(
                anyOrNull(), anyOrNull(), anyOrNull(),
                anyOrNull(), anyOrNull(), anyOrNull(),
                anyOrNull(), anyOrNull(), anyOrNull(),
                any(), any())).thenReturn(Single.just(emptyList()))

        val interactor = MainProjectsListInteractor(projectRepository)

        val testObserver = interactor.getStarredProjects(testPage).test()
        testObserver.awaitTerminalEvent()

        verify(projectRepository).getProjectsList(
                eq(false),
                eq(null),
                eq(OrderBy.LAST_ACTIVITY_AT),
                eq(null),
                eq(null),
                eq(null),
                eq(null),
                eq(null),
                eq(true),
                eq(testPage),
                any()
        )
        testObserver
                .assertValueCount(1)
                .assertNoErrors()
    }

    @Test
    fun getStarredProjects_error() {
        whenever(projectRepository.getProjectsList(
                anyOrNull(), anyOrNull(), anyOrNull(),
                anyOrNull(), anyOrNull(), anyOrNull(),
                anyOrNull(), anyOrNull(), anyOrNull(),
                any(), any())).thenReturn(Single.error(testError))

        val interactor = MainProjectsListInteractor(projectRepository)

        val testObserver = interactor.getStarredProjects(testPage).test()
        testObserver.awaitTerminalEvent()

        verify(projectRepository).getProjectsList(
                eq(false),
                eq(null),
                eq(OrderBy.LAST_ACTIVITY_AT),
                eq(null),
                eq(null),
                eq(null),
                eq(null),
                eq(null),
                eq(true),
                eq(testPage),
                any()
        )
        testObserver
                .assertNoValues()
                .assertError(testError)
    }
}
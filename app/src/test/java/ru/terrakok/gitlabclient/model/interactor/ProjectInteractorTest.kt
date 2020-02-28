package ru.terrakok.gitlabclient.model.interactor

import com.nhaarman.mockitokotlin2.anyOrNull
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
import ru.terrakok.gitlabclient.entity.app.ProjectFile
import ru.terrakok.gitlabclient.model.data.server.GitlabApi
import ru.terrakok.gitlabclient.model.data.state.ServerChanges

/**
 * @author Vitaliy Belyaev on 03.06.2019.
 */
class ProjectInteractorTest {
    private val defaultPageSize = 1
    private val testPage = 2
    private val testProject = TestData.getProject(123L)

    private val api = Mockito.mock(GitlabApi::class.java)
    private val interactor = ProjectInteractor(
        api,
        ServerChanges(TestSchedulers()),
        TestSchedulers(),
        PrimitiveWrapper(defaultPageSize)
    )

    @Test
    fun `get projects should succeed with valid api response`() {
        // GIVEN
        given(api.getProjects(
                anyOrNull(), anyOrNull(), anyOrNull(), anyOrNull(), anyOrNull(),
                anyOrNull(), anyOrNull(), anyOrNull(), anyOrNull(), anyInt(), anyInt()))
                .willReturn(Single.just(listOf(testProject)))

        // WHEN
        val testObserver = interactor.getProjectsList(page = testPage).test()
        testObserver.awaitTerminalEvent()

        // THEN
        then(api)
                .should(times(1))
                .getProjects(
                        null, null, null, null, null,
                        null, null, null, null,
                        testPage, defaultPageSize)

        then(api).shouldHaveNoMoreInteractions()

        testObserver.assertResult(listOf(testProject))
    }

    @Test
    fun `get project should succeed with valid api response`() {
        // GIVEN
        given(api.getProject(anyLong(), anyBoolean())).willReturn(Single.just(testProject))

        // WHEN
        val testObserver = interactor.getProject(testProject.id).test()
        testObserver.awaitTerminalEvent()

        // THEN
        then(api)
                .should(times(1))
                .getProject(testProject.id, true)

        then(api).shouldHaveNoMoreInteractions()

        testObserver.assertResult(testProject)
    }

    @Test
    fun `get project file should succeed with valid api response`() {
        // GIVEN
        val fileReference = "file reference"
        val testFile = TestData.getFile()
        given(api.getFile(anyLong(), anyString(), anyString())).willReturn(Single.just(testFile))

        // WHEN
        val testObserver = interactor.getProjectFile(
                testProject.id, testFile.path, fileReference).test()

        testObserver.awaitTerminalEvent()

        // THEN
        then(api)
                .should(times(1))
                .getFile(testProject.id, testFile.path, fileReference)

        then(api).shouldHaveNoMoreInteractions()

        testObserver.assertResult(testFile)
    }

    @Test
    fun `get project files should map tree nodes to projects files`() {
        // GIVEN
        val path = "some path"
        val branchName = "some branch name"
        val testTree = TestData.getRepositoryTreeNode()
        val testProjectFile = ProjectFile(testTree.id, testTree.name, testTree.type)

        given(api.getRepositoryTree(
                anyLong(), anyOrNull(), anyOrNull(), anyOrNull(), anyInt(), anyInt()))
                .willReturn(Single.just(listOf(testTree)))

        // WHEN
        val testObserver = interactor.getProjectFiles(
                projectId = testProject.id,
                path = path,
                branchName = branchName,
                page = testPage).test()

        testObserver.awaitTerminalEvent()

        // THEN
        then(api)
                .should(times(1))
                .getRepositoryTree(
                        testProject.id, path, branchName, null, testPage, defaultPageSize)

        then(api).shouldHaveNoMoreInteractions()

        testObserver.assertResult(listOf(testProjectFile))
    }

    @Test
    fun `get project branches should succeed with valid api response`() {
        // GIVEN
        val testBranch = TestData.getBranch()
        given(api.getRepositoryBranches(anyLong())).willReturn(Single.just(listOf(testBranch)))

        // WHEN
        val testObserver = interactor.getProjectBranches(testProject.id).test()
        testObserver.awaitTerminalEvent()

        // THEN
        then(api)
                .should(times(1))
                .getRepositoryBranches(testProject.id)

        then(api).shouldHaveNoMoreInteractions()

        testObserver.assertResult(listOf(testBranch))
    }
}

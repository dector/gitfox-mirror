package ru.terrakok.gitlabclient.model.interactor.project

import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Test
import org.mockito.Mockito.*
import ru.terrakok.gitlabclient.TestData
import ru.terrakok.gitlabclient.TestSchedulers
import ru.terrakok.gitlabclient.entity.Branch
import ru.terrakok.gitlabclient.entity.OrderBy
import ru.terrakok.gitlabclient.entity.RepositoryTreeNodeType
import ru.terrakok.gitlabclient.entity.app.ProjectFile
import ru.terrakok.gitlabclient.model.repository.project.ProjectRepository
import ru.terrakok.gitlabclient.model.repository.tools.Base64Tools

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 28.05.17.
 *
 * @author Vitaliy Belyaev on 19.05.19.
 */
class ProjectInteractorTest {
    private val base64Tools = mock(Base64Tools::class.java)
    private val repo = mock(ProjectRepository::class.java)
    private val testNodeType = mock(RepositoryTreeNodeType::class.java)
    private val interactor = ProjectInteractor(repo, TestSchedulers(), base64Tools)

    private val testError = RuntimeException("test error")
    private val testPage = 13
    private val testProject = TestData.getProject(42L)
    private val testProjectsList = listOf(testProject)

    @Test
    fun get_main_projects() {
        `when`(repo.getProjectsList(
                archived = any(),
                visibility = any(),
                orderBy = any(),
                sort = any(),
                search = any(),
                simple = any(),
                owned = any(),
                membership = any(),
                starred = any(),
                page = anyInt(),
                pageSize = anyInt())).thenReturn(Single.just(testProjectsList))

        val testObserver = interactor.getMainProjects(testPage).test()
        testObserver.awaitTerminalEvent()

        verify(repo, times(1)).getProjectsList(
                page = testPage,
                membership = true,
                orderBy = OrderBy.LAST_ACTIVITY_AT,
                archived = false)

        testObserver
                .assertNoErrors()
                .assertValue(testProjectsList)
                .assertComplete()
    }

    @Test
    fun get_my_projects() {
        `when`(repo.getProjectsList(
                archived = any(),
                visibility = any(),
                orderBy = any(),
                sort = any(),
                search = any(),
                simple = any(),
                owned = any(),
                membership = any(),
                starred = any(),
                page = anyInt(),
                pageSize = anyInt())).thenReturn(Single.just(testProjectsList))

        val testObserver = interactor.getMyProjects(testPage).test()
        testObserver.awaitTerminalEvent()

        verify(repo, times(1)).getProjectsList(
                page = testPage,
                owned = true,
                orderBy = OrderBy.LAST_ACTIVITY_AT,
                archived = false)

        testObserver
                .assertNoErrors()
                .assertValue(testProjectsList)
                .assertComplete()
    }

    @Test
    fun get_starred_projects() {
        `when`(repo.getProjectsList(
                archived = any(),
                visibility = any(),
                orderBy = any(),
                sort = any(),
                search = any(),
                simple = any(),
                owned = any(),
                membership = any(),
                starred = any(),
                page = anyInt(),
                pageSize = anyInt())).thenReturn(Single.just(testProjectsList))

        val testObserver = interactor.getStarredProjects(testPage).test()
        testObserver.awaitTerminalEvent()

        verify(repo, times(1)).getProjectsList(
                page = testPage,
                starred = true,
                orderBy = OrderBy.LAST_ACTIVITY_AT,
                archived = false)

        testObserver
                .assertNoErrors()
                .assertValue(testProjectsList)
                .assertComplete()
    }

    @Test
    fun get_single_project() {
        `when`(repo.getProject(anyLong())).thenReturn(Single.just(testProject))

        val testObserver = interactor.getProject(testProject.id).test()
        testObserver.awaitTerminalEvent()

        verify(repo, times(1)).getProject(testProject.id)
        testObserver
                .assertNoErrors()
                .assertValue(testProject)
                .assertComplete()
    }

    @Test
    fun get_project_error() {
        `when`(repo.getProject(anyLong())).thenReturn(Single.error(testError))

        val testObserver = interactor.getProject(testProject.id).test()
        testObserver.awaitTerminalEvent()

        verify(repo, times(1)).getProject(testProject.id)
        testObserver
                .assertNoValues()
                .assertError(testError)
    }

    @Test
    fun get_project_raw_file() {
        val raw = "lorem ipsum"
        val testFileContent = "bG9yZW0gaXBzdW0=" //base64 for raw
        val testFile = TestData.getFile(testFileContent, testProject.defaultBranch!!)

        `when`(repo.getProjectFile(anyLong(), anyString(), anyString())).thenReturn(Single.just(testFile))
        `when`(base64Tools.decode(anyString())).thenReturn(raw)

        val testObserver = interactor
                .getProjectRawFile(testProject.id, "README.md", testProject.defaultBranch!!)
                .test()
        testObserver.awaitTerminalEvent()

        verify(repo, times(1))
                .getProjectFile(testProject.id, "README.md", testProject.defaultBranch!!)
        verify(base64Tools, times(1)).decode(testFileContent)
        testObserver
                .assertNoErrors()
                .assertValue(raw)
                .assertComplete()
    }

    @Test
    fun get_project_readme() {
        val raw = "lorem ipsum"
        val testFileContent = "bG9yZW0gaXBzdW0=" //base64 for raw
        val testFile = TestData.getFile(testFileContent, testProject.defaultBranch!!)

        `when`(repo.getProjectFile(anyLong(), anyString(), anyString())).thenReturn(Single.just(testFile))
        `when`(base64Tools.decode(anyString())).thenReturn(raw)

        val testObserver = interactor.getProjectReadme(testProject).test()
        testObserver.awaitTerminalEvent()

        verify(repo, times(1))
                .getProjectFile(testProject.id, "README.md", testProject.defaultBranch!!)
        verify(base64Tools, times(1)).decode(testFileContent)

        testObserver
                .assertNoErrors()
                .assertValue(raw)
                .assertComplete()
    }

    @Test
    fun get_project_readme_not_found_error() {
        val projectWithoutReadme = testProject.copy(defaultBranch = null, readmeUrl = null)

        val testObserver: TestObserver<String> = interactor.getProjectReadme(projectWithoutReadme).test()
        testObserver.awaitTerminalEvent()

        verify(repo, times(1)).projectChanges
        verifyZeroInteractions(base64Tools)

        testObserver
                .assertNoValues()
                .assertError { it is ProjectInteractor.ReadmeNotFound }
    }

    @Test
    fun get_project_readme_error_from_repo() {
        `when`(repo.getProjectFile(anyLong(), anyString(), anyString())).thenReturn(Single.error(testError))

        val testObserver = interactor.getProjectReadme(testProject).test()
        testObserver.awaitTerminalEvent()

        verify(repo, times(1))
                .getProjectFile(testProject.id, "README.md", testProject.defaultBranch!!)
        verifyZeroInteractions(base64Tools)

        testObserver
                .assertNoValues()
                .assertError(testError)
    }

    @Test
    fun get_projects_files() {
        val projectFilesList = listOf(ProjectFile("1", "name", testNodeType))

        `when`(repo.getProjectFiles(
                anyLong(),
                anyString(),
                anyString(),
                any(),
                anyInt(),
                anyInt())).thenReturn(Single.just(projectFilesList))

        val testObserver = interactor.getProjectFiles(
                testProject.id,
                testProject.path,
                testProject.defaultBranch!!,
                testPage).test()
        testObserver.awaitTerminalEvent()

        verify(repo, times(1))
                .getProjectFiles(
                        projectId = testProject.id,
                        path = testProject.path,
                        branchName = testProject.defaultBranch!!,
                        page = testPage)

        testObserver
                .assertNoErrors()
                .assertValue(projectFilesList)
                .assertComplete()
    }

    @Test
    fun get_projects_branches() {
        val testBranch = Branch(
                "1",
                merged = true,
                protected = true,
                default = true,
                developersCanPush = true,
                developersCanMerge = true,
                canPush = true)

        val testBranches = listOf(testBranch)

        `when`(repo.getProjectBranches(anyLong())).thenReturn(Single.just(testBranches))

        val testObserver = interactor.getProjectBranches(testProject.id).test()
        testObserver.awaitTerminalEvent()

        verify(repo, times(1)).getProjectBranches(testProject.id)

        testObserver
                .assertNoErrors()
                .assertValue(testBranches)
                .assertComplete()
    }
}
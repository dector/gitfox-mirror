package ru.terrakok.gitlabclient.model.interactor.project

import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Test
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.Mockito.*
import ru.terrakok.gitlabclient.TestSchedulers
import ru.terrakok.gitlabclient.entity.File
import ru.terrakok.gitlabclient.entity.Project
import ru.terrakok.gitlabclient.entity.Visibility
import ru.terrakok.gitlabclient.model.repository.project.ProjectRepository

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 28.05.17.
 */
class ProjectInteractorTest {
    private val schedulers = TestSchedulers()
    private val base64Tools = mock(Base64Tools::class.java)

    @Test
    fun get_single_project() {
        val projectId = 42L
        val testProject = Project(projectId, null, "", Visibility.PUBLIC, null, null,
                null, null, null, null, null, "", null, false,
                0L, false, false, false, false, false, null, null, 0L, null, null, false, null, false,
                0L, 0L, null, false, null, false, false, false)

        val repo = mock(ProjectRepository::class.java)
        `when`(repo.getProject(anyLong())).thenReturn(Single.just(testProject))

        val interactor = ProjectInteractor(repo, mock(MarkDownConverter::class.java), schedulers, base64Tools)

        val testObserver: TestObserver<Project> = interactor.getProject(projectId).test()
        testObserver.awaitTerminalEvent()

        verify(repo, times(1)).getProject(projectId)
        testObserver
                .assertValueCount(1)
                .assertNoErrors()
                .assertValue(testProject)
    }

    @Test
    fun get_project_error() {
        val projectId = 42L
        val error = RuntimeException("test error")

        val repo = mock(ProjectRepository::class.java)
        `when`(repo.getProject(anyLong())).thenReturn(Single.error(error))

        val interactor = ProjectInteractor(repo, mock(MarkDownConverter::class.java), schedulers, base64Tools)

        val testObserver: TestObserver<Project> = interactor.getProject(projectId).test()
        testObserver.awaitTerminalEvent()

        verify(repo, times(1)).getProject(projectId)
        testObserver
                .assertValueCount(0)
                .assertError(error)
    }

    @Test
    fun get_project_readme() {
        val projectId = 42L
        val branchName = "test_br"
        val raw = "lorem ipsum"
        val testFileContent = "bG9yZW0gaXBzdW0=" //base64 for raw
        val testFile = File("", "", 0L, "", testFileContent, branchName, "", "", "")
        val correctHtml = "correct html"

        val repo = mock(ProjectRepository::class.java)
        `when`(repo.getFile(anyLong(), anyString(), anyString())).thenReturn(Single.just(testFile))

        val mdConverter = mock(MarkDownConverter::class.java)
        `when`(mdConverter.markdownToHtml(anyString())).thenReturn(correctHtml)

        val base64 = mock(Base64Tools::class.java)
        `when`(base64.decode(anyString())).thenReturn(raw)

        val interactor = ProjectInteractor(repo, mdConverter, schedulers, base64)

        val testObserver: TestObserver<String> = interactor.getProjectReadmeHtml(projectId, branchName).test()
        testObserver.awaitTerminalEvent()

        verify(repo, times(1)).getFile(projectId, "README.md", branchName)
        verify(base64, times(1)).decode(testFileContent)
        verify(mdConverter, times(1)).markdownToHtml(raw)
        testObserver
                .assertValueCount(1)
                .assertNoErrors()
                .assertValue(correctHtml)
    }

    @Test
    fun get_project_readme_error() {
        val error = RuntimeException("test error")
        val projectId = 42L
        val branchName = "test_br"

        val repo = mock(ProjectRepository::class.java)
        `when`(repo.getFile(anyLong(), anyString(), anyString())).thenReturn(Single.error(error))

        val mdConverter = mock(MarkDownConverter::class.java)
        val base64 = mock(Base64Tools::class.java)

        val interactor = ProjectInteractor(repo, mdConverter, schedulers, base64)

        val testObserver: TestObserver<String> = interactor.getProjectReadmeHtml(projectId, branchName).test()
        testObserver.awaitTerminalEvent()

        verify(repo, times(1)).getFile(projectId, "README.md", branchName)
        verifyZeroInteractions(base64)
        verifyZeroInteractions(mdConverter)
        testObserver
                .assertNoValues()
                .assertError(error)
    }

}